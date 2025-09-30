package com.example.liftoff.extractor

import com.example.liftoff.error.ExtractorError
import zio.IO

trait Extractor {
  def extract(): IO[ExtractorError, Unit]
}
