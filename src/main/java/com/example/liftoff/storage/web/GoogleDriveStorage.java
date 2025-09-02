package com.example.liftoff.storage.web;

import com.example.liftoff.storage.Storage;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.drive.Drive;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

public class GoogleDriveStorage implements Storage {
    private final Path rootDirectory;
    private final String accessToken;

    public GoogleDriveStorage(Path rootDirectory, String accessToken) {
        this.rootDirectory = rootDirectory;
        this.accessToken = accessToken;
    }


    @Override
    public void storeFile(InputStream file, String fileName, Optional<String> fileDirectoryName) throws Exception {
        // TODO implement Google Drive storage functionality
        // new Drive.Builder(GoogleNetHttpTransport()).setApplicationName("test").build().files().create(file).execute();
    }
}
