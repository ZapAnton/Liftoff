package com.example.liftoff.storage;

import java.io.IOException;
import java.io.InputStream;

/**
 * The ${@code Storage} interface holds the description
 * of the methods necessary for the every type of data storage
 * implementation, such as file system, database, remote storage API (Dropbox, etc.)
 */
interface Storage {
    /**
     * Store a single file in the specific storage, chosen by the
     * interface implementation
     * @param file - file input stream to save in storage
     */
    void storeFile(final InputStream file) throws IOException;
}