package org.sertia.contracts.screening.ticket.request;

import java.time.LocalDateTime;

public class ScreeningTicketWithCovidRequest extends BasicPaymentRequest {
    public int screeningId;
    public int numberOfSeats;

    public ScreeningTicketWithCovidRequest(String cardHolderId, String cardHolderName, String creditCardNumber, String cardHolderEmail, String cardHolderPhone, String cvv, LocalDateTime expirationDate) {
        super(cardHolderId, cardHolderName, creditCardNumber, cardHolderEmail, cardHolderPhone, cvv, expirationDate);
    }
}