package org.sertia.server.communication;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.sertia.server.communication.messages.AllMoviesRequestMsg;
import org.sertia.server.communication.messages.AllMoviesRequestResponse;
import org.sertia.server.controllers.MoviesController;

import java.io.IOException;

public class MessageHandler extends AbstractServer {

    private static Gson GSON = new Gson();

    public MessageHandler(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Received Message: " + msg.toString());
        JSONObject rawMessage = new JSONObject(msg.toString());

        switch (rawMessage.getString("messageName")) {
            case "ALL_MOVIES_REQ":
                handleAllMoviesRequest(msg.toString(), client);
                break;
            default:
                System.out.println("Got uknown message: " + msg);
        }
    }

    private void handleAllMoviesRequest(String msg, ConnectionToClient client) {
        AllMoviesRequestMsg receivedMessage = GSON.fromJson(msg, AllMoviesRequestMsg.class);
        try {
            AllMoviesRequestResponse requestResponse = new AllMoviesRequestResponse(receivedMessage,
                    MoviesController.getScreeningMovies());
            client.sendToClient(GSON.toJson(requestResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        // TODO Auto-generated method stub

        System.out.println("Client Disconnected.");
        super.clientDisconnected(client);
    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        System.out.println("Client connected: " + client.getInetAddress());
    }

    public void startListening() throws IOException {
        System.out.println("Starting to listen on port: " + getPort());
        listen();
    }
}