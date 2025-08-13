package com.example.liftoff.storage.file;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Implements logic to save attachments to the file system.
 * TODO: Decouple the saving logic from the ${@code EmailExtractor} class
 */
public class FileStorage {

    private final Path destinationDirectory;

    public FileStorage(final Path destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public Path buildEmailFilePath(final Date receivedDate, final Optional<String> subject) {
        final var receivedDateFormatted = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss").format(receivedDate);
        final var attachmentDirectoryName = receivedDateFormatted + "_" + subject.orElse("None");
        return Paths.get(this.destinationDirectory.toString(), attachmentDirectoryName);
    }

    public void createDirectory(Path path) throws IOException {
        Files.createDirectories(path);
    }
}
