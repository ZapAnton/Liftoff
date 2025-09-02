package com.example.liftoff.extractor.email;

import com.example.liftoff.storage.Storage;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

public class GmailExtractor extends EmailExtractor {
    private Gmail service = null;

    public GmailExtractor(String emailAddress, String userToken, Storage storage) {
        super(emailAddress, userToken, storage);
    }

    @Override
    public void authenticate() throws Exception {
        // TODO implement
        // this.service = new Gmail.Builder().setApplicationName("").build();
    }


    private boolean containsPayload(final Message message) {
        // TODO implement
        return false;
    }


    @Override
    public void extract() {
        // TODO implement
        // final var messagesResponse = this.service.users().messages().list("me").execute();
        // messagesResponse.getMessages().parallelStream().filter(this::containsPayload);
    }


    @Override
    public void close() throws Exception {
        // TODO implement cleanup
    }
}
