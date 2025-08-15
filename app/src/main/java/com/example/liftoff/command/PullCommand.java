package com.example.liftoff.command;

import com.example.liftoff.extractor.email.EmailExtractor;
import com.example.liftoff.extractor.email.GmailExtractor;
import com.example.liftoff.extractor.email.ImapGmailExtractor;
import com.example.liftoff.extractor.email.OutlookExtractor;
import com.example.liftoff.storage.file.FileStorage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Implements a ${@code Command} for the email attachments download
 */
class PullCommand implements Command {
    /**
     * An address from which the attachments will be downloaded
     */
    private final String emailAddress;
    /**
     * A token needed to access the email service API (Gmail, Graph, etc.).
     * TODO: Move this to the properties file
     */
    private final String userToken;
    /**
     * An optional directory path for the downloaded attachments
     */
    private final Optional<String> destinationDirectory;

    private static String DEFAULT_DESTINATION_DIRECTORY = "email_attachments_downloads";

    public PullCommand(String emailAddress, String userToken, Optional<String> destinationDirectory) {
        this.emailAddress = emailAddress;
        this.userToken = userToken;
        this.destinationDirectory = destinationDirectory;
    }

    /**
     * Check if the provided {@code destinationDirectory} exists in the file system.
     *
     * @return ${@code true} if {@code destinationDirectory} exists, {@code false} otherwise
     * TODO: Check for the {@code emailAddress} validity
     */
    @Override
    public boolean isValid() {
        var isValid = true;
        if (this.destinationDirectory.isPresent() && !Files.exists(Paths.get(this.destinationDirectory.get()))) {
            System.err.println("Provided destination path " + this.destinationDirectory.get() + " for the downloaded attachments does not exist.\nPlease make sure that the provided path is valid and exists in the file system");
            isValid = false;
        }
        return isValid;
    }

    /**
     * Returns the specific ${@code EmailExtractor} implementation,
     * depending on the email adrress provided to the application
     */
    private EmailExtractor chooseSpecificEmailExtractor(final FileStorage fileStorage) {
        EmailExtractor extractor;
        if (this.emailAddress.endsWith("@gmail.com")) {
            extractor = new GmailExtractor(this.emailAddress, this.userToken, fileStorage);
        } else if (emailAddress.endsWith("@outlook.com")) {
            extractor = new OutlookExtractor(this.emailAddress, this.userToken, fileStorage);
        }
        // TODO: Since only ImapGmailAPI is implemented, fallback to it as a default. Remove after implementing GmailExtractor and OutlookExtractor
        extractor = new ImapGmailExtractor(this.emailAddress, this.userToken, fileStorage);
        return extractor;
    }

    /**
     * Constructs an appropriate extractor object (eg. ${code ImapGmailExtractor}) and
     * a storage object (eg. ${code FileStorage}) for the successful email attachments download
     */
    @Override
    public void execute() {
        final var pathForStorage = this.destinationDirectory
                .map(s -> Paths.get(s).toAbsolutePath())
                .orElseGet(() -> Paths.get("", DEFAULT_DESTINATION_DIRECTORY));
        System.out.println("Saving extracted email attachments to: " + pathForStorage.toAbsolutePath());
        final var fileStorage = new FileStorage(pathForStorage.toAbsolutePath());

        final var emailExtractor = this.chooseSpecificEmailExtractor(fileStorage);
        try {
            emailExtractor.authenticate();
            emailExtractor.extract();
            emailExtractor.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
