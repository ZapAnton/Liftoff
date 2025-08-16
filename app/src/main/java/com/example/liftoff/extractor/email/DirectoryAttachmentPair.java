package com.example.liftoff.extractor.email;

import jakarta.mail.internet.MimeBodyPart;

/**
 * Utility class for the email message processing.
 * Contains a pair of an attachment and a directory path, specific to that attachment
 */
record DirectoryAttachmentPair(String attachmentDirectoryName, MimeBodyPart attachment) {
}
