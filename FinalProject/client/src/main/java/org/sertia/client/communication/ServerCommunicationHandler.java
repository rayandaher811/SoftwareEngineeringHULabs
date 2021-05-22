package org.sertia.client.communication;

import org.sertia.client.communication.messages.AllMoviesRequestMsg;
import org.sertia.client.communication.messages.AllMoviesRequestResponse;
import org.sertia.client.pojos.ScreeningMovie;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class ServerCommunicationHandler extends AbstractClient {
    private static final Logger LOGGER =
            Logger.getLogger(ServerCommunicationHandler.class.getName());

    private static ServerCommunicationHandler client;
    private final String clientId;

    private ServerCommunicationHandler(String host, int port) {
        super(host, port);
        clientId = UUID.randomUUID().toString();
    }

    public static boolean initializeClientServerConnection(String host, int port) {
        client = new ServerCommunicationHandler(host, port);
        try {
            client.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client.isConnected();
    }

    public static ServerCommunicationHandler getInstance() {
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

    public Collection<ScreeningMovie> getScreeningMovies() {

//        try {
        AllMoviesRequestMsg allMoviesRequestMsg = new AllMoviesRequestMsg(clientId);
//            client.sendToServer(new Gson().toJson(allMoviesRequestMsg));
//
//            String response = client.getResponse(allMoviesRequestMsg.getMessageId());
//
//            while (response == null) {
//                Thread.sleep(5);
//                response = client.getResponse(allMoviesRequestMsg.getMessageId());
//            }
//
//            AllMoviesRequestResponse requestResponse = new Gson().fromJson(response, AllMoviesRequestResponse.class);

        Optional<AllMoviesRequestResponse> res =
                client.requestAndWaitForResponse(allMoviesRequestMsg, allMoviesRequestMsg.getMessageId(), AllMoviesRequestResponse.class);
        if (res.isPresent())
            return res.get().getScreeningMovieCollection();
        else
            return Collections.emptyList();

//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return Collections.emptyList();
//    }
    }
}
