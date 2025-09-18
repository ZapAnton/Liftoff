package com.example.liftoff.extractor.email.gmail.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class Message(id: String, threadId: String, payload: Option[MessagePart])

object Message {
  implicit val messageDecoder: Decoder[Message] = deriveDecoder[Message]
}
