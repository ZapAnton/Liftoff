package com.example.liftoff.extractor.email

import com.example.liftoff.extractor.{Extractor, ExtractorError}
import com.example.liftoff.storage.Storage

abstract class EmailExtractor(emailAddress: String, userToken: String, storage: Storage) extends Extractor {
  protected var isAuthenticated: Boolean = false

  def authenticate(): Option[ExtractorError]

  def close(): Option[ExtractorError]
}
