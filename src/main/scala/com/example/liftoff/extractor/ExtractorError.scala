package com.example.liftoff.extractor

sealed trait ExtractorError {
  def message: String
}

object ExtractorAuthError extends ExtractorError {
  override def message: String = "Unable to perform extraction without authorization. Aborting"
}

sealed case class ExtractorConnectionError(errorText: String) extends ExtractorError {
  override def message: String = s"Failed to connect to the email provider: $errorText"
}

sealed case class ExtractorConnectionCloseError(errorText: String) extends ExtractorError {
  override def message: String = s"Failed to close connection to the email provider: $errorText"
}