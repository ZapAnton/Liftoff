package com.example.liftoff.extractor.email

import com.example.liftoff.extractor.ExtractorError
import com.example.liftoff.storage.Storage

class GmailExtractor(emailAddress: String, userToken: String, storage: Storage) extends EmailExtractor(emailAddress: String, userToken: String, storage: Storage) {

  override def authenticate(): Option[ExtractorError] = ???

  override def close(): Option[ExtractorError] = ???

  override def extract(): Option[ExtractorError] = ???
}
