package com.example.liftoff.extractor.email.gmail.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class Header(name: String, value: String)

object Header {
   implicit val headerDecoder: Decoder[Header] = deriveDecoder[Header]
}
