package org.sertia.server.communication;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.sertia.contracts.movies.catalog.controller.SertiaCatalog;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.LoginResult;
import org.sertia.server.bl.MoviesCatalogController;
import org.sertia.server.bl.UserLoginController;
import org.sertia.server.communication.messages.UpdateMovieScreeningTime;

import java.io.IOException;

public class MessageHandler extends AbstractServer {
    private static Gson GSON = new Gson();
    private final MoviesCatalogController moviesCatalogController;


    private UserLoginController userLoginController;
    private final String ClientRoleType = "Role";
    private final String ClientSessionIdType = "Session";

    public MessageHandler(int port) {
        super(port);
        userLoginController = new UserLoginController();

        LoginCredentials a = new LoginCredentials();
        a.username = "Admin";
        a.password = "123123";
        userLoginController.login(a);
        moviesCatalogController = new MoviesCatalogController();
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Received Message: " + msg.toString());
        JSONObject rawMessage = new JSONObject(msg.toString());

        switch (rawMessage.getString("messageName")) {
            case "ALL_MOVIES_REQ":
                handleAllMoviesRequest(client);
                break;
            case "UPDATE_SCREENING_REQ":
                handleMovieScreeningUpdate(msg.toString(), client);
                break;
            case "LOGIN_REQ":
                handleLoginRequest(msg.toString(), client);
                break;
            default:
                System.out.println("Got uknown message: " + msg);
        }
    }

    public void handleAllMoviesRequest(ConnectionToClient client) {
        try {
            SertiaCatalog requestResponse = moviesCatalogController.getSertiaCatalog();
            client.sendToClient(GSON.toJson(requestResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMovieScreeningUpdate(String msg, ConnectionToClient client) {
        UpdateMovieScreeningTime receivedScreening = GSON.fromJson(msg, UpdateMovieScreeningTime.class);
        try {
            moviesCatalogController.updateScreeningMovie(receivedScreening);
            client.sendToClient(GSON.toJson(true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLoginRequest(String msg, ConnectionToClient client) {
        LoginCredentials loginCredentials = GSON.fromJson(msg, LoginCredentials.class);

        try {
            LoginResult result = userLoginController.login(loginCredentials);

            // Saving the user's role and session ID
            client.setInfo(ClientRoleType, result.userRole);
            client.setInfo(ClientSessionIdType, result.sessionId);

            client.sendToClient(GSON.toJson(result));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        userLoginController.disconnect((Integer) client.getInfo(ClientSessionIdType));

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
