package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;

public class VoucherPaymentResponse extends SertiaBasicResponse {

    public int voucherId;

    public VoucherPaymentResponse(boolean isSuccessful) {
        super(isSuccessful);
    }

    public VoucherPaymentResponse setVoucherId(int voucherId) {
        this.voucherId = voucherId;
        return this;
    }


}
