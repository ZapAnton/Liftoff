package com.example.liftoff.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * The ${@code Storage} interface holds the description
 * of the methods necessary for the every type of data storage
 * implementation, such as file system, database, remote storage API (Dropbox, etc.)
 */
public interface Storage {
    /**
     * Store a single file in the specific storage, chosen by the
     * interface implementation
     * @param file - file input stream to save in storage
     * @param fileName - the name of the file, which will stored in storage
     * @param fileDirectoryName - the name of the directory in the storage, where the file
     *                          will be stored (if the storage supports directories)
     */
    void storeFile(final InputStream file, final String fileName, final Optional<String> fileDirectoryName) throws IOException;
}