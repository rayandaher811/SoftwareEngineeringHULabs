package org.sertia.server.bl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.sertia.contracts.reports.controller.ClientReport;
import org.sertia.contracts.screening.ticket.*;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.CustomerPaymentDetails;
import org.sertia.server.dl.classes.PaymentMethod;
import org.sertia.server.dl.classes.Screening;
import org.sertia.server.dl.classes.ScreeningTicket;

import java.util.*;
import java.util.stream.Collectors;

public class ScreeningTicketController implements Reportable {

    private final CovidRegulationsController covidRegulationsController;

    public ScreeningTicketController(CovidRegulationsController covidRegulationsController) {
        this.covidRegulationsController = covidRegulationsController;
    }

    /**
     * @param parameter
     * @param seatMap
     * @param numberOfSeats
     */
    private HallSeat[] automaticChoseSeats(int parameter, ClientSeatMap seatMap, int numberOfSeats) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param
     */
    public PaymentResponse buyTicketWithRegulations(ScreeningTicketWithCovidRequest request) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param
     */
    public PaymentResponse buyTicketWithSeatChose(ScreeningTicketWithSeatsRequest request) {
        if (!isPaymentRequestValid(request)) {
            return new PaymentResponse()
                    .setFailReason("Payment details are invalid")
                    .setSuccessful(false);
        }

        ScreeningPaymentResponse paymentResponse = new ScreeningPaymentResponse();
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            Set<ScreeningTicket> screeningTickets = DbUtils.getById(Screening.class, request.screeningId)
                    .map(screening -> request.chosenSeats.stream()
                            .map(hallSeat -> createScreeningTicket(hallSeat, screening, session))
                            .collect(Collectors.toSet()))
                    .orElse(Collections.emptySet());

            CustomerPaymentDetails paymentDetails = getPaymentDetails(request);
            session.save(paymentDetails);
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

            paymentResponse.setSuccessful(true);
        } catch (RuntimeException exception) {
            return new PaymentResponse()
                    .setFailReason("Problem during purchase process")
                    .setSuccessful(false);
        }

        return paymentResponse;
    }

    public ClientSeatMap getSeatMapForScreening(int screeningId) {
        final List<HallSeat> hallSeatList = new ArrayList<>();
        DbUtils.getById(Screening.class, screeningId).ifPresent(screening ->
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
        });

        return new ClientSeatMap(hallSeatList);
    }

    /**
     * @param purchaseId
     */
    public void cancelTicket(int purchaseId) {
        // TODO - implement ScreeningTicketController.cancelTicket
        throw new UnsupportedOperationException();
    }

    /**
     * @param paymentDetails
     */
    public PaymentResponse buyVoucher(ClientPaymentRequest paymentDetails) {
        // TODO - implement ScreeningTicketController.buyVoucher
        throw new UnsupportedOperationException();
    }

    public boolean getAreRegulationsActivated() {
        // TODO - implement ScreeningTicketController.getAreRegulationsActivated
        throw new UnsupportedOperationException();
    }

    /**
     * @param screeningId
     */
    public void getScreeningSeatMap(ClientSeatMap screeningId) {
        // TODO - implement ScreeningTicketController.getScreeningSeatMap
        throw new UnsupportedOperationException();
    }

    /**
     * @param voucherId
     */
    public void getVoucherBalance(String voucherId) {
        // TODO - implement ScreeningTicketController.getVoucherBalance
        throw new UnsupportedOperationException();
    }

    /**
     * @param voucherId
     */
    public void useVoucher(String voucherId) {
        // TODO - implement ScreeningTicketController.useVoucher
        throw new UnsupportedOperationException();
    }

    @Override
    public ClientReport[] createSertiaReports() {
        return new ClientReport[0];
    }

    @Override
    public ClientReport[] createCinemaReports(String cinemaId) {
        return new ClientReport[0];
    }

    private boolean isPaymentRequestValid(ClientPaymentRequest clientPaymentRequest) {
        return true;
    }

    private ScreeningTicket createScreeningTicket(HallSeat hallSeat, Screening screening, Session session) {
        ScreeningTicket ticket = new ScreeningTicket();
        org.sertia.server.dl.classes.HallSeat requestedSeat =
                DbUtils.getById(org.sertia.server.dl.classes.HallSeat.class, hallSeat.id)
                        .orElseThrow(() -> new IllegalArgumentException("seat doesn't exist"));

        if (!isSeatFree(screening.getId(), hallSeat.id, session)) {
            throw new IllegalArgumentException("seat isn't free");
        }

        ticket.setScreening(screening);
        ticket.setVoucher(false);
        ticket.setPaidPrice(screening.getScreenableMovie().getTicketPrice());
        ticket.setSeat(requestedSeat);
        return ticket;
    }

    private boolean isSeatFree(int screeningId, int seatId, Session session) {
        Query query =
                session
                        .createQuery("from ScreeningTicket where seat_seat_id = :seat and screening_screening_id = :screening");
        query.setParameter("seat", seatId);
        query.setParameter("screening", screeningId);
        return query.list().isEmpty();
    }

    private CustomerPaymentDetails getPaymentDetails(ClientPaymentRequest paymentRequest) {
        CustomerPaymentDetails paymentDetails = new CustomerPaymentDetails();
        paymentDetails.setPayerId(paymentRequest.cardHolderId);
        paymentDetails.setFullName(paymentRequest.cardHolderName);
        paymentDetails.setExperationDate(new Date(paymentRequest.expirationDate.getMillis()));
        paymentDetails.setPaymentMethod(PaymentMethod.Credit);
        paymentDetails.setCreditNumber(paymentRequest.creditCardNumber);
        paymentDetails.setCvv(paymentRequest.cvv);
        paymentDetails.setEmail(paymentRequest.cardHolderEmail);
        paymentDetails.setPhoneNumber(paymentRequest.cardHolderPhone);

        return paymentDetails;
    }
}