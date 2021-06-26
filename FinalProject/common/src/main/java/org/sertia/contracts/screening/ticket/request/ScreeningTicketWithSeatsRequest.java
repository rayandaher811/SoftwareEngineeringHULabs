package org.sertia.contracts.screening.ticket.request;

import java.util.List;

public class ScreeningTicketWithSeatsRequest extends BasicPaymentRequest {
    public List<Integer> chosenSeats;
    public int screeningId;
}
