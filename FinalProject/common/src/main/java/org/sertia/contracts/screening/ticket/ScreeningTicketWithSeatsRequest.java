package org.sertia.contracts.screening.ticket;

import java.util.List;

public class ScreeningTicketWithSeatsRequest extends ClientPaymentRequest{
    public List<HallSeat> chosenSeats;
    public int screeningId;
}
