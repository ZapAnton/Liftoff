package com.example.liftoff.extractor.email;

import com.example.liftoff.extractor.Extractor;
import com.example.liftoff.storage.filesystem.FileStorage;

/**
 * A generic abstract extractor class, that holds
 * the typical email extraction methods.
 */
public abstract class EmailExtractor implements Extractor {
    protected boolean isAuthenticated = false;
    protected final String emailAddress;
    protected final String userToken;
    protected final FileStorage storage;

    protected EmailExtractor(final String emailAddress, final String userToken, FileStorage storage) {
        this.emailAddress = emailAddress;
        this.userToken = userToken;
        this.storage = storage;
    }

    public abstract void authenticate() throws Exception;

    public abstract void close() throws Exception;
}
