package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.screening.ticket.VoucherDetails;

import java.time.LocalDateTime;

public abstract class ScreeningPaymentRequest extends BasicPaymentRequest {
    public int screeningId;
    public boolean isVoucher;
    public VoucherDetails voucherDetails;

    public ScreeningPaymentRequest() {
    }

    public ScreeningPaymentRequest(int screeningId, VoucherDetails voucherDetails) {
        this.voucherDetails = voucherDetails;
        this.screeningId = screeningId;
        isVoucher = true;
    }

    public ScreeningPaymentRequest(String cardHolderId, String cardHolderName, String creditCardNumber, String cardHolderEmail, String cardHolderPhone, String cvv, LocalDateTime expirationDate, int screeningId) {
        super(cardHolderId, cardHolderName, creditCardNumber, cardHolderEmail, cardHolderPhone, cvv, expirationDate);
        this.screeningId = screeningId;
    }

    public boolean isUsingVoucher() {
        return isVoucher;
    }
}
