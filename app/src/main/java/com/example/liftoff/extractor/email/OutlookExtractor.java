package com.example.liftoff.extractor.email;

import com.example.liftoff.storage.filesystem.FileStorage;
import com.microsoft.graph.serviceclient.GraphServiceClient;

import java.util.Properties;

public class OutlookExtractor extends EmailExtractor{
    private GraphServiceClient client = null;

    public OutlookExtractor(String emailAddress, String userToken, FileStorage storage) {
        super(emailAddress, userToken, storage);
    }

    private void initializeGraph(final Properties oAuthProperties) {
        // TODO implement
        //this.client = new GraphServiceClient();
    }

    @Override
    public void authenticate() throws Exception {
        final Properties oAuthProperties = new Properties();
        initializeGraph(oAuthProperties);
    }

    @Override
    public void extract() {
        // TODO implement
        // GET /me/mailFolders/{id}/messages/{id}/attachments/{id}
        //this.client.me().mailFolders()
    }

    @Override
    public void close() throws Exception {
        // TODO impelment cleanup
    }
}
