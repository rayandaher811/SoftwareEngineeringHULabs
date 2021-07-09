package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;

public class TicketCancellationResponse extends SertiaBasicResponse {
    public double refundAmount;

    public TicketCancellationResponse(boolean isSuccessful) {
        this(isSuccessful, 0);
    }

    public TicketCancellationResponse(boolean isSuccessful, double refundAmount) {
        super(isSuccessful);
        this.refundAmount = refundAmount;
    }
}
