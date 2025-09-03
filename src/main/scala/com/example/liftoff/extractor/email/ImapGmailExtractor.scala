package com.example.liftoff.extractor.email

import com.example.liftoff.extractor.{ExtractorAuthError, ExtractorConnectionCloseError, ExtractorConnectionError, ExtractorError}
import com.example.liftoff.storage.Storage
import jakarta.mail._
import jakarta.mail.internet.MimeBodyPart

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Properties
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.util.{Failure, Success, Using}

class ImapGmailExtractor(emailAddress: String, userToken: String, storage: Storage) extends EmailExtractor(emailAddress: String, userToken: String, storage: Storage) {
  private var store: Store = _

  private def containsMultipart(message: Message): Boolean = {
    try {
      message.getContentType.contains("multipart")
    } catch {
      case e: MessagingException =>
        Console.err.println(s"Failed to access multipart content: ${e.getMessage}")
        false
    }
  }

  private def toEmail(message: Message): Option[Email] = {
    try {
      val multipartContent: Multipart = message.getContent.asInstanceOf[Multipart]
      val attachments = (for (i <- 0 until multipartContent.getCount)
        yield multipartContent.getBodyPart(i).asInstanceOf[MimeBodyPart])
        .filter(mimeBodyPart => "attachment".equalsIgnoreCase(mimeBodyPart.getDisposition))
        .toList
      if (attachments.isEmpty) return None
      Some(Email(Option(message.getSubject), message.getReceivedDate, attachments))
    } catch {
      case e: MessagingException =>
        Console.err.println(s"Failed to access multipart content: ${e.getMessage}")
        None
      case e: IOException =>
        Console.err.println(s"Failed to access multipart content: ${e.getMessage}")
        None
    }
  }

  private def extractAttachment(directory_name: String, attachment: MimeBodyPart): Unit = {
    Using(attachment.getInputStream) { attachmentStream =>
      this.storage.storeFile(
        attachmentStream,
        attachment.getFileName,
        Some(directory_name))
    } match {
      case Failure(e) => Console.err.println(s"Failed to store file to $directory_name ${e.getClass}: ${e.getMessage}")
      case Success(_) =>
    }
  }

  private def flattenEmail(email: Email) = {
    email.attachments.map(attachment => {
      val dateReceived = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss").format(email.received)
      val attachmentDirectoryName = s"${dateReceived}_${email.subject.getOrElse("None")}"
      (attachmentDirectoryName, attachment)
    })
  }

  override def extract(): Option[ExtractorError] = {
    if (!this.isAuthenticated) {
      return Some(ExtractorAuthError)
    }
    val folder = this.store.getFolder("INBOX")
    folder.open(Folder.READ_ONLY)
    folder
      .getMessages
      .toList
      .par
      .take(20)
      .filter(containsMultipart)
      .flatMap(toEmail)
      .flatMap(flattenEmail)
      .foreach { case (directory_name, attachment) => extractAttachment(directory_name, attachment) }
    None
  }

  override def authenticate(): Option[ExtractorError] = {
    val properties = new Properties()
    properties.put("mail.imaps.host", "imap.gmail.com")
    properties.put("mail.imaps.ssl.trust", "imap.gmail.com")
    properties.put("mail.imaps.port", "993")
    properties.put("mail.imaps.starttls.enable", "true")
    properties.put("mail.imaps.connectiontimeout", "10000")
    properties.put("mail.imaps.timeout", "10000")
    val session = Session.getInstance(properties)
    try {
      this.store = session.getStore("imaps")
      this.store.connect("imap.gmail.com", this.emailAddress, this.userToken)
    } catch {
      case e: MessagingException => return Some(ExtractorConnectionError(e.getMessage))
    }
    this.isAuthenticated = true
    None
  }

  override def close(): Option[ExtractorError] = {
    try {
      this.store.close()
    } catch {
      case e: MessagingException => return Some(ExtractorConnectionCloseError(e.getMessage))
    }
    None
  }

}
