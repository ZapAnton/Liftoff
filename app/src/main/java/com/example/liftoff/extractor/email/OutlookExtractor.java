package com.example.liftoff.extractor.email;

import com.example.liftoff.storage.file.FileStorage;

public class OutlookExtractor extends EmailExtractor{
    public OutlookExtractor(String emailAddress, String userToken, FileStorage storage) {
        super(emailAddress, userToken, storage);
    }

    @Override
    public void authenticate() throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void extract() {

    }
}
