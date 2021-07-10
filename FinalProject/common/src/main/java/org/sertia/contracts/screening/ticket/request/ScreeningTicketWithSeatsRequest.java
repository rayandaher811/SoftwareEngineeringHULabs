package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.screening.ticket.VoucherDetails;

import java.time.LocalDateTime;
import java.util.List;

public class ScreeningTicketWithSeatsRequest extends ScreeningPaymentRequest {
    public List<Integer> chosenSeats;

    public ScreeningTicketWithSeatsRequest() {
    }

    public ScreeningTicketWithSeatsRequest(int screeningId, VoucherDetails voucherDetails, List<Integer> chosenSeats) {
        super(screeningId, voucherDetails);
        this.chosenSeats = chosenSeats;
    }

    public ScreeningTicketWithSeatsRequest(String cardHolderId, String cardHolderName, String creditCardNumber,
                                           String cardHolderEmail, String cardHolderPhone, String cvv,
                                           LocalDateTime expirationDate, List<Integer> chosenSeats, int screeningId) {
        super(cardHolderId, cardHolderName, creditCardNumber, cardHolderEmail, cardHolderPhone, cvv, expirationDate, screeningId);
        this.chosenSeats = chosenSeats;
        this.screeningId = screeningId;
    }
}
