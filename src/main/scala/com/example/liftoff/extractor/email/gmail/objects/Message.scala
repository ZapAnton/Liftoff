package com.example.liftoff.extractor.email.gmail.objects

case class Message(id: String, threadId: String, payload: Option[MessagePart])

