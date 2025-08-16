package com.example.liftoff.storage.web;

import com.example.liftoff.storage.Storage;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

public class OneDriveStorage implements Storage {
    private final Path rootDirectory;
    private final String accessToken;

    public OneDriveStorage(Path rootDirectory, String accessToken) {
        this.rootDirectory = rootDirectory;
        this.accessToken = accessToken;
    }

    @Override
    public void storeFile(InputStream file, String fileName, Optional<String> fileDirectoryName) throws Exception {
        // TODO implement OneDrive storage functionality
    }
}
