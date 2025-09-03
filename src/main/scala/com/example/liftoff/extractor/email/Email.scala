package com.example.liftoff.extractor.email

import jakarta.mail.internet.MimeBodyPart

import java.util.Date

case class Email(subject: Option[String], received: Date, attachments: List[MimeBodyPart])
