package org.sertia.server.bl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.covidRegulations.responses.ClientCovidRegulationsStatus;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.contracts.screening.ticket.HallSeat;
import org.sertia.contracts.screening.ticket.VoucherDetails;
import org.sertia.contracts.screening.ticket.request.*;
import org.sertia.contracts.screening.ticket.response.*;
import org.sertia.server.SertiaException;
import org.sertia.server.bl.Services.CustomerNotifier;
import org.sertia.server.bl.Services.ICreditCardService;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.sertia.server.bl.Utils.getPaymentDetails;

public class ScreeningTicketController extends Reportable {
    private final CovidRegulationsController covidRegulationsController;
    private final ICreditCardService creditCardService;

    public ScreeningTicketController(CovidRegulationsController covidRegulationsController, ICreditCardService creditDetails) {
        this.covidRegulationsController = covidRegulationsController;
        this.creditCardService = creditDetails;
    }

    public ScreeningPaymentResponse buyTicketWithRegulations(ScreeningTicketWithCovidRequest request) {
        ClientSeatMapResponse seatMapForScreening = getSeatMapForScreening(request.screeningId);
        ScreeningPaymentResponse response = new ScreeningPaymentResponse(true);

        try {
            int numberOfWantedSeats = request.numberOfSeats;
            Optional<Screening> optionalScreening = DbUtils.getById(Screening.class, request.screeningId);
            if (!optionalScreening.isPresent()) {
                response.isSuccessful = false;
                response.failReason = "ההקרנה אינה קיימת";

                return response;
            }

            int numberOfTakenSeats = optionalScreening.get().getTickets().size();
            if (numberOfTakenSeats + numberOfWantedSeats > getMaxTicketsForHall(optionalScreening.get().getHall())) {
                response.isSuccessful = false;
                response.setFailReason("אין מספיק מושבים פנויים");

                return response;
            }

            Set<HallSeat> seats = automaticChoseSeats(seatMapForScreening, numberOfWantedSeats);
            return purchaseTicketsForScreening(createScreeningTicketWithSeatsRequest(request, seats));
        } catch (IllegalArgumentException exception) {
            System.out.println("Failed to buy tickets with regulations");
            response.isSuccessful = false;
            response.setFailReason("תהליך רכישה נכשל");

            return response;
        }
    }

    public ScreeningPaymentResponse buyTicketWithSeatChose(ScreeningTicketWithSeatsRequest request) {
        if (!isPaymentRequestValid(request)) {
            ScreeningPaymentResponse paymentResponse = new ScreeningPaymentResponse(false);
            paymentResponse.failReason = "נתוני חיוב אינם תקינים";

            return paymentResponse;
        }

        return purchaseTicketsForScreening(request);
    }

    public TicketCancellationResponse cancelTicket(CancelScreeningTicketRequest request) {
        TicketCancellationResponse response = new TicketCancellationResponse(true);
        Optional<TicketCancellationResponse> ticketCancellationResponse = DbUtils.getById(ScreeningTicket.class, request.ticketId)
                .map(screeningTicket -> {
                    if (!screeningTicket.getPaymentInfo().getPayerId().equals(request.userId)) {
                        return null;
                    }

                    if (screeningTicket.isVoucher()) {
                        return Utils.createFailureResponse(response, "לא ניתן לבטל כרטיס שנרכש על ידי כרטיסיה");
                    }

                    try (Session session = HibernateSessionFactory.getInstance().openSession()) {
                        session.beginTransaction();
                        session.delete(screeningTicket);
                        session.getTransaction().commit();
                        double refundAmount = refundCreditForScreening(screeningTicket.getScreening(), screeningTicket.getPaymentInfo());
                        return new TicketCancellationResponse(true, refundAmount);
                    } catch (RuntimeException e) {
                        System.out.println("couldn't delete ticket " + request.ticketId);
                        return Utils.createFailureResponse(response, "ביטול רכישה נכשל, פנה לשירות לקוחות");
                    }
                });

        return ticketCancellationResponse.orElseGet(() ->
                ticketCancellationResponse.orElse(Utils.createFailureResponse(response, "הכרטיס אינו קיים")));

    }

    public SertiaBasicResponse getSeatMapForScreening(GetScreeningSeatMap request) {
        return getSeatMapForScreening(request.screeningId);
    }

    public SertiaBasicResponse buyVoucher(BasicPaymentRequest paymentDetails) {
        VouchersInfo vouchersInfo = DbUtils.getById(VouchersInfo.class, VouchersInfo.singleRecordId).orElse(null);
        VoucherPaymentResponse response = new VoucherPaymentResponse(true);
        if (vouchersInfo == null) {
            response.isSuccessful = false;
            response.failReason = "לא קיימים נתוני כרטיסיות במערכת, אנא פנה לשירות לקוחות";

            return response;
        }

        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            TicketsVoucher voucher = new TicketsVoucher();
            CustomerPaymentDetails customerPaymentDetails = getPaymentDetails(paymentDetails);
            session.saveOrUpdate(customerPaymentDetails);
            voucher.setCustomerPaymentDetails(customerPaymentDetails);

            voucher.setTicketsBalance(vouchersInfo.getVoucherInitialBalance());
            voucher.setPurchaseDate(LocalDateTime.now());
            response.voucherId = (int) session.save(voucher);
            CustomerNotifier.getInstance().notify(paymentDetails.cardHolderEmail, getVoucherMail(response));

            return response;
        } catch (RuntimeException exception) {
            response.isSuccessful = false;
            response.failReason = "רכישת כרטיסיה נכשל, אנא פנה לשירות לקוחות";

            return response;
        }
    }

    public GetVoucherInfoResponse getVouchersInfo() {
        GetVoucherInfoResponse response = new GetVoucherInfoResponse(true);

        VouchersInfo vouchersInfo = DbUtils.getById(VouchersInfo.class, VouchersInfo.singleRecordId).orElse(null);
        if (vouchersInfo == null) {
            response.isSuccessful = false;
            response.failReason = "לא קיימים נתוני כרטיסיות במערכת, אנא פנה לשירות לקוחות";
        } else {
            response.initialBalance = vouchersInfo.getVoucherInitialBalance();
            response.price = vouchersInfo.getPrice();
        }

        return response;
    }

    public SertiaBasicResponse getVoucherBalance(VoucherBalanceRequest request) {
        VoucherBalanceResponse response = new VoucherBalanceResponse(true);
        return DbUtils.getById(TicketsVoucher.class, request.voucherId).map(ticketsVoucher -> {
            response.balance = ticketsVoucher.getTicketsBalance();

            return response;
        }).orElseGet(() -> {
            response.isSuccessful = false;
            response.setFailReason("לא קיימת כרטיסיה עם הנתונים שהוזנו");

            return response;
        });
    }

    public boolean useVoucher(ScreeningTicket ticket, int voucherId, Session session) {
        return DbUtils.getById(TicketsVoucher.class, voucherId, session).map(ticketsVoucher -> {
            ticket.setIsVoucher(ticketsVoucher);
            ticketsVoucher.setTicketsBalance(ticketsVoucher.getTicketsBalance() - 1);
            session.persist(ticketsVoucher);
            return true;
        }).orElse(false);
    }

    private boolean isPaymentRequestValid(BasicPaymentRequest clientPaymentRequest) {
        return true;
    }

    private double refundCreditForScreening(Screening screening, CustomerPaymentDetails paymentDetails) {
        LocalDateTime screeningTime = screening.getScreeningTime();
        long hoursToScreening = ChronoUnit.HOURS.between(LocalDateTime.now(), screeningTime);
        double refundAmount;
        if (hoursToScreening >= 3) {
            refundAmount = screening.getScreenableMovie().getTicketPrice();
        } else if (hoursToScreening >= 1) {
            refundAmount = screening.getScreenableMovie().getTicketPrice() / 2;
        } else {
            refundAmount = 0;
        }

        creditCardService.refund(paymentDetails, refundAmount, RefundReason.ScreeningService);
        return refundAmount;
    }

    private ScreeningPaymentResponse purchaseTicketsForScreening(ScreeningTicketWithSeatsRequest request) {
        ScreeningPaymentResponse paymentResponse = new ScreeningPaymentResponse(true);
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            session.beginTransaction();

            Set<ScreeningTicket> screeningTickets = DbUtils.getById(Screening.class, request.screeningId)
                    .map(screening -> {
                        paymentResponse.hallNumber = screening.getHall().getHallNumber();
                        paymentResponse.cinemaName = screening.getHall().getCinema().getName();
                        paymentResponse.finalPrice = screening.getScreenableMovie().getTicketPrice() * request.chosenSeats.size();
                        paymentResponse.movieName = screening.getScreenableMovie().getMovie().getName();
                        paymentResponse.screeningTime = screening.getScreeningTime();

                        return request.chosenSeats.stream()
                                .map(hallSeat -> createScreeningTicket(hallSeat, screening, session))
                                .collect(Collectors.toSet());
                    })
                    .orElse(Collections.emptySet());

            CustomerPaymentDetails paymentDetails;
            if (request.isUsingVoucher()) {
                VoucherDetails voucherDetails = request.voucherDetails;
                if (!doesVoucherExist(voucherDetails)) {
                    session.getTransaction().rollback();
                    return Utils.createFailureResponse(paymentResponse, "נתוני הכרטיסיה אינם תקינים");
                }

                if (!isVoucherUsageValid(voucherDetails, request.chosenSeats.size())) {
                    session.getTransaction().rollback();
                    return Utils.createFailureResponse(paymentResponse, "אין מספיק יתרה בכרטיסיה");
                }

                TicketsVoucher voucher = DbUtils.getById(TicketsVoucher.class, request.voucherDetails.voucherId, session)
                        .orElseThrow(() -> new SertiaException("קבלת נתוני כרטיסיה נכשלה"));
                paymentDetails = voucher.getCustomerPaymentDetails();
                paymentResponse.isVoucher = true;
                paymentResponse.voucherBalance = voucher.getTicketsBalance() - request.chosenSeats.size();
                request.cardHolderEmail = paymentDetails.getEmail();
            } else {
                paymentDetails = getPaymentDetails(request);
                session.saveOrUpdate(paymentDetails);
            }

            for (ScreeningTicket screeningTicket : screeningTickets) {
                screeningTicket.setPaymentInfo(paymentDetails);
                if (request.isUsingVoucher()) {
                    handlePaymentForTicketWithVoucher(screeningTicket, request.voucherDetails.voucherId, session);
                }

                int ticketId = (int) session.save(screeningTicket);
                org.sertia.server.dl.classes.HallSeat seat = screeningTicket.getSeat();
                HallSeat purchasedSeat = new HallSeat(seat.getId());
                purchasedSeat.isTaken = true;
                purchasedSeat.row = seat.getRowNumber();
                purchasedSeat.numberInRow = seat.getNumberInRow();
                paymentResponse.addTicket(ticketId, purchasedSeat);
            }

            session.flush();
            session.getTransaction().commit();
        } catch (RuntimeException exception) {
            paymentResponse.isSuccessful = false;
            paymentResponse.failReason = "ארעה שגיאה בעת תהליך הרכישה";

            return paymentResponse;
        }

        CustomerNotifier.getInstance().notify(request.cardHolderEmail, getScreeningMail(paymentResponse));
        return paymentResponse;
    }

    private boolean handlePaymentForTicketWithVoucher(ScreeningTicket screeningTicket, int voucherId, Session session) {
        screeningTicket.setIsVoucher(true);
        return useVoucher(screeningTicket, voucherId, session);
    }

    private boolean isVoucherUsageValid(VoucherDetails voucherDetails, int numberOfTickets) {
        SertiaBasicResponse voucherBalance = getVoucherBalance(new VoucherBalanceRequest(voucherDetails.voucherId));
        return voucherBalance.isSuccessful &&
                (((VoucherBalanceResponse) voucherBalance).balance >= numberOfTickets);
    }

    private boolean doesVoucherExist(VoucherDetails voucherDetails) {
        return DbUtils.getById(TicketsVoucher.class, voucherDetails.voucherId)
                .map(ticketsVoucher -> ticketsVoucher.getCustomerPaymentDetails().getPayerId().equals(voucherDetails.buyerId))
                .orElse(false);
    }

    private String getScreeningMail(ScreeningPaymentResponse response) {
        StringBuilder stringBuilder = new StringBuilder();
        if (response.isVoucher) {
            stringBuilder.append(" השתמשת בכרטיסיה, וכעת היתרה היא ").append(response.voucherBalance)
                    .append("\n");
        }

        stringBuilder.append("\nסרט: ").append(response.movieName)
                .append("\nקולנוע: ").append(response.cinemaName)
                .append("\nאולם: ").append(response.hallNumber)
                .append("\nשעת הקרנה: ").append(response.screeningTime);

        if (!response.isVoucher) {
            stringBuilder.append("\nמחיר סופי: ").append(response.finalPrice);
        }

        stringBuilder.append("\n")
                .append(":כרטיסים").append("\n");
        response.ticketIdToSeat.forEach((ticketId, hallSeat) -> stringBuilder
                .append(" :מזהה כרטיס ").append(ticketId.toString())
                .append("\n")
                .append(" שורה ").append(hallSeat.row)
                .append(" כיסא ").append(hallSeat.numberInRow)
                .append("\n"));

        return stringBuilder.toString();
    }

    private String getVoucherMail(VoucherPaymentResponse response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nמזהה כרטיסיה:").append(response.voucherId);
        return stringBuilder.toString();
    }

    private int getMaxTicketsForHall(Hall hall) {
        ClientCovidRegulationsStatus covidRegulationsStatus = covidRegulationsController.getCovidRegulationsStatus();
        int hallCapacity = hall.getSeats().size();

        if (!covidRegulationsStatus.isActive) {
            return hallCapacity;
        }

        if (hallCapacity > 1.2 * covidRegulationsStatus.maxNumberOfPeople) {
            return covidRegulationsStatus.maxNumberOfPeople;
        }

        if (hallCapacity > 0.8 * covidRegulationsStatus.maxNumberOfPeople) {
            return (int) Math.floor(0.8 * covidRegulationsStatus.maxNumberOfPeople);
        }

        return Math.floorDiv(hallCapacity, 2);
    }

    private ClientSeatMapResponse getSeatMapForScreening(int screeningId) {
        final List<HallSeat> hallSeatList = new ArrayList<>();

        return DbUtils.getById(Screening.class, screeningId).map(screening ->
        {
            Set<Integer> takenSeats =
                    screening.getTickets().stream()
                            .map(ScreeningTicket::getSeat)
                            .map(org.sertia.server.dl.classes.HallSeat::getId)
                            .collect(Collectors.toSet());

            screening.getHall().getSeats().stream().map(hallSeat -> {
                HallSeat clientHallSeat = new HallSeat(hallSeat.getId());
                clientHallSeat.row = hallSeat.getRowNumber();
                clientHallSeat.numberInRow = hallSeat.getNumberInRow();
                clientHallSeat.isTaken = takenSeats.contains(hallSeat.getId());
                return clientHallSeat;
            }).forEach(hallSeatList::add);
            return new ClientSeatMapResponse(true, hallSeatList);
        }).orElseGet(() -> {
            ClientSeatMapResponse response = new ClientSeatMapResponse(false, Collections.emptyList());
            response.setFailReason("הקרנה אינה קיימת");
            return response;
        });
    }

    private ScreeningTicket createScreeningTicket(int hallSeat, Screening screening, Session session) {
        ScreeningTicket ticket = new ScreeningTicket();
        org.sertia.server.dl.classes.HallSeat requestedSeat =
                DbUtils.getById(org.sertia.server.dl.classes.HallSeat.class, hallSeat)
                        .orElseThrow(() -> new IllegalArgumentException("seat doesn't exist"));

        if (!isSeatFree(screening.getId(), hallSeat, session)) {
            throw new IllegalArgumentException("אחד הכיסאות אינם פנויים כבר, אנא רענן את העמוד ובחר מחדש");
        }

        ticket.setScreening(screening);
        ticket.setIsVoucher(false);
        ticket.setPaidPrice(screening.getScreenableMovie().getTicketPrice());
        ticket.setSeat(requestedSeat);
        ticket.setPurchaseDate(LocalDateTime.now());
        return ticket;
    }

    private Set<HallSeat> automaticChoseSeats(ClientSeatMapResponse seatMap, int numberOfSeats) {
        Set<HallSeat> seats = new HashSet<>();

        for (HallSeat hallSeat : seatMap.hallSeats) {
            if (!hallSeat.isTaken) {
                seats.add(hallSeat);
                numberOfSeats--;
                if (numberOfSeats == 0) {
                    return seats;
                }
            }
        }

        throw new IllegalArgumentException("לא היה ניתן לבחור כיסאות עבור ההקרנה");
    }

    private boolean isSeatFree(int screeningId, int seatId, Session session) {
        Query query = session
                .createQuery("from ScreeningTicket where seat_seat_id = :seat and screening_screening_id = :screening");
        query.setParameter("seat", seatId);
        query.setParameter("screening", screeningId);
        return query.list().isEmpty();
    }

    private ScreeningTicketWithSeatsRequest createScreeningTicketWithSeatsRequest(ScreeningTicketWithCovidRequest request, Set<HallSeat> seats) {
        ScreeningTicketWithSeatsRequest screeningTicketRequest = new ScreeningTicketWithSeatsRequest();
        screeningTicketRequest.cardHolderId = request.cardHolderId;
        screeningTicketRequest.creditCardNumber = request.creditCardNumber;
        screeningTicketRequest.cardHolderName = request.cardHolderName;
        screeningTicketRequest.cardHolderPhone = request.cardHolderPhone;
        screeningTicketRequest.cardHolderEmail = request.cardHolderEmail;
        screeningTicketRequest.expirationDate = request.expirationDate;
        screeningTicketRequest.cvv = request.cvv;
        screeningTicketRequest.screeningId = request.screeningId;
        screeningTicketRequest.isVoucher = request.isVoucher;
        screeningTicketRequest.voucherDetails = request.voucherDetails;
        screeningTicketRequest.chosenSeats = seats.stream().map(HallSeat::getId).collect(Collectors.toList());

        return screeningTicketRequest;
    }

    @Override
    public List<ClientReport> createSertiaReports() {
        List<ClientReport> reports = new ArrayList<>();
        reports.add(ticketsByCinemaReport());
        reports.add(screeningsAndVouchersReport());

        return reports;
    }

    private ClientReport ticketsByCinemaReport() {
        final ClientReport report = new ClientReport();
        report.title = "כרטיסים שנמכרו בכל קולנוע";
        List<ScreeningTicket> tickets = getDataOfThisMonth(ScreeningTicket.class, "purchaseDate");
        Map<String, List<ScreeningTicket>> cinemaToTickets = tickets.stream().collect(Collectors.groupingBy(screeningTicket -> screeningTicket
                .getScreening().getHall().getCinema().getName()));

        for (Cinema cinema : DbUtils.getAll(Cinema.class)) {
            if (!cinemaToTickets.containsKey(cinema.getName())) {
                cinemaToTickets.put(cinema.getName(), Collections.emptyList());
            }
        }

        cinemaToTickets.forEach((cinemaName, screeningTickets) -> report.addEntry(cinemaName, screeningTickets.size()));

        return report;
    }

    private ClientReport screeningsAndVouchersReport() {
        final ClientReport report = new ClientReport();
        report.title = "כרטיסיות ובחבילות צפייה שנמכרו";
        List<TicketsVoucher> vouchers = getDataOfThisMonth(TicketsVoucher.class, "purchaseDate");
        List<StreamingLink> streamingLinks = getDataOfThisMonth(StreamingLink.class, "purchaseDate");
        report.addEntry("כרטיסיות", vouchers.size());
        report.addEntry("חבילות צפייה", streamingLinks.size());

        return report;
    }

    @Override
    public List<ClientReport> createCinemaReports(int cinemaId) {
        ClientReport clientReport = new ClientReport();
        clientReport.title = "מספר כרטיסים שנמכרו בקולנוע שלך";
        List<ScreeningTicket> tickets = getDataOfThisMonth(ScreeningTicket.class, "purchaseDate");
        int numberOfTickets = (int) tickets.stream().filter(screeningTicket -> screeningTicket.getScreening().getHall().getCinema().getId() == cinemaId).count();
        clientReport.addEntry("מספר כרטיסים", numberOfTickets);

        return Collections.singletonList(clientReport);
    }
}