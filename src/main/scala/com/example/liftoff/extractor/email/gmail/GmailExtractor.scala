package com.example.liftoff.extractor.email.gmail

import com.example.liftoff.error.{ExtractorAuthError, ExtractorError}
import com.example.liftoff.extractor.email.EmailExtractor
import com.example.liftoff.extractor.email.gmail.objects._
import com.example.liftoff.storage.Storage

import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat
import java.util.Base64

class GmailExtractor(emailAddress: String, credentialPath: String, storage: Storage) extends EmailExtractor(emailAddress: String, credentialPath: String, storage: Storage) {
  private val currentUser = "me"
  private val gmailClient = new GmailClient()

  override def authenticate(): Option[ExtractorError] = {
    gmailClient.authenticate(credentialPath)
    None
  }

  override def close(): Option[ExtractorError] = {
    None
  }

  private def extractParts(payload: MessagePart): List[(MessagePart, String)] = {
    val gmailDateTimeFormat = "E, d MMM yyyy HH:mm:ss Z (z)"
    val savedAttachmentDateTimeFormat = "dd.MM.yyyy_HH:mm:ss"
    val receivedDateString = payload.headers.find(_.name == "Received").get.value.split(";").last.trim
    val receivedDate = new SimpleDateFormat(gmailDateTimeFormat).parse(receivedDateString)
    val receivedDateNewFormat = new SimpleDateFormat(savedAttachmentDateTimeFormat).format(receivedDate)
    val subject = payload.headers.find(_.name == "Subject").map(_.value)
    val saveDirectoryName = s"${receivedDateNewFormat}_${subject.getOrElse("None")}"
    payload.parts.get.map(part => (part, saveDirectoryName))
  }

  private def downloadAttachment(part: MessagePart, saveDirectoryName: String): Option[Attachment] = {
    this.gmailClient.getAttachment(this.currentUser, part.partId, part.body.attachmentId.get) match {
      case Right(body) => body.data match {
        case Some(data) => Some(Attachment(part.filename, saveDirectoryName, data.trim))
        case None => None
      }
      case Left(error) =>
        println(error)
        None
    }
  }

  private def saveAttachment(attachment: Attachment): Unit = {
    storage.storeFile(
      new ByteArrayInputStream(Base64.getUrlDecoder.decode(attachment.data)),
      attachment.fileName,
      Some(attachment.saveDirectoryName)
    )
  }

  override def extract(): Option[ExtractorError] = {
    if (!this.gmailClient.isAuthorized) {
      return Some(ExtractorAuthError)
    }
    val messageList = this.gmailClient.getMessageList(this.currentUser) match {
      case Right(body) => body
      case Left(error) =>
        println(error)
        return None
    }
    (for (message <- messageList.messages) yield this.gmailClient.getMessage(this.currentUser, message.id))
      .collect { case Right(downloadedMessageData) => downloadedMessageData.payload }
      .collect { case Some(payload) => payload }
      .collect { case payload if payload.parts.isDefined => extractParts(payload) }
      .flatten
      .collect { case (part, saveDirectoryName) if part.body.attachmentId.isDefined => downloadAttachment(part, saveDirectoryName) }
      .collect { case Some(downloadedAttachment) => downloadedAttachment }
      .foreach(saveAttachment)
    None
  }
}
