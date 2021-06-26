package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;

public class VoucherBalanceResponse extends SertiaBasicResponse {
    public int balance;

    public VoucherBalanceResponse(boolean isSuccessful) {
        super(isSuccessful);
    }
}
