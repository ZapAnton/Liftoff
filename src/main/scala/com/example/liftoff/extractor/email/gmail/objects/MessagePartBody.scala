package com.example.liftoff.extractor.email.gmail.objects

case class MessagePartBody(attachmentId: Option[String], size: Int, data: Option[String])
