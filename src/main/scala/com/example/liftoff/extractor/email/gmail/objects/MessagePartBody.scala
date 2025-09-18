package com.example.liftoff.extractor.email.gmail.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class MessagePartBody(attachmentId: Option[String], size: Int, data: Option[String])

object MessagePartBody {
  implicit val messagePartBodyDecoder: Decoder[MessagePartBody] = deriveDecoder[MessagePartBody]
}