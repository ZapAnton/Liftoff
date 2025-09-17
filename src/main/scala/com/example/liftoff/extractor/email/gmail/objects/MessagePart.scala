package com.example.liftoff.extractor.email.gmail.objects

case class MessagePart(partId: String, mimeType: String, filename: String, headers: List[Header], body: MessagePartBody, parts: Option[List[MessagePart]])
