package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.SertiaBasicRequest;

public class VoucherBalanceRequest extends SertiaBasicRequest {
    public int voucherId;

    public VoucherBalanceRequest(int voucherId) {
        this.voucherId = voucherId;
    }
}
