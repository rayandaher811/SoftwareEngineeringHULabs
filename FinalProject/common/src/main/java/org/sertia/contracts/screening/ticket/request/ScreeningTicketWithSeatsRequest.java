package org.sertia.contracts.screening.ticket.request;

import java.time.LocalDateTime;
import java.util.List;

public class ScreeningTicketWithSeatsRequest extends BasicPaymentRequest {
    public List<Integer> chosenSeats;
    public int screeningId;

    public ScreeningTicketWithSeatsRequest(List<Integer> chosenSeats, int screeningId) {
        this.chosenSeats = chosenSeats;
        this.screeningId = screeningId;
    }

    public ScreeningTicketWithSeatsRequest() {
    }

    public ScreeningTicketWithSeatsRequest(String cardHolderId, String cardHolderName, String creditCardNumber,
                                           String cardHolderEmail, String cardHolderPhone, String cvv,
                                           LocalDateTime expirationDate, List<Integer> chosenSeats, int screeningId) {
        super(cardHolderId, cardHolderName, creditCardNumber, cardHolderEmail, cardHolderPhone, cvv, expirationDate);
        this.chosenSeats = chosenSeats;
        this.screeningId = screeningId;
    }
}
