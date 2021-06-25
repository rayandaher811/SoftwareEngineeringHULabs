package org.sertia.contracts.screening.ticket;

import java.util.HashMap;
import java.util.Map;

public class ScreeningPaymentResponse extends PaymentResponse {
    Map<Integer, HallSeat> ticketIdToSeat;

    public ScreeningPaymentResponse() {
        ticketIdToSeat = new HashMap<>();
    }

    public void addTicket(int ticketId, HallSeat hallSeat) {
        ticketIdToSeat.put(ticketId, hallSeat);
    }
}
