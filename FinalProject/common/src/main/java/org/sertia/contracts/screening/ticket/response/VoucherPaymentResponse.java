package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;

public class VoucherPaymentResponse extends SertiaBasicResponse {

    public VoucherPaymentResponse(boolean isSuccessful) {
        super(isSuccessful);
    }

    public VoucherPaymentResponse setVoucherId(int voucherId) {
        this.voucherId = voucherId;
        return this;
    }

    public int voucherId;
}
