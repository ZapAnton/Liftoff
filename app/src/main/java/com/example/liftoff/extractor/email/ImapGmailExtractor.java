package com.example.liftoff.extractor.email;

import com.example.liftoff.storage.Storage;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * Implements necessary logic to connect and download attachments from the Gmail address
 */
public class ImapGmailExtractor extends EmailExtractor {
    private Store store;

    public ImapGmailExtractor(String emailAddress, String userToken, Storage storage) {
        super(emailAddress, userToken, storage);
    }

    private boolean containsMultipartSafe(final Message message) {
        try {
            return message.getContentType().contains("multipart");
        } catch (MessagingException e) {
            System.err.println("Failed to access mail content: " + e.getMessage());
            return false;
        }
    }

    private Optional<Email> messageToEmailSafe(final Message message) {
        try {
            final var attachments = new ArrayList<MimeBodyPart>();
            final var multipartContent = (Multipart) message.getContent();
            for (int i = 0; i < multipartContent.getCount(); i++) {
                final var mimeBodyPart = (MimeBodyPart) multipartContent.getBodyPart(i);
                if (!MimeBodyPart.ATTACHMENT.equalsIgnoreCase(mimeBodyPart.getDisposition())) {
                    continue;
                }
                attachments.add(mimeBodyPart);
            }
            if (attachments.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new Email(Optional.ofNullable(message.getSubject()), message.getReceivedDate(), attachments));
        } catch (MessagingException | IOException e) {
            System.err.println("Failed to access mail content: " + e.getMessage());
            return Optional.empty();
        }

    }

    private void extractAttachmentSafe(final DirectoryAttachmentPair pair) {
        try (final var attachmentStream = pair.attachment().getInputStream()) {
            this.storage.storeFile(
                    attachmentStream,
                    pair.attachment().getFileName(),
                    Optional.of(pair.attachmentDirectoryName())
            );
        } catch (IOException e) {
            System.err.println("Failed to store file to " + pair.attachmentDirectoryName() + " " + e.getClass() + ": " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("Failed to access mail content: " + e.getMessage());
        }

    }

    private String buildAttachmentDirectoryName(final Date receivedDate, final Optional<String> subject) {
        final var receivedDateFormatted = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss").format(receivedDate);
        return receivedDateFormatted + "_" + subject.orElse("None");
    }

    private Stream<DirectoryAttachmentPair> flattenEmail(final Email email) {
        return email.attachments().stream().map(attachment -> {
            final var attachmentDirectoryName = buildAttachmentDirectoryName(email.received(), email.subject());
            return new DirectoryAttachmentPair(attachmentDirectoryName, attachment);
        });
    }

    @Override
    public void extract() {
        if (!this.isAuthenticated) {
            System.err.println("Cannot extract emails without authentication. Aborting");
            return;
        }
        try {
            final var folder = this.store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Arrays
                    .stream(folder.getMessages())
                    .limit(20) // For testing only
                    .parallel()
                    .filter(this::containsMultipartSafe)
                    .map(this::messageToEmailSafe)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .flatMap(this::flattenEmail)
                    .forEach(this::extractAttachmentSafe);
        } catch (MessagingException e) {
            System.err.println("Failed to access mail provider: " + e.getMessage());
        }
    }

    @Override
    public void authenticate() throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.ssl.trust", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.starttls.enable", "true");
        properties.put("mail.imaps.connectiontimeout", "10000");
        properties.put("mail.imaps.timeout", "10000");
        final var session = Session.getInstance(properties);
        this.store = session.getStore("imaps");
        this.store.connect("imap.gmail.com", this.emailAddress, this.userToken);
        this.isAuthenticated = true;
    }

    @Override
    public void close() throws MessagingException {
        this.store.close();
    }
}
