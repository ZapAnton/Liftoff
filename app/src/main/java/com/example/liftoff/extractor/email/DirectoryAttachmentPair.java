package com.example.liftoff.extractor.email;

import jakarta.mail.internet.MimeBodyPart;

import java.nio.file.Path;

/**
 * Utility class for the email message processing.
 * Contains a pair of an attachment and a directory path, specific to that attachment
 */
record DirectoryAttachmentPair(Path directoryPath, MimeBodyPart attachment) {
}
