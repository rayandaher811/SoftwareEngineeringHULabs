package org.sertia.server.bl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.covidRegulations.responses.ClientCovidRegulationsStatus;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.contracts.screening.ticket.HallSeat;
import org.sertia.contracts.screening.ticket.request.*;
import org.sertia.contracts.screening.ticket.response.*;
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

    public SertiaBasicResponse buyTicketWithRegulations(ScreeningTicketWithCovidRequest request) {
        ClientSeatMapResponse seatMapForScreening = getSeatMapForScreening(request.screeningId);
        ScreeningPaymentResponse response = new ScreeningPaymentResponse(true);

        try {
            int numberOfWantedSeats = request.numberOfSeats;
            Optional<Screening> optionalScreening = DbUtils.getById(Screening.class, request.screeningId);
            if (!optionalScreening.isPresent()) {
                response.isSuccessful = false;
                response.failReason = "screening doesn't exist";

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

    public SertiaBasicResponse buyTicketWithSeatChose(ScreeningTicketWithSeatsRequest request) {
        if (!isPaymentRequestValid(request)) {
            ScreeningPaymentResponse paymentResponse = new ScreeningPaymentResponse(false);
            paymentResponse.failReason = "Payment details are invalid";

            return paymentResponse;
        }

        return purchaseTicketsForScreening(request);
    }

    public SertiaBasicResponse cancelTicket(CancelScreeningTicketRequest request) {
        TicketCancellationResponse response = new TicketCancellationResponse(true);
        return DbUtils.getById(ScreeningTicket.class, request.ticketId)
                .map(screeningTicket -> {
                    if (!screeningTicket.getPaymentInfo().getPayerId().equals(request.userId)) {
                        return null;
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
                }).orElse(
                        Utils.createFailureResponse(response, "הכרטיס אינו קיים"));
    }

    public SertiaBasicResponse getSeatMapForScreening(GetScreeningSeatMap request) {
        return getSeatMapForScreening(request.screeningId);
    }

    public SertiaBasicResponse buyVoucher(BasicPaymentRequest paymentDetails) {
        VouchersInfo vouchersInfo = DbUtils.getById(VouchersInfo.class, VouchersInfo.singleRecordId).orElse(null);
        VoucherPaymentResponse response = new VoucherPaymentResponse(true);
        if (vouchersInfo == null) {
            response.isSuccessful = false;
            response.failReason = "sertia has no voucher info, contact support";

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
            response.failReason = "couldn't purchase voucher, contact support";

            return response;
        }
    }

    public SertiaBasicResponse getVoucherBalance(VoucherBalanceRequest request) {
        VoucherBalanceResponse response = new VoucherBalanceResponse(true);
        return DbUtils.getById(TicketsVoucher.class, request.voucherId).map(ticketsVoucher -> {
            response.balance = ticketsVoucher.getTicketsBalance();

            return response;
        }).orElseGet(() -> {
            response.isSuccessful = false;
            response.setFailReason("voucher doesn't exist");

            return response;
        });
    }

    public SertiaBasicResponse useVoucher(UseVoucherRequest request) {
        return DbUtils.getById(TicketsVoucher.class, request.voucherId).map(ticketsVoucher -> {
            try (Session session = HibernateSessionFactory.getInstance().openSession()) {
                if (ticketsVoucher.getTicketsBalance() == 1) {
                    session.delete(ticketsVoucher);
                }

                ticketsVoucher.setTicketsBalance(ticketsVoucher.getTicketsBalance() - 1);
                session.save(ticketsVoucher);
                return new SertiaBasicResponse(true);
            } catch (RuntimeException exception) {
                return new SertiaBasicResponse(false).setFailReason("failed to update voucher");
            }
        }).orElseGet(() -> {
            SertiaBasicResponse response = new SertiaBasicResponse(false);
            response.setFailReason("voucher doesn't exist");
            return response;
        });
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

    private SertiaBasicResponse purchaseTicketsForScreening(ScreeningTicketWithSeatsRequest request) {
        ScreeningPaymentResponse paymentResponse = new ScreeningPaymentResponse(true);
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
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

            CustomerPaymentDetails paymentDetails = getPaymentDetails(request);
            session.saveOrUpdate(paymentDetails);
            for (ScreeningTicket screeningTicket : screeningTickets) {
                screeningTicket.setPaymentInfo(paymentDetails);
                int ticketId = (int) session.save(screeningTicket);
                org.sertia.server.dl.classes.HallSeat seat = screeningTicket.getSeat();
                HallSeat purchasedSeat = new HallSeat(seat.getId());
                purchasedSeat.isTaken = true;
                purchasedSeat.row = seat.getRowNumber();
                purchasedSeat.numberInRow = seat.getNumberInRow();
                paymentResponse.addTicket(ticketId, purchasedSeat);
            }
        } catch (RuntimeException exception) {
            paymentResponse.isSuccessful = false;
            paymentResponse.failReason = "Problem during purchase process";

            return paymentResponse;
        }

        CustomerNotifier.getInstance().notify(request.cardHolderEmail, getScreeningMail(paymentResponse));
        return paymentResponse;
    }

    private String getScreeningMail(ScreeningPaymentResponse response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nסרט: ").append(response.movieName)
                .append("\nקולנוע: ").append(response.cinemaName)
                .append("\nאולם: ").append(response.hallNumber)
                .append("\nשעת הקרנה: ").append(response.screeningTime)
                .append("\n")
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

        return (int) Math.floorDiv(hallCapacity, 2);
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
            response.setFailReason("screening doesn't exist");
            return response;
        });
    }

    private ScreeningTicket createScreeningTicket(int hallSeat, Screening screening, Session session) {
        ScreeningTicket ticket = new ScreeningTicket();
        org.sertia.server.dl.classes.HallSeat requestedSeat =
                DbUtils.getById(org.sertia.server.dl.classes.HallSeat.class, hallSeat)
                        .orElseThrow(() -> new IllegalArgumentException("seat doesn't exist"));

        if (!isSeatFree(screening.getId(), hallSeat, session)) {
            throw new IllegalArgumentException("seat isn't free");
        }

        ticket.setScreening(screening);
        ticket.setVoucher(false);
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

        throw new IllegalArgumentException("can't choose seats for desired screening");
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