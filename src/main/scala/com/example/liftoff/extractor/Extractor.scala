package com.example.liftoff.extractor

trait Extractor {
  def extract(): Option[ExtractorError]
}
