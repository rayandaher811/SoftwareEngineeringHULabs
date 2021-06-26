package org.sertia.server.communication;

import org.sertia.contracts.SertiaBasicRequest;
import org.sertia.contracts.complaints.requests.CloseComplaintRequest;
import org.sertia.contracts.complaints.requests.CreateNewComplaintRequest;
import org.sertia.contracts.complaints.requests.GetAllUnhandledComplaintsRequest;
import org.sertia.contracts.complaints.requests.PurchaseCancellationFromComplaintRequest;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.request.*;
import org.sertia.contracts.price.change.request.ApprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.contracts.price.change.request.DissapprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.GetUnapprovedPriceChangeRequest;
import org.sertia.contracts.screening.ticket.request.*;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.UserRole;
import org.sertia.contracts.user.login.request.LoginRequest;
import org.sertia.contracts.user.login.response.LoginResult;
import org.sertia.server.bl.*;

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
    private final ComplaintsController complaintsController;

    private final Map<Class<? extends SertiaBasicRequest>, BiConsumer<SertiaBasicRequest, ConnectionToClient>> messageTypeToHandler;

    private final RoleValidator roleValidator;

    public MessageHandler(int port, ScreeningTicketController screeningTicketController) {
        super(port);
        this.roleValidator = new RoleValidator();
        this.messageTypeToHandler = new HashMap<>();
        initializeHandlerMapping();

        this.screeningTicketController = screeningTicketController;
        this.userLoginController = new UserLoginController();
        this.priceChangeController = new PriceChangeController();
        this.complaintsController = new ComplaintsController();
        this.moviesCatalogController = new MoviesCatalogController();
    }

    private void initializeHandlerMapping() {
        messageTypeToHandler.put(LoginRequest.class, this::handleLoginRequest);

        messageTypeToHandler.put(SertiaCatalogRequest.class, this::handleSertiaCatalog);
        messageTypeToHandler.put(ScreeningUpdateRequest.class, this::handleMovieScreeningTimeUpdate);
        messageTypeToHandler.put(AddMovieRequest.class, this::handleMovieAddition);
        messageTypeToHandler.put(RemoveMovieRequest.class, this::handleMovieRemoval);
        messageTypeToHandler.put(AddScreeningRequest.class, this::handleScreeningAddition);
        messageTypeToHandler.put(RemoveScreeningRequest.class, this::handleScreeningRemoval);
        messageTypeToHandler.put(StreamingAdditionRequest.class, this::handleStreamingAddition);
        messageTypeToHandler.put(StreamingRemovalRequest.class, this::handleStreamingRemoval);

        messageTypeToHandler.put(BasicPriceChangeRequest.class, this::handlePriceChangeRequest);
        messageTypeToHandler.put(GetUnapprovedPriceChangeRequest.class, this::handleAllUnapprovedPriceChangeRequests);
        messageTypeToHandler.put(ApprovePriceChangeRequest.class, this::handleApprovePriceChangeRequest);
        messageTypeToHandler.put(DissapprovePriceChangeRequest.class, this::handleDisapprovePriceChangeRequest);

        messageTypeToHandler.put(GetAllUnhandledComplaintsRequest.class, this::handleAllUnhandledComplaintsRequest);
        messageTypeToHandler.put(CreateNewComplaintRequest.class, this::handleNewComplaintCreationRequest);
        messageTypeToHandler.put(CloseComplaintRequest.class, this::handleCloseComplaintRequest);
        messageTypeToHandler.put(PurchaseCancellationFromComplaintRequest.class, this::handlePurchaseCancellationFromComplaintRequest);

        messageTypeToHandler.put(GetScreeningSeatMap.class, this::handleGetScreeningSeatMap);
        messageTypeToHandler.put(ScreeningTicketWithSeatsRequest.class, this::handleScreeningTicketWithSeats);
        messageTypeToHandler.put(ScreeningTicketWithCovidRequest.class, this::handleScreeningTicketWithCovid);
        messageTypeToHandler.put(VoucherPurchaseRequest.class, this::handleVoucherPurchase);
        messageTypeToHandler.put(VoucherBalanceRequest.class, this::handleVoucherBalanceRequest);
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

        messageTypeToHandler.get(requestType).accept((SertiaBasicRequest) msg, client);
    }

    private void handleSertiaCatalog(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            client.sendToClient(moviesCatalogController.getSertiaCatalog());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleScreeningTicketWithSeats(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            ScreeningTicketWithSeatsRequest ticketRequest = (ScreeningTicketWithSeatsRequest) request;
            client.sendToClient(screeningTicketController.buyTicketWithSeatChose(ticketRequest));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleScreeningTicketWithCovid(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            ScreeningTicketWithCovidRequest ticketRequest = (ScreeningTicketWithCovidRequest) request;
            client.sendToClient(screeningTicketController.buyTicketWithRegulations(ticketRequest));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGetScreeningSeatMap(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            GetScreeningSeatMap seatMapRequest = (GetScreeningSeatMap) request;
            client.sendToClient(screeningTicketController.getSeatMapForScreening(seatMapRequest));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleVoucherPurchase(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            VoucherPurchaseRequest voucherPurchaseRequest = (VoucherPurchaseRequest) request;
            client.sendToClient(screeningTicketController.buyVoucher(voucherPurchaseRequest));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleVoucherBalanceRequest(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            VoucherBalanceRequest voucherBalanceRequest = (VoucherBalanceRequest) request;
            client.sendToClient(screeningTicketController.getVoucherBalance(voucherBalanceRequest));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // region Price change requests handlers

    private void handleAllUnapprovedPriceChangeRequests(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            client.sendToClient(priceChangeController.getUnapprovedRequests());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDisapprovePriceChangeRequest(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            int priceChangeRequestId = ((DissapprovePriceChangeRequest) request).priceChangeRequestId;
            priceChangeController.disapprovePriceChangeRequest(priceChangeRequestId, (String) client.getInfo(ClientUsernameType));
            client.sendToClient(Boolean.TRUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleApprovePriceChangeRequest(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            int priceChangeRequestId = ((ApprovePriceChangeRequest) request).priceChangeRequestId;
            priceChangeController.approveRequest(priceChangeRequestId, (String) client.getInfo(ClientUsernameType));
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePriceChangeRequest(SertiaBasicRequest request, ConnectionToClient client) {
        BasicPriceChangeRequest priceChangeRequest = (BasicPriceChangeRequest) request;
        try {
            priceChangeController.requestPriceChange(priceChangeRequest, (String) client.getInfo(ClientUsernameType));
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // endregion

    // region Movies Addition/Removal handlers

    private void handleMovieRemoval(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            int movieId = ((RemoveMovieRequest) request).movieId;
            moviesCatalogController.removeMovie(movieId);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMovieAddition(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaMovie newMovie = ((AddMovieRequest) request).sertiaMovie;
        try {
            moviesCatalogController.addMovie(newMovie);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // endregion

    // region Screening Addition/Removal/Update handlers

    private void handleScreeningAddition(SertiaBasicRequest request, ConnectionToClient client) {
        CinemaScreeningMovie movieScreenings = ((AddScreeningRequest) request).cinemaScreeningMovie;
        try {
            moviesCatalogController.addMovieScreenings(movieScreenings);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleScreeningRemoval(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            int screeningId = ((RemoveScreeningRequest) request).screeningId;
            moviesCatalogController.removeMovieScreening(screeningId);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMovieScreeningTimeUpdate(SertiaBasicRequest request, ConnectionToClient client) {
        ClientScreening screeningToUpdate = ((ScreeningUpdateRequest) request).screening;
        try {
            moviesCatalogController.updateScreeningTime(screeningToUpdate);
            client.sendToClient(Boolean.TRUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // endregion

    // region Streaming Addition/Removal handlers

    private void handleStreamingAddition(SertiaBasicRequest request, ConnectionToClient client) {
        StreamingAdditionRequest streamingAdditionRequest = (StreamingAdditionRequest) request;
        try {
            moviesCatalogController.addStreaming(streamingAdditionRequest.movieId, streamingAdditionRequest.pricePerStream);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleStreamingRemoval(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            int streamingId = ((StreamingRemovalRequest) request).streamingId;
            moviesCatalogController.removeStreaming(streamingId);
            client.sendToClient(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // endregion

    private void handleLoginRequest(SertiaBasicRequest request, ConnectionToClient client) {
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

    // region Complaints handlers

    private void handlePurchaseCancellationFromComplaintRequest(SertiaBasicRequest request, ConnectionToClient client) {
        PurchaseCancellationFromComplaintRequest cancellationFromComplaintRequest = (PurchaseCancellationFromComplaintRequest) request;
        try {
            complaintsController.cancelPurchaseFromComplaint(cancellationFromComplaintRequest.complaintId,
                    (String) client.getInfo(ClientUsernameType),
                    cancellationFromComplaintRequest.refundAmount);
            client.sendToClient(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCloseComplaintRequest(SertiaBasicRequest request, ConnectionToClient client) {
        CloseComplaintRequest closeComplaintRequest = (CloseComplaintRequest) request;
        try {
            complaintsController.closeComplaint(closeComplaintRequest.complaintId, (String) client.getInfo(ClientUsernameType));
            client.sendToClient(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNewComplaintCreationRequest(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            complaintsController.createNewComplaint(((CreateNewComplaintRequest) request).complaint);
            client.sendToClient(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAllUnhandledComplaintsRequest(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            client.sendToClient(complaintsController.getAllUnhandledComplaints());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // endregion

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
