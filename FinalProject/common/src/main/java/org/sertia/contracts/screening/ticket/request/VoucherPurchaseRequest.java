package org.sertia.contracts.screening.ticket.request;

import java.time.LocalDateTime;

public class VoucherPurchaseRequest extends BasicPaymentRequest {
    public VoucherPurchaseRequest() {
    }

    public VoucherPurchaseRequest(String cardHolderId, String cardHolderName, String creditCardNumber, String cardHolderEmail, String cardHolderPhone, String cvv, LocalDateTime expirationDate) {
        super(cardHolderId, cardHolderName, creditCardNumber, cardHolderEmail, cardHolderPhone, cvv, expirationDate);
    }
}
