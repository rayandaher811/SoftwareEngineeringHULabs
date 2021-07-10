package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.screening.ticket.HallSeat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ScreeningPaymentResponse extends SertiaBasicResponse {
    public Map<Integer, HallSeat> ticketIdToSeat;
    public double finalPrice;
    public String movieName;
    public String cinemaName;
    public LocalDateTime screeningTime;
    public int hallNumber;
    public boolean isVoucher;
    public int voucherBalance;

    public ScreeningPaymentResponse(boolean isSuccessful) {
        super(isSuccessful);
        ticketIdToSeat = new HashMap<>();
    }

    public void addTicket(int ticketId, HallSeat hallSeat) {
        ticketIdToSeat.put(ticketId, hallSeat);
    }
}
