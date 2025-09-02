package com.example.liftoff.extractor;

/**
 * The ${@code Extractor} interface holds the description
 * of the methods necessary for the every type of data extractors,
 * e.g. web service, email, DB, FTP etc.
 */
public interface Extractor {
    /**
     * This method should hold the main data extraction logic
     */
    void extract();
}