package com.example.liftoff.storage.filesystem;


import com.example.liftoff.storage.Storage;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;

/**
 * Implements logic to save attachments to the file system.
 */
public class FileStorage implements Storage {

    private final Path rootDirectory;

    public FileStorage(final Path rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public void storeFile(InputStream file, String fileName, Optional<String> fileDirectoryName) throws Exception {
        final var directoryPath = Paths.get(this.rootDirectory.toString(), fileDirectoryName.orElse(""));
        if (fileDirectoryName.isPresent() && !Files.exists(directoryPath)) {
            try {
                Files.createDirectory(directoryPath);
            } catch (FileAlreadyExistsException ignored) {
            }
        }
        Files.copy(
                file,
                Paths.get(directoryPath.toString(), fileName),
                StandardCopyOption.REPLACE_EXISTING
        );
    }
}
