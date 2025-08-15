package com.example.liftoff.extractor.email;

import com.example.liftoff.extractor.Extractor;

/**
 * A generic abstract extractor class, that holds
 * the typical email extraction methods.
 */
abstract class EmailExtractor implements Extractor {
    private boolean isAuthenticated = false;

    abstract void authenticate();

    abstract void close();
}
