package com.example.liftoff.extractor.email.gmail.objects

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class MessageList(messages: List[Message])

object MessageList {
  implicit val messageListDecoder: Decoder[MessageList] = deriveDecoder[MessageList]
}