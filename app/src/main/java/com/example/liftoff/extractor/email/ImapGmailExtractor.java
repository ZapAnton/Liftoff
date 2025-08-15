package com.example.liftoff.extractor.email;

import com.example.liftoff.storage.file.FileStorage;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Implements necessary logic to connect and download attachments from the Gmail address
 */
public class ImapGmailExtractor extends EmailExtractor {
    private Store store;

    public ImapGmailExtractor(String emailAddress, String userToken, FileStorage storage) {
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
        try {
            this.storage.createDirectory(pair.directoryPath());
            final var attachmentPath = Paths.get(pair.directoryPath().toString(), pair.attachment().getFileName());
            pair.attachment().saveFile(attachmentPath.toString());
        } catch (IOException e) {
            System.err.println("Failed to create directory " + pair.directoryPath() + " " + e.getClass() + ": " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("Failed to access mail content: " + e.getMessage());
        }

    }

    private Stream<DirectoryAttachmentPair> flattenEmail(final Email email) {
        return email.attachments().stream().map(attachment -> {
            final var directoryPath = this.storage.buildEmailFilePath(email.received(), email.subject());
            return new DirectoryAttachmentPair(directoryPath, attachment);
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
