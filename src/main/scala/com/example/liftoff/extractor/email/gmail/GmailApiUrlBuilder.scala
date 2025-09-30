package com.example.liftoff.extractor.email.gmail

case class GmailApiUrlBuilder(user: String = "", accessToken: String = "", messageId: String = "", attachmentId: String = "", query: String = "", maxResult: Int = 100) {
  private val gmailApiPrefix = "https://gmail.googleapis.com/gmail/v1/users"

  def user(user: String): GmailApiUrlBuilder = {
    this.copy(user = user)
  }

  def accessToken(accessToken: String): GmailApiUrlBuilder = {
    this.copy(accessToken = accessToken)
  }

  def query(query: String): GmailApiUrlBuilder = {
    this.copy(query = query)
  }

  def maxResult(maxResult: Int): GmailApiUrlBuilder = {
    this.copy(maxResult = maxResult)
  }

  def messageId(messageId: String): GmailApiUrlBuilder = {
    this.copy(messageId = s"/messages/$messageId")
  }

  def messageList: GmailApiUrlBuilder = {
    this.copy(messageId = s"/messages")
  }

  def attachmentId(attachmentId: String): GmailApiUrlBuilder = {
    this.copy(attachmentId = s"/attachments/$attachmentId")
  }

  def build: String = {
    val queryParameters = List(
      s"access_token=${this.accessToken}",
      s"q=${this.query}",
      s"maxResults=${this.maxResult}",
    ).mkString("&")
    s"${this.gmailApiPrefix}/${this.user}${this.messageId}${this.attachmentId}?$queryParameters"
  }
}
