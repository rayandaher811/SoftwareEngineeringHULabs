package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;

public class GetVoucherInfoResponse extends SertiaBasicResponse {
    public int initialBalance;
    public double price;

    public GetVoucherInfoResponse(boolean isSuccessful) {
        super(isSuccessful);
    }
}
