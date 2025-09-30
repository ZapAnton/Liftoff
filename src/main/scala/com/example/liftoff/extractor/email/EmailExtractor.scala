package com.example.liftoff.extractor.email

import com.example.liftoff.error.ExtractorError
import com.example.liftoff.extractor.Extractor
import com.example.liftoff.storage.Storage
import zio.IO

abstract class EmailExtractor(emailAddress: String, userToken: String, storage: Storage) extends Extractor {
  protected var isAuthenticated: Boolean = false

  def authenticate(): IO[ExtractorError, Unit]

  def close(): IO[ExtractorError, Unit]
}
