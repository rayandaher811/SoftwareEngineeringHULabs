package org.sertia.server.bl;

import org.sertia.contracts.reports.controller.ClientReport;
import org.sertia.contracts.screening.ticket.*;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.classes.Screening;
import org.sertia.server.dl.classes.ScreeningTicket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
        throw new UnsupportedOperationException();
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
                HallSeat clientHallSeat = new HallSeat();
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
}