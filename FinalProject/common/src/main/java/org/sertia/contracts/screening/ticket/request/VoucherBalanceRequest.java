package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.SertiaBasicRequest;

public class VoucherBalanceRequest extends SertiaBasicRequest {
    public int voucherId;
    public String clientId;

    public VoucherBalanceRequest(int voucherId, String clientId) {
        this.voucherId = voucherId;
        this.clientId = clientId;
    }
}
