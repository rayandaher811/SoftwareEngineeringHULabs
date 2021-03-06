package org.sertia.server.communication;

import org.sertia.contracts.SertiaBasicRequest;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.complaints.requests.CloseComplaintRequest;
import org.sertia.contracts.complaints.requests.CreateNewComplaintRequest;
import org.sertia.contracts.complaints.requests.GetAllUnhandledComplaintsRequest;
import org.sertia.contracts.complaints.requests.PurchaseCancellationFromComplaintRequest;
import org.sertia.contracts.complaints.responses.AllUnhandledComplaintsResponse;
import org.sertia.contracts.covidRegulations.requests.*;
import org.sertia.contracts.covidRegulations.responses.ClientCovidRegulationsStatus;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.request.*;
import org.sertia.contracts.movies.catalog.response.GetMovieByIdResponse;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;
import org.sertia.contracts.price.change.request.ApprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.contracts.price.change.request.DissapprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.GetUnapprovedPriceChangeRequest;
import org.sertia.contracts.price.change.responses.GetUnapprovedPriceChangeResponse;
import org.sertia.contracts.reports.request.GetCinemaReports;
import org.sertia.contracts.reports.request.GetSertiaReports;
import org.sertia.contracts.reports.response.ClientReportsResponse;
import org.sertia.contracts.screening.ticket.request.*;
import org.sertia.contracts.screening.ticket.response.GetStreamingByLinkResponse;
import org.sertia.contracts.screening.ticket.response.GetVoucherInfoResponse;
import org.sertia.contracts.screening.ticket.response.ScreeningPaymentResponse;
import org.sertia.contracts.screening.ticket.response.TicketCancellationResponse;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.UserRole;
import org.sertia.contracts.user.login.request.LoginRequest;
import org.sertia.contracts.user.login.request.LogoutRequest;
import org.sertia.contracts.user.login.response.LoginResult;
import org.sertia.server.SertiaException;
import org.sertia.server.bl.*;
import org.sertia.server.bl.Services.CreditCardService;
import org.sertia.server.bl.Services.CreditCardServiceRefundsRecorderDecorator;
import org.sertia.server.bl.Services.ICreditCardService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MessageHandler extends AbstractServer {
    private final String ClientRoleType = "Role";
    private final String ClientSessionIdType = "Session";
    private final String ClientUsernameType = "Username";
    private final String ManagedCinemaIdType = "ManagedCinemaId";

    private final ICreditCardService creditCardService;
    private final ReportsController reportsController;
    private final MoviesCatalogController moviesCatalogController;
    private final ScreeningTicketController screeningTicketController;
    private final StreamingTicketController streamingTicketController;
    private final PriceChangeController priceChangeController;
    private final UserLoginController userLoginController;
    private final ComplaintsController complaintsController;
    private final CovidRegulationsController covidRegulationsController;
    private final CinemaController cinemaController;

    private final Map<Class<? extends SertiaBasicRequest>, BiConsumer<SertiaBasicRequest, ConnectionToClient>> messageTypeToHandler;

    private final RoleValidator roleValidator;

    public MessageHandler(int port) {
        super(port);
        this.roleValidator = new RoleValidator();
        this.messageTypeToHandler = new HashMap<>();
        initializeHandlerMapping();

        // Decorating the credit card service in order to record all the refunds
        creditCardService = new CreditCardServiceRefundsRecorderDecorator(new CreditCardService());

        this.priceChangeController = new PriceChangeController();
        this.complaintsController = new ComplaintsController(creditCardService);
        this.moviesCatalogController = new MoviesCatalogController(creditCardService);
        this.covidRegulationsController = new CovidRegulationsController(moviesCatalogController);
        this.screeningTicketController = new ScreeningTicketController(covidRegulationsController, creditCardService);
        this.streamingTicketController = new StreamingTicketController(creditCardService);
        this.cinemaController = new CinemaController();
        this.userLoginController = new UserLoginController(cinemaController);
        this.reportsController = new ReportsController(
                complaintsController,
                screeningTicketController,
                covidRegulationsController
        );
    }

    private void initializeHandlerMapping() {
        messageTypeToHandler.put(LoginRequest.class, this::handleLoginRequest);
        messageTypeToHandler.put(LogoutRequest.class, this::handleLogoutRequest);

        messageTypeToHandler.put(SertiaCatalogRequest.class, this::handleSertiaCatalog);
        messageTypeToHandler.put(GetMovieByIdRequest.class, this::handleGetMovieById);
        messageTypeToHandler.put(CinemaCatalogRequest.class, this::handleCinemaCatalog);
        messageTypeToHandler.put(ScreeningTimeUpdateRequest.class, this::handleMovieScreeningTimeUpdate);
        messageTypeToHandler.put(AddMovieRequest.class, this::handleMovieAddition);
        messageTypeToHandler.put(RemoveMovieRequest.class, this::handleMovieRemoval);
        messageTypeToHandler.put(RequestCinemasAndHalls.class, this::handleRequestCinemasAndHalls);
        messageTypeToHandler.put(AddScreeningRequest.class, this::handleScreeningAddition);
        messageTypeToHandler.put(RemoveScreeningRequest.class, this::handleScreeningRemoval);
        messageTypeToHandler.put(StreamingAdditionRequest.class, this::handleStreamingAddition);
        messageTypeToHandler.put(StreamingRemovalRequest.class, this::handleStreamingRemoval);
        messageTypeToHandler.put(GetStreamingByLinkRequest.class, this::handleGetStreamingByLinkRequest);

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
        messageTypeToHandler.put(StreamingPaymentRequest.class, this::handleStreamingPurchase);
        messageTypeToHandler.put(CancelStreamingTicketRequest.class, this::handleStreamingTicketCancel);
        messageTypeToHandler.put(VoucherPurchaseRequest.class, this::handleVoucherPurchase);
        messageTypeToHandler.put(VoucherBalanceRequest.class, this::handleVoucherBalanceRequest);
        messageTypeToHandler.put(GetVoucherInfoRequest.class, this::handleGetVoucherInfoRequest);

        messageTypeToHandler.put(ActiveCovidRegulationsRequest.class, this::handleActiveCovidRegulationRequest);
        messageTypeToHandler.put(CancelAllScreeningsDueCovidRequest.class, this::handleCancelAllScreeningsDueCovidRequest);
        messageTypeToHandler.put(CancelCovidRegulationsRequest.class, this::handleCancelCovidRegulationsRequest);
        messageTypeToHandler.put(GetCovidRegulationsStatusRequest.class, this::handleGetCovidRegulationsStatusRequest);
        messageTypeToHandler.put(UpdateCovidCrowdingRegulationsRequest.class, this::handleUpdateCovidCrowdingRegulationsRequest);

        messageTypeToHandler.put(GetSertiaReports.class, this::handleSertiaReports);
        messageTypeToHandler.put(GetCinemaReports.class, this::handleCinemaReports);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Received Message: " + msg.toString());

        Class<?> requestType = msg.getClass();
        if (!messageTypeToHandler.containsKey(requestType)) {
            System.out.println("user " + client.getName() + " requested non-existing action " + msg.getClass());
            sendResponseToClient(client, new SertiaBasicResponse(false, "There are no such action"));
            return;
        }

        if (!roleValidator.isClientAllowed((UserRole) client.getInfo(ClientRoleType), msg.getClass())) {
            System.out.println("user " + client.getName() + " denied request of type " + msg.getClass());
            sendResponseToClient(client, new SertiaBasicResponse(false, "Access denied you are not allowed to do such actions."));
            return;
        }

        messageTypeToHandler.get(requestType).accept((SertiaBasicRequest) msg, client);
    }

    private void handleSertiaCatalog(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaCatalogResponse response = new SertiaCatalogResponse(false);

        try {
            response = moviesCatalogController.getSertiaCatalog();
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת קטלוג סרטיה");
        }

        sendResponseToClient(client, response);
    }

    private void handleGetMovieById(SertiaBasicRequest request, ConnectionToClient client) {
        GetMovieByIdResponse response = new GetMovieByIdResponse(false);
        try {
            response.movie = moviesCatalogController.getMovieById(((GetMovieByIdRequest) request).movieId);
            response.setSuccessful(true);
        } catch (SertiaException e) {
            e.printStackTrace();
            response.setFailReason(e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת סרט");
        }

        sendResponseToClient(client, response);
    }

    private void handleCinemaCatalog(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);
        try {
            response = moviesCatalogController.getCinemaCatalog((CinemaCatalogRequest) request);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת קטלוג קולנוע");
        }

        sendResponseToClient(client, response);
    }

    private void handleScreeningTicketWithSeats(SertiaBasicRequest request, ConnectionToClient client) {
        ScreeningPaymentResponse response = new ScreeningPaymentResponse(false);
        try {
            ScreeningTicketWithSeatsRequest ticketRequest = (ScreeningTicketWithSeatsRequest) request;
            response = screeningTicketController.buyTicketWithSeatChose(ticketRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת רכישת כרטיסים");
        }

        sendResponseToClient(client, response);
    }

    private void handleScreeningTicketWithCovid(SertiaBasicRequest request, ConnectionToClient client) {
        ScreeningPaymentResponse response = new ScreeningPaymentResponse(false);
        try {
            ScreeningTicketWithCovidRequest ticketRequest = (ScreeningTicketWithCovidRequest) request;
            response = screeningTicketController.buyTicketWithRegulations(ticketRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת רכישת כרטיסים");
        }

        sendResponseToClient(client, response);
    }

    private void handleTicketCancel(SertiaBasicRequest request, ConnectionToClient client) {
        TicketCancellationResponse response = new TicketCancellationResponse(false);
        try {
            CancelScreeningTicketRequest cancelRequest = (CancelScreeningTicketRequest) request;
            response = screeningTicketController.cancelTicket(cancelRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת ביטול רכישה");
        }

        sendResponseToClient(client, response);
    }

    private void handleStreamingPurchase(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);
        try {
            StreamingPaymentRequest streamingPaymentRequest = (StreamingPaymentRequest) request;
            response = streamingTicketController.purchaseStreamingTicket(streamingPaymentRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת רכישת חבילת צפייה");
        }

        sendResponseToClient(client, response);
    }

    private void handleStreamingTicketCancel(SertiaBasicRequest request, ConnectionToClient client) {
        TicketCancellationResponse response = new TicketCancellationResponse(false);
        try {
            CancelStreamingTicketRequest streamingCancelRequest = (CancelStreamingTicketRequest) request;
            response = streamingTicketController.cancelStreamingTicket(streamingCancelRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת ביטול חבילת צפייה");
        }

        sendResponseToClient(client, response);
    }

    private void handleGetScreeningSeatMap(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);
        try {
            GetScreeningSeatMap seatMapRequest = (GetScreeningSeatMap) request;
            response = screeningTicketController.getSeatMapForScreening(seatMapRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת מפת אולם");
        }

        sendResponseToClient(client, response);
    }

    private void handleGetVoucherInfoRequest(SertiaBasicRequest request, ConnectionToClient client) {
        GetVoucherInfoResponse response = new GetVoucherInfoResponse(false);
        try {
            response = screeningTicketController.getVouchersInfo();
        } catch (SertiaException e) {
            e.printStackTrace();
            response.setFailReason(e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת מידע על כרטיסיות");
        }

        sendResponseToClient(client, response);
    }

    private void handleVoucherPurchase(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);
        try {
            VoucherPurchaseRequest voucherPurchaseRequest = (VoucherPurchaseRequest) request;
            response = screeningTicketController.buyVoucher(voucherPurchaseRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת רכישת כרטיסיה");
        }

        sendResponseToClient(client, response);
    }

    private void handleVoucherBalanceRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);
        try {
            VoucherBalanceRequest voucherBalanceRequest = (VoucherBalanceRequest) request;
            response = screeningTicketController.getVoucherBalance(voucherBalanceRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת בדיקת יתרת כרטיסיה");
        }

        sendResponseToClient(client, response);
    }

    private void handleGetStreamingByLinkRequest(SertiaBasicRequest request, ConnectionToClient client){
        GetStreamingByLinkResponse response = new GetStreamingByLinkResponse(false);
        try {
            response = streamingTicketController.getStreamingByLink(((GetStreamingByLinkRequest) request).link);
        } catch (SertiaException e) {
            e.printStackTrace();
            response.setSuccessful(false);
            response.setFailReason(e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת סרט");
        }

        sendResponseToClient(client, response);
    }

    // region Price change requests handlers

    private void handleAllUnapprovedPriceChangeRequests(SertiaBasicRequest request, ConnectionToClient client) {
        GetUnapprovedPriceChangeResponse response = new GetUnapprovedPriceChangeResponse(false);

        try {
            response.unapprovedRequests = priceChangeController.getUnapprovedRequests();
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת בקשות שינוי מחיר");
        }

        sendResponseToClient(client, response);
    }

    private void handleDisapprovePriceChangeRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            int priceChangeRequestId = ((DissapprovePriceChangeRequest) request).priceChangeRequestId;
            priceChangeController.disapprovePriceChangeRequest(priceChangeRequestId, (String) client.getInfo(ClientUsernameType));
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת ביטול בקשת שינוי מחיר");
        }

        sendResponseToClient(client, response);
    }

    private void handleApprovePriceChangeRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            int priceChangeRequestId = ((ApprovePriceChangeRequest) request).priceChangeRequestId;
            priceChangeController.approveRequest(priceChangeRequestId, (String) client.getInfo(ClientUsernameType));
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת אישור בקשת שינוי מחיר");
        }

        sendResponseToClient(client, response);
    }

    private void handlePriceChangeRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            BasicPriceChangeRequest priceChangeRequest = (BasicPriceChangeRequest) request;
            priceChangeController.requestPriceChange(priceChangeRequest, (String) client.getInfo(ClientUsernameType));
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת יצירת בקשה לשינוי מחיר");
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
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת מחיקת סרט");
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
            response.setFailReason("ארעה שגיאה בעת הוספת סרט חדש");
        }

        sendResponseToClient(client, response);
    }

    // endregion

    // region Screening Addition/Removal/Update handlers

    private void handleRequestCinemasAndHalls(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            response = moviesCatalogController.getCinemaAndHalls();
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת בתי קולנוע ואולמות");
        }

        sendResponseToClient(client, response);
    }

    private void handleScreeningAddition(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            AddScreeningRequest addScreeningRequest = (AddScreeningRequest) request;
            moviesCatalogController.addMovieScreenings(addScreeningRequest);
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת הוספת הקרנה");
        }

        sendResponseToClient(client, response);
    }

    private void handleScreeningRemoval(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            int screeningId = ((RemoveScreeningRequest) request).screeningId;
            moviesCatalogController.removeMovieScreening(screeningId);
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת מחיקת הקרנה");
        }

        sendResponseToClient(client, response);
    }

    private void handleMovieScreeningTimeUpdate(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            ClientScreening screeningToUpdate = ((ScreeningTimeUpdateRequest) request).screening;
            moviesCatalogController.updateScreeningTime(screeningToUpdate);
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת עדכון זמן הקרנה");
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
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת הוספת חבילת צפייה");
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
            response.setFailReason("ארעה שגיאה בעת מחיקת חבילת צפייה");
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
            if (result.userRole != UserRole.None) {
                client.setInfo(ClientUsernameType, loginCredentials.username);

                // Saving the manager's cinema
                if (result.userRole == UserRole.BranchManager)
                    client.setInfo(ManagedCinemaIdType, cinemaController.getCinemaIdByManagerUsername(loginCredentials.username));
            }
        } catch (SertiaException e) {
            result.setFailReason(e.getMessage());
            result.setSuccessful(false);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccessful(false);
            result.setFailReason("ארעה שגיאה בעת חיבור");
        }

        sendResponseToClient(client, result);
    }

    private void handleLogoutRequest(SertiaBasicRequest request, ConnectionToClient client) {

        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {

            // Clearing up client's saved info
            client.setInfo(ClientRoleType, null);
            client.setInfo(ClientSessionIdType, null);
            client.setInfo(ClientUsernameType, null);
            client.setInfo(ManagedCinemaIdType, null);

            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת ניתוק");
        }

        sendResponseToClient(client, response);
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
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת ביטול רכישה מתלונה");
        }

        sendResponseToClient(client, response);
    }

    private void handleCloseComplaintRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            CloseComplaintRequest closeComplaintRequest = (CloseComplaintRequest) request;
            complaintsController.closeComplaint(closeComplaintRequest.complaintId, (String) client.getInfo(ClientUsernameType));
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת טיפול בתלונה");
        }

        sendResponseToClient(client, response);
    }

    private void handleNewComplaintCreationRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            complaintsController.createNewComplaint(((CreateNewComplaintRequest) request));
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת פתיחת התלונה");
        }

        sendResponseToClient(client, response);
    }

    private void handleAllUnhandledComplaintsRequest(SertiaBasicRequest request, ConnectionToClient client) {
        AllUnhandledComplaintsResponse response = new AllUnhandledComplaintsResponse(false);

        try {
            response.openComplaints = complaintsController.getAllUnhandledComplaints();
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת התלונות הפתוחות");
        }

        sendResponseToClient(client, response);
    }

    // endregion

    // region Covid regulation changes + status getter

    private void handleCancelAllScreeningsDueCovidRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            CancelAllScreeningsDueCovidRequest cancellationRequest = (CancelAllScreeningsDueCovidRequest) request;
            covidRegulationsController.cancelAllScreeningsDueCovid(cancellationRequest.cancellationStartDate,
                    cancellationRequest.cancellationEndDate);
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת ביטול הקרנות עקב קורונה");
        }

        sendResponseToClient(client, response);
    }

    private void handleUpdateCovidCrowdingRegulationsRequest(SertiaBasicRequest request, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            UpdateCovidCrowdingRegulationsRequest updateRequest = (UpdateCovidCrowdingRegulationsRequest) request;
            covidRegulationsController.updateCovidCrowdingRegulations(updateRequest.newMaxNumberOfPeople);
            response.setSuccessful(true);
        } catch (SertiaException e) {
            response.setFailReason(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת עדכון מגבלות תו סגול");
        }

        sendResponseToClient(client, response);
    }

    private void handleCancelCovidRegulationsRequest(SertiaBasicRequest sertiaBasicRequest, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            covidRegulationsController.cancelCovidRegulations();
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת ביטול מגבלות תו סגול");
        }

        sendResponseToClient(client, response);
    }

    private void handleGetCovidRegulationsStatusRequest(SertiaBasicRequest sertiaBasicRequest, ConnectionToClient client) {
        ClientCovidRegulationsStatus response = null;

        try {
            response = covidRegulationsController.getCovidRegulationsStatus();
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ClientCovidRegulationsStatus(false);
            response.setFailReason("ארעה שגיאה בעת קבלת מגבלות תו סגול נוכחיות");
        }

        sendResponseToClient(client, response);
    }

    private void handleActiveCovidRegulationRequest(SertiaBasicRequest sertiaBasicRequest, ConnectionToClient client) {
        SertiaBasicResponse response = new SertiaBasicResponse(false);

        try {
            covidRegulationsController.activeCovidRegulations();
            response.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת הפעלת מגבלות תו סגול");
        }

        sendResponseToClient(client, response);
    }

    // endregion

    // region reports

    private void handleSertiaReports(SertiaBasicRequest sertiaBasicRequest, ConnectionToClient client) {
        ClientReportsResponse response = new ClientReportsResponse(false);

        try {
            response = reportsController.getSertiaReports();
        } catch (Exception e) {
            e.printStackTrace();
            response.failReason = "ארעה שגיאה בעת קבלת דוחות סרטיה";
        }

        sendResponseToClient(client, response);
    }

    private void handleCinemaReports(SertiaBasicRequest sertiaBasicRequest, ConnectionToClient client) {
        ClientReportsResponse response = new ClientReportsResponse(false);

        try {
            Integer cinemaId = (Integer) client.getInfo(ManagedCinemaIdType);
            if (cinemaId == null) {
                response.setFailReason("אינך מנהל של סניף קולנוע");
                response.setSuccessful(false);
            } else {
                response = reportsController.getCinemaReports(cinemaId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailReason("ארעה שגיאה בעת קבלת דוחות עבור בית קולנוע");
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

    private void sendResponseToClient(ConnectionToClient client, SertiaBasicResponse response) {
        try {
            client.sendToClient(response);
        } catch (Exception e) {
            System.out.println("We couldn't send a response to " + client.getInfo(ClientSessionIdType));
        }
    }
}
