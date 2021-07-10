package org.sertia.contracts.screening.ticket.request;

import java.time.LocalDateTime;

public class StreamingPaymentRequest extends BasicPaymentRequest {
    public int movieId;
    public LocalDateTime startTime;
    public int extraDays;

    public StreamingPaymentRequest(String cardHolderId,
                                   String cardHolderName,
                                   String creditCardNumber,
                                   String cardHolderEmail,
                                   String cardHolderPhone,
                                   String cvv,
                                   LocalDateTime expirationDate,
                                   int movieId,
                                   LocalDateTime startTime,
                                   int extraDays) {
        super(cardHolderId, cardHolderName, creditCardNumber, cardHolderEmail, cardHolderPhone, cvv, expirationDate);
        this.movieId = movieId;
        this.startTime = startTime;
        this.extraDays = extraDays;
    }
}