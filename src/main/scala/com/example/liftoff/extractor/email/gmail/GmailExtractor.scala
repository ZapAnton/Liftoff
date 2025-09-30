package com.example.liftoff.extractor.email.gmail

import com.example.liftoff.error.{ExtractorAuthError, ExtractorClientError, ExtractorError}
import com.example.liftoff.extractor.email.EmailExtractor
import com.example.liftoff.extractor.email.gmail.objects._
import com.example.liftoff.storage.Storage
import zio.{IO, ZIO}

import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat
import java.util.Base64

class GmailExtractor(emailAddress: String, credentialPath: String, storage: Storage) extends EmailExtractor(emailAddress: String, credentialPath: String, storage: Storage) {
  private val currentUser = "me"
  private val gmailClient = new GmailClient()

  override def authenticate(): IO[ExtractorError, Unit] = {
    gmailClient.authenticate(credentialPath)
    ZIO.succeed()
  }

  override def close(): IO[ExtractorError, Unit] = {
    ZIO.succeed()
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

  private def downloadAttachment(part: MessagePart, saveDirectoryName: String): IO[ExtractorError, Attachment] = {
    this.gmailClient
      .getAttachment(this.currentUser, part.partId, part.body.attachmentId.get)
      .map(downloadedBody => Attachment(part.filename, saveDirectoryName, downloadedBody.data.get.trim))
      .mapError(error => ExtractorClientError(error.getMessage))
  }

  private def saveAttachment(attachment: Attachment): IO[ExtractorError, Unit] = {
    ZIO.attempt(storage.storeFile(
      new ByteArrayInputStream(Base64.getUrlDecoder.decode(attachment.data)),
      attachment.fileName,
      Some(attachment.saveDirectoryName)
    )).map(_ => ()).mapError(error => ExtractorClientError(error.getMessage))
  }

  override def extract(): IO[ExtractorError, Unit] = {
    for {
      _ <- ZIO.when(!this.gmailClient.isAuthorized) (ZIO.fail(ExtractorAuthError))
      messageList <- this.gmailClient
        .getMessageList(this.currentUser)
        .mapError(error => ExtractorClientError(error.getMessage))
      messagesData <- ZIO.collectAll(
        messageList.messages
          .map(message => this.gmailClient.getMessage(this.currentUser, message.id)
            .mapError(error => ExtractorClientError(error.getMessage))))
      messagePartsList <- ZIO.succeed(messagesData
        .filter(message => message.payload.isDefined && message.payload.get.parts.isDefined)
        .flatMap(message => extractParts(message.payload.get))
      )
      attachmentList <- ZIO.collectAll(
        messagePartsList
          .filter{case (part, _) => part.body.attachmentId.isDefined}
          .map{case (part, saveDirectoryName) => downloadAttachment(part, saveDirectoryName)}
      )
      _ <- ZIO.foreach(attachmentList)(saveAttachment)
    } yield ()
  }
}
