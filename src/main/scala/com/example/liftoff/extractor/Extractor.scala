package com.example.liftoff.extractor

import com.example.liftoff.error.ExtractorError

trait Extractor {
  def extract(): Option[ExtractorError]
}
