package com.example.liftoff.extractor.email.gmail.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class MessagePart(partId: String, mimeType: String, filename: String, headers: List[Header], body: MessagePartBody, parts: Option[List[MessagePart]])

object MessagePart {
  implicit val messagePartDecoder: Decoder[MessagePart] = deriveDecoder[MessagePart]
}
