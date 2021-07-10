package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.screening.ticket.VoucherDetails;

import java.time.LocalDateTime;

public class ScreeningTicketWithCovidRequest extends ScreeningPaymentRequest {
    public int numberOfSeats;

    public ScreeningTicketWithCovidRequest(int screeningId, VoucherDetails voucherDetails, int numberOfSeats) {
        super(screeningId, voucherDetails);
        this.numberOfSeats = numberOfSeats;
    }

    public ScreeningTicketWithCovidRequest(String cardHolderId, String cardHolderName, String creditCardNumber, String cardHolderEmail, String cardHolderPhone, String cvv, LocalDateTime expirationDate, int numberOfSeats, int screeningId) {
        super(cardHolderId, cardHolderName, creditCardNumber, cardHolderEmail, cardHolderPhone, cvv, expirationDate, screeningId);
        this.screeningId = screeningId;
        this.numberOfSeats = numberOfSeats;
    }
}