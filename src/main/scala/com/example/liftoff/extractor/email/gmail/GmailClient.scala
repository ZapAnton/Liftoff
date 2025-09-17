package com.example.liftoff.extractor.email.gmail

import com.example.liftoff.extractor.email.gmail.objects._
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleClientSecrets}
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.{Gmail, GmailScopes}
import io.circe
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import sttp.client3.circe.asJson
import sttp.client3.{ResponseException, SimpleHttpClient, UriContext, basicRequest}

import java.io.{File, InputStreamReader}
import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters._


// TODO Cred Path parameter
class GmailClient {
  private val jsonFactory = GsonFactory.getDefaultInstance
  private var accessToken: Option[String] = None
  private val httpClient = SimpleHttpClient()

  private implicit val headerDecoder: Decoder[Header] = deriveDecoder[Header]
  private implicit val messagePartBodyDecoder: Decoder[MessagePartBody] = deriveDecoder[MessagePartBody]
  private implicit lazy val messagePartDecoder: Decoder[MessagePart] = deriveDecoder[MessagePart]
  private implicit val messageDecoder: Decoder[Message] = deriveDecoder[Message]
  private implicit val responseDecoder: Decoder[MessageList] = deriveDecoder[MessageList]

  private def getCredentials(transport: NetHttpTransport, credentialsPath: String): Credential = {
    val scopes = List(GmailScopes.GMAIL_READONLY)
    val credentialsInputStream = Files.newInputStream(Paths.get(credentialsPath))
    if (credentialsInputStream == null) {
      Console.err.println(s"Failed to read credential file $credentialsPath")
      return null
    }
    val clientSecrets = GoogleClientSecrets.load(this.jsonFactory, new InputStreamReader(credentialsInputStream))
    val flow = new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory, clientSecrets, scopes.toBuffer.asJava)
      .setDataStoreFactory(new FileDataStoreFactory(new File("data")))
      .setAccessType("offline")
      .build
    val receiver = new LocalServerReceiver.Builder()
      .setHost("localhost")
      .setPort(9000)
      .setCallbackPath("/authenticate/google")
      .build()
    val app = new AuthorizationCodeInstalledApp(flow, receiver)
    app.authorize("user")
  }

  def isAuthorized: Boolean = this.accessToken.isDefined

  def authenticate(credentialsPath: String): Unit = {
    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val applicationName = "Gmail API Java quickstart"
    val credentials: Credential = getCredentials(httpTransport, credentialsPath)
    new Gmail.Builder(httpTransport, this.jsonFactory, credentials).setApplicationName(applicationName).build()
    this.accessToken = Some(credentials.getAccessToken)
  }

  def getMessageList(user: String): Either[ResponseException[String, circe.Error], MessageList] = {
    val messageListUriString = GmailApiUrlBuilder()
      .user(user)
      .accessToken(this.accessToken.get)
      .messageList
      .query("has:attachment")
      .maxResult(20)
      .build
    val messageListUri = uri"$messageListUriString"
    val messageListRequest = basicRequest
      .get(messageListUri)
      .response(asJson[MessageList])
    this.httpClient.send(messageListRequest).body
  }

  def getMessage(user: String, messageId: String): Either[ResponseException[String, circe.Error], Message] = {
    val getMessageUriString = GmailApiUrlBuilder()
      .user(user)
      .accessToken(this.accessToken.get)
      .messageId(messageId)
      .build
    val getMessageUri = uri"$getMessageUriString"
    val getMessageRequest = basicRequest
      .get(getMessageUri)
      .response(asJson[Message])
    this.httpClient.send(getMessageRequest).body
  }

  def getAttachment(user: String, messageId: String, attachmentId: String): Either[ResponseException[String, circe.Error], MessagePartBody] = {
    val getAttachmentUriString = GmailApiUrlBuilder()
      .user(user)
      .accessToken(this.accessToken.get)
      .messageId(messageId)
      .attachmentId(attachmentId)
      .build
    val getAttachmentUri = uri"$getAttachmentUriString"
    val getAttachmentRequest = basicRequest
      .get(getAttachmentUri)
      .response(asJson[MessagePartBody])
    this.httpClient.send(getAttachmentRequest).body
  }
}
