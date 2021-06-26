package org.sertia.server.communication;

import org.sertia.contracts.SertiaBasicRequest;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.complaints.requests.CloseComplaintRequest;
import org.sertia.contracts.complaints.requests.CreateNewComplaintRequest;
import org.sertia.contracts.complaints.requests.GetAllUnhandledComplaintsRequest;
import org.sertia.contracts.complaints.requests.PurchaseCancellationFromComplaintRequest;
import org.sertia.contracts.complaints.responses.AllUnhandledComplaintsResponse;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.request.*;
import org.sertia.contracts.price.change.request.ApprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.contracts.price.change.request.DissapprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.GetUnapprovedPriceChangeRequest;
import org.sertia.contracts.price.change.responses.GetUnapprovedPriceChangeResponse;
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
        messageTypeToHandler.put(CancelScreeningTicketRequest.class, this::handleTicketCancel);
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

    private void handleTicketCancel(SertiaBasicRequest request, ConnectionToClient client) {
        try {
            CancelScreeningTicketRequest cancelRequest = (CancelScreeningTicketRequest) request;
            client.sendToClient(screeningTicketController.cancelTicket(cancelRequest));
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
        GetUnapprovedPriceChangeResponse response = new GetUnapprovedPriceChangeResponse(false);

        try {
            response.unapprovedRequests = priceChangeController.getUnapprovedRequests();
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleAllUnapprovedPriceChangeRequests.");
        }

        sendResponseToClient(client, response);
    }

    private void handleDisapprovePriceChangeRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            int priceChangeRequestId = ((DissapprovePriceChangeRequest) request).priceChangeRequestId;
            priceChangeController.disapprovePriceChangeRequest(priceChangeRequestId, (String) client.getInfo(ClientUsernameType));
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleDisapprovePriceChangeRequest.");
        }

        sendResponseToClient(client, response);
    }

    private void handleApprovePriceChangeRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            int priceChangeRequestId = ((ApprovePriceChangeRequest) request).priceChangeRequestId;
            priceChangeController.approveRequest(priceChangeRequestId, (String) client.getInfo(ClientUsernameType));
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleApprovePriceChangeRequest.");
        }

        sendResponseToClient(client, response);
    }

    private void handlePriceChangeRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            BasicPriceChangeRequest priceChangeRequest = (BasicPriceChangeRequest) request;
            priceChangeController.requestPriceChange(priceChangeRequest, (String) client.getInfo(ClientUsernameType));
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handlePriceChangeRequest.");
        }

        sendResponseToClient(client, response);
    }

    // endregion

    // region Movies Addition/Removal handlers

    private void handleMovieRemoval(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            int movieId = ((RemoveMovieRequest) request).movieId;
            moviesCatalogController.removeMovie(movieId);
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleMovieRemoval.");
        }

        sendResponseToClient(client, response);
    }

    private void handleMovieAddition(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            SertiaMovie newMovie = ((AddMovieRequest) request).sertiaMovie;
            moviesCatalogController.addMovie(newMovie);
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleMovieAddition.");
        }

        sendResponseToClient(client, response);
    }

    // endregion

    // region Screening Addition/Removal/Update handlers

    private void handleScreeningAddition(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            CinemaScreeningMovie movieScreenings = ((AddScreeningRequest) request).cinemaScreeningMovie;
            moviesCatalogController.addMovieScreenings(movieScreenings);
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleScreeningAddition.");
        }

        sendResponseToClient(client, response);
    }

    private void handleScreeningRemoval(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            int screeningId = ((RemoveScreeningRequest) request).screeningId;
            moviesCatalogController.removeMovieScreening(screeningId);
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleScreeningRemoval.");
        }

        sendResponseToClient(client, response);
    }

    private void handleMovieScreeningTimeUpdate(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            ClientScreening screeningToUpdate = ((ScreeningUpdateRequest) request).screening;
            moviesCatalogController.updateScreeningTime(screeningToUpdate);
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleMovieScreeningTimeUpdate.");
        }

        sendResponseToClient(client, response);
    }

    // endregion

    // region Streaming Addition/Removal handlers

    private void handleStreamingAddition(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            StreamingAdditionRequest streamingAdditionRequest = (StreamingAdditionRequest) request;
            moviesCatalogController.addStreaming(streamingAdditionRequest.movieId, streamingAdditionRequest.pricePerStream);
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleStreamingAddition.");
        }

        sendResponseToClient(client, response);
    }

    private void handleStreamingRemoval(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            int streamingId = ((StreamingRemovalRequest) request).streamingId;
            moviesCatalogController.removeStreaming(streamingId);
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleStreamingRemoval.");
        }

        sendResponseToClient(client, response);
    }

    // endregion

    private void handleLoginRequest(SertiaBasicRequest request, ConnectionToClient client) {

        LoginResult result = new LoginResult();

        try {
            LoginCredentials loginCredentials = ((LoginRequest) request).loginCredentials;
            result = userLoginController.login(loginCredentials);

            // Saving the user's role and session ID
            client.setInfo(ClientRoleType, result.userRole);
            client.setInfo(ClientSessionIdType, result.sessionId);

            result.setSuccessful(result.userRole != UserRole.None);

            // Saving the username if the client has special role
            if (result.userRole != UserRole.None){
                client.setInfo(ClientUsernameType, loginCredentials.username);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccessful(false);
            result.setFailReason("We couldn't handleLoginRequest.");
        }

        sendResponseToClient(client, result);
    }

    // region Complaints handlers

    private void handlePurchaseCancellationFromComplaintRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            PurchaseCancellationFromComplaintRequest cancellationFromComplaintRequest = (PurchaseCancellationFromComplaintRequest) request;
            complaintsController.cancelPurchaseFromComplaint(cancellationFromComplaintRequest.complaintId,
                    (String) client.getInfo(ClientUsernameType),
                    cancellationFromComplaintRequest.refundAmount);
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handlePurchaseCancellationFromComplaintRequest.");
        }

        sendResponseToClient(client, response);
    }

    private void handleCloseComplaintRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            CloseComplaintRequest closeComplaintRequest = (CloseComplaintRequest) request;
            complaintsController.closeComplaint(closeComplaintRequest.complaintId, (String) client.getInfo(ClientUsernameType));
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleCloseComplaintRequest.");
        }

        sendResponseToClient(client, response);
    }

    private void handleNewComplaintCreationRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            complaintsController.createNewComplaint(((CreateNewComplaintRequest) request).complaint);
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleNewComplaintCreationRequest.");
        }

        sendResponseToClient(client, response);
    }

    private void handleAllUnhandledComplaintsRequest(SertiaBasicRequest request, ConnectionToClient client) {
        AllUnhandledComplaintsResponse response = new AllUnhandledComplaintsResponse(false);

        try {
            response.openComplaints = complaintsController.getAllUnhandledComplaints();
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("We couldn't handleAllUnhandledComplaintsRequest.");
        }

        sendResponseToClient(client, response);
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

    private void sendResponseToClient(ConnectionToClient client, SertiaBasicResponse response){
        try{
            client.sendToClient(response);
        } catch (Exception e){
            System.out.println("We couldn't send a response to " + (String) client.getInfo(ClientSessionIdType));
        }
    }
}
