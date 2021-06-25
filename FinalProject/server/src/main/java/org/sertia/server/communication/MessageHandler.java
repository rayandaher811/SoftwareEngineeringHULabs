package org.sertia.server.communication;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.sertia.contracts.movies.catalog.controller.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.controller.SertiaCatalog;
import org.sertia.contracts.movies.catalog.controller.SertiaMovie;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.LoginResult;
import org.sertia.contracts.user.login.UserRole;
import org.sertia.server.bl.MoviesCatalogController;
import org.sertia.server.bl.ScreeningTicketController;
import org.sertia.server.bl.UserLoginController;

import java.io.IOException;

public class MessageHandler extends AbstractServer {
    private static Gson GSON = new Gson();
    private final MoviesCatalogController moviesCatalogController;
    private final ScreeningTicketController screeningTicketController;


    private UserLoginController userLoginController;
    private final String ClientRoleType = "Role";
    private final String ClientSessionIdType = "Session";
    private RoleValidator roleValidator;

    public MessageHandler(int port, ScreeningTicketController screeningTicketController) {
        super(port);
        this.screeningTicketController = screeningTicketController;
        userLoginController = new UserLoginController();
        roleValidator = new RoleValidator();

        LoginCredentials a = new LoginCredentials();
        a.username = "Admin";
        a.password = "123123";
        userLoginController.login(a);
        moviesCatalogController = new MoviesCatalogController();
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Received Message: " + msg.toString());
        String requestType = new JSONObject(msg.toString()).getString("messageName");

        // Validating the user requests
        if(!roleValidator.isClientAllowed((UserRole)client.getInfo(ClientRoleType),
                                            requestType))
            return;

        switch (requestType) {
            case RequestType.ALL_MOVIES_REQ:
                handleAllMoviesRequest(client);
                break;
            case RequestType.UPDATE_SCREENING_REQ:
                handleMovieScreeningUpdate(msg.toString(),client);
                break;
            case RequestType.LOGIN_REQ:
                handleLoginRequest(msg.toString(),client);
                break;
            case RequestType.ADD_MOVIE:
                handleMovieAddition(msg.toString(),client);
                break;
            case RequestType.REMOVE_MOVIE:
                handleMovieRemoval(msg.toString(),client);
                break;
            case RequestType.ADD_SCREENINGS:
                handleScreeningAddition(msg.toString(),client);
                break;
            case RequestType.REMOVE_SCREENINGS:
                handleScreeningRemoval(msg.toString(),client);
                break;
            default:
                System.out.println("Got uknown message: " + msg);
        }
    }


    private void handleMovieRemoval(String msg, ConnectionToClient client) {
        try {
            int movieId = Integer.parseInt(msg);
            moviesCatalogController.removeMovie(movieId);
            client.sendToClient(GSON.toJson(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleScreeningRemoval(String msg, ConnectionToClient client) {
        try {
            int screeningId = Integer.parseInt(msg);
            moviesCatalogController.removeMovieScreening(screeningId);
            client.sendToClient(GSON.toJson(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMovieAddition(String msg, ConnectionToClient client) {
        SertiaMovie newMovie = GSON.fromJson(msg, SertiaMovie.class);
        try {
            moviesCatalogController.addMovie(newMovie);
            client.sendToClient(GSON.toJson(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleScreeningAddition(String msg, ConnectionToClient client) {
        CinemaScreeningMovie movieScreenings = GSON.fromJson(msg, CinemaScreeningMovie.class);
        try {
            moviesCatalogController.addMovieScreenings(movieScreenings);
            client.sendToClient(GSON.toJson(true));
        } catch (Exception e) {
            e.printStackTrace();
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
//        UpdateMovieScreeningTime receivedScreening = GSON.fromJson(msg, UpdateMovieScreeningTime.class);
//        try {
//            moviesCatalogController.updateScreeningMovie(receivedScreening);
//            client.sendToClient(GSON.toJson(true));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
