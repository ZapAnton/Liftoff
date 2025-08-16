package com.example.liftoff.command;

import com.example.liftoff.extractor.email.EmailExtractor;
import com.example.liftoff.extractor.email.GmailExtractor;
import com.example.liftoff.extractor.email.ImapGmailExtractor;
import com.example.liftoff.extractor.email.OutlookExtractor;
import com.example.liftoff.storage.Storage;
import com.example.liftoff.storage.filesystem.FileStorage;

import java.nio.file.Files;
import java.nio.file.Path;
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
    private final Optional<String> rootDirectory;

    private static String DEFAULT_DESTINATION_DIRECTORY = "email_attachments_downloads";

    public PullCommand(String emailAddress, String userToken, Optional<String> rootDirectory) {
        this.emailAddress = emailAddress;
        this.userToken = userToken;
        this.rootDirectory = rootDirectory;
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
        if (this.rootDirectory.isPresent() && !Files.exists(Paths.get(this.rootDirectory.get()))) {
            System.err.println("Provided destination path " + this.rootDirectory.get() + " for the downloaded attachments does not exist.\nPlease make sure that the provided path is valid and exists in the file system");
            isValid = false;
        }
        return isValid;
    }

    /**
     * Returns the specific ${@code EmailExtractor} implementation,
     * depending on the email adrress provided to the application
     */
    private EmailExtractor chooseSpecificEmailExtractor(final Storage fileStorage) {
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
     * Returns the specific ${@code Storage} implementation,
     * depending on the cli arguments provided
     */
    private Storage chooseStorage(final Path rootPath) {
        Storage storage;
        // TODO instead of returning the default FileStorage implementation, add logic to choose between file system or various web interfaces (GoogleDrive, Dropbox, etc.)
        storage = new FileStorage(rootPath.toAbsolutePath());
        return storage;
    }

    /**
     * Constructs an appropriate extractor object (eg. ${code ImapGmailExtractor}) and
     * a storage object (eg. ${code FileStorage}) for the successful email attachments download
     */
    @Override
    public void execute() {
        final var rootPath = this.rootDirectory
                .map(s -> Paths.get(s).toAbsolutePath())
                .orElseGet(() -> Paths.get("", DEFAULT_DESTINATION_DIRECTORY));
        System.out.println("Saving extracted email attachments to: " + rootPath.toAbsolutePath());
        final var storage = chooseStorage(rootPath);
        final var emailExtractor = this.chooseSpecificEmailExtractor(storage);
        try {
            emailExtractor.authenticate();
            emailExtractor.extract();
            emailExtractor.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
