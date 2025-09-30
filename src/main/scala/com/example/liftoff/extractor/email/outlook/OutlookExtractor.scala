package com.example.liftoff.extractor.email.outlook

import com.example.liftoff.error.ExtractorError
import com.example.liftoff.extractor.email.EmailExtractor
import com.example.liftoff.storage.Storage
import zio.IO

class OutlookExtractor(emailAddress: String, userToken: String, storage: Storage) extends EmailExtractor(emailAddress: String, userToken: String, storage: Storage) {

  override def authenticate(): IO[ExtractorError, Unit] = ???

  override def close(): IO[ExtractorError, Unit] = ???

  override def extract(): IO[ExtractorError, Unit] = ???
}
