package org.sertia.server.communication;

import org.sertia.contracts.SertiaClientRequest;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaCatalog;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.request.*;
import org.sertia.contracts.price.change.request.ApprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.ClientPriceChangeRequest;
import org.sertia.contracts.price.change.request.DissapprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.GetUnapprovedPriceChangeRequests;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.UserRole;
import org.sertia.contracts.user.login.request.LoginRequest;
import org.sertia.contracts.user.login.response.LoginResult;
import org.sertia.server.bl.MoviesCatalogController;
import org.sertia.server.bl.PriceChangeController;
import org.sertia.server.bl.ScreeningTicketController;
import org.sertia.server.bl.UserLoginController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MessageHandler extends AbstractServer {
    private final String ClientRoleType = "Role";
    private final String ClientSessionIdType = "Session";
    private final String ClientUsernameType = "Username";

    private final MoviesCatalogController moviesCatalogController;
    private final ScreeningTicketController screeningTicketController;
    private final PriceChangeController priceChangeController;
    private final UserLoginController userLoginController;
    private final Map<Class<? extends SertiaClientRequest>, BiConsumer<SertiaClientRequest, ConnectionToClient>> messageTypeToHandler;

    private final RoleValidator roleValidator;

    public MessageHandler(int port, ScreeningTicketController screeningTicketController) {
        super(port);
        this.roleValidator = new RoleValidator();
        this.messageTypeToHandler = new HashMap<>();
        initializeHandlerMapping();

        this.screeningTicketController = screeningTicketController;
        this.userLoginController = new UserLoginController();
        this.priceChangeController = new PriceChangeController();

        LoginCredentials a = new LoginCredentials();
        a.username = "Admin";
        a.password = "123123";
        userLoginController.login(a);
        moviesCatalogController = new MoviesCatalogController();
    }

    private void initializeHandlerMapping() {
        messageTypeToHandler.put(SertiaCatalogRequest.class, this::handleSertiaCatalog);
        messageTypeToHandler.put(ScreeningUpdateRequest.class, this::handleMovieScreeningTimeUpdate);
        messageTypeToHandler.put(LoginRequest.class, this::handleLoginRequest);
        messageTypeToHandler.put(AddMovieRequest.class, this::handleMovieAddition);
        messageTypeToHandler.put(RemoveMovieRequest.class, this::handleMovieRemoval);
        messageTypeToHandler.put(AddScreeningRequest.class, this::handleScreeningAddition);
        messageTypeToHandler.put(RemoveScreeningRequest.class, this::handleScreeningRemoval);
        messageTypeToHandler.put(StreamingAdditionRequest.class, this::handleStreamingAddition);
        messageTypeToHandler.put(StreamingRemovalRequest.class, this::handleStreamingRemoval);
        messageTypeToHandler.put(ClientPriceChangeRequest.class, this::handlePriceChangeRequest);
        messageTypeToHandler.put(ApprovePriceChangeRequest.class, this::handleApprovePriceChangeRequest);
        messageTypeToHandler.put(DissapprovePriceChangeRequest.class, this::handleDisapprovePriceChangeRequest);
        messageTypeToHandler.put(GetUnapprovedPriceChangeRequests.class, this::handleAllUnapprovedPriceChangeRequests);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Received Message: " + msg.toString());

        Class<?> requestType = msg.getClass();
        if (!messageTypeToHandler.containsKey(requestType)) {
            System.out.println("user " + client.getName() + " requested non-existing action " + msg.getClass());
            return;
        }

        if (!roleValidator.isClientAllowed((UserRole) client.getInfo(ClientRoleType), msg.getClass())) {
            System.out.println("user " + client.getName() + " denied request of type " + msg.getClass());
            return;
        }

        messageTypeToHandler.get(requestType).accept((SertiaClientRequest) msg, client);
    }

    private void handleSertiaCatalog(SertiaClientRequest request, ConnectionToClient client) {
        try {
            SertiaCatalog sertiaCatalog = moviesCatalogController.getSertiaCatalog();
            client.sendToClient(sertiaCatalog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAllUnapprovedPriceChangeRequests(SertiaClientRequest request, ConnectionToClient client) {
        try {
            client.sendToClient(priceChangeController.getUnapprovedRequests());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDisapprovePriceChangeRequest(SertiaClientRequest request, ConnectionToClient client) {
        try {
            int priceChangeRequestId = ((DissapprovePriceChangeRequest) request).priceChangeRequestId;
            priceChangeController.disapprovePriceChangeRequest(priceChangeRequestId, (String) client.getInfo(ClientUsernameType));
            client.sendToClient(Boolean.TRUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleApprovePriceChangeRequest(SertiaClientRequest request, ConnectionToClient client) {
        try {
            int priceChangeRequestId = ((ApprovePriceChangeRequest) request).priceChangeRequestId;
            priceChangeController.approveRequest(priceChangeRequestId, (String) client.getInfo(ClientUsernameType));
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePriceChangeRequest(SertiaClientRequest request, ConnectionToClient client) {
        ClientPriceChangeRequest priceChangeRequest = (ClientPriceChangeRequest) request;
        try {
            priceChangeController.requestPriceChange(priceChangeRequest, (String) client.getInfo(ClientUsernameType));
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMovieRemoval(SertiaClientRequest request, ConnectionToClient client) {
        try {
            int movieId = ((RemoveMovieRequest) request).movieId;
            moviesCatalogController.removeMovie(movieId);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleScreeningRemoval(SertiaClientRequest request, ConnectionToClient client) {
        try {
            int screeningId = ((RemoveScreeningRequest) request).screeningId;
            moviesCatalogController.removeMovieScreening(screeningId);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMovieAddition(SertiaClientRequest request, ConnectionToClient client) {
        SertiaMovie newMovie = ((AddMovieRequest) request).sertiaMovie;
        try {
            moviesCatalogController.addMovie(newMovie);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleScreeningAddition(SertiaClientRequest request, ConnectionToClient client) {
        CinemaScreeningMovie movieScreenings = ((AddScreeningRequest) request).cinemaScreeningMovie;
        try {
            moviesCatalogController.addMovieScreenings(movieScreenings);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleStreamingAddition(SertiaClientRequest request, ConnectionToClient client) {
        StreamingAdditionRequest streamingAdditionRequest = (StreamingAdditionRequest) request;
        try {
            moviesCatalogController.addStreaming(streamingAdditionRequest.movieId, streamingAdditionRequest.pricePerStream);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleStreamingRemoval(SertiaClientRequest request, ConnectionToClient client) {
        try {
            int streamingId = ((StreamingRemovalRequest) request).streamingId;
            moviesCatalogController.removeStreaming(streamingId);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMovieScreeningTimeUpdate(SertiaClientRequest request, ConnectionToClient client) {
        ClientScreening screeningToUpdate = ((ScreeningUpdateRequest) request).screening;
        try {
            moviesCatalogController.updateScreeningTime(screeningToUpdate);
            client.sendToClient(Boolean.TRUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLoginRequest(SertiaClientRequest request, ConnectionToClient client) {
        LoginCredentials loginCredentials = ((LoginRequest) request).loginCredentials;

        try {
            LoginResult result = userLoginController.login(loginCredentials);

            // Saving the user's role and session ID
            client.setInfo(ClientRoleType, result.userRole);
            client.setInfo(ClientSessionIdType, result.sessionId);

            // Saving the username if the client has special role
            if (result.userRole != UserRole.None)
                client.setInfo(ClientUsernameType, loginCredentials.username);

            client.sendToClient(result);
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
