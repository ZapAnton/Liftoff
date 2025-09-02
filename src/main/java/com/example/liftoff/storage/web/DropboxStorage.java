package com.example.liftoff.storage.web;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.example.liftoff.storage.Storage;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

public class DropboxStorage implements Storage {
    private final Path rootDirectory;
    private final String accessToken;

    public DropboxStorage(Path rootDirectory, String accessToken) {
        this.rootDirectory = rootDirectory;
        this.accessToken = accessToken;
    }

    @Override
    public void storeFile(InputStream file, String fileName, Optional<String> fileDirectoryName) throws Exception {
        // TODO implement and test Dropbox storage
        final var config = DbxRequestConfig.newBuilder("test").build();
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        client.files().uploadBuilder(this.rootDirectory.toString()).withMode(WriteMode.OVERWRITE).uploadAndFinish(file);
    }

}
