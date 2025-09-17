package com.example.liftoff.extractor.email.imap

import jakarta.mail.internet.MimeBodyPart

import java.util.Date

case class Email(subject: Option[String], received: Date, attachments: List[MimeBodyPart])
