package com.example.liftoff.extractor.email;

import jakarta.mail.internet.MimeBodyPart;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Contains information about a single email message
 * @param subject - Email Subject, could be missing
 * @param received - A date when the email message was received
 * @param attachments - Any possible attachments that the email message contains
 */
public record Email(Optional<String> subject, Date received, List<MimeBodyPart> attachments) {
}
