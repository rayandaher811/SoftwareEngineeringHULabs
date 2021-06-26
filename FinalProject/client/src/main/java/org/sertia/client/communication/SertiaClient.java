package org.sertia.client.communication;

import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.client.communication.messages.UpdateMovieScreeningTime;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;
import org.sertia.contracts.movies.catalog.request.SertiaCatalogRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class SertiaClient extends AbstractClient {
    private static final Logger LOGGER =
            Logger.getLogger(SertiaClient.class.getName());

    private static SertiaClient client;
    private final String clientId;

    private SertiaClient(String host, int port) {
        super(host, port);
        clientId = UUID.randomUUID().toString();
    }

    public static boolean initializeClientServerConnection(String host, int port) {
        client = new SertiaClient(host, port);
        try {
            client.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client.isConnected();
    }

    public static SertiaClient getInstance() {
        if (client == null)
            LOGGER.severe("Connection to server never established or corrupted, restart your app");
        return client;
    }

    @Override
    protected void connectionEstablished() {
        // TODO Auto-generated method stub
        super.connectionEstablished();
        LOGGER.info("Connected to server.");
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        LOGGER.info("Got response from server: {}" + msg);
    }

    @Override
    protected void connectionClosed() {
        // TODO Auto-generated method stub
        super.connectionClosed();
    }

    public MoviesCatalog getMoviesCatalog() {
        SertiaCatalogRequest sertiaCatalogRequest = new SertiaCatalogRequest();
        Optional<SertiaCatalogResponse> res =
                client.requestAndWaitForResponse(sertiaCatalogRequest, SertiaCatalogResponse.class);

        if (res.isPresent())
            return new MoviesCatalog();
        else
            return null;
    }

    public void requestMovieScreeningTimeChange(UpdateMovieScreeningTime updateMovieScreeningTimeMsg) {
        client.publishToServer(updateMovieScreeningTimeMsg);
    }
}