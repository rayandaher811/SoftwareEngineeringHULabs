package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.PurchaseRelatedRequest;

public class CancelStreamingTicketRequest extends PurchaseRelatedRequest {
    public int streamingId;

    public CancelStreamingTicketRequest(int streamingId, String userId) {
        super(userId);
        this.streamingId = streamingId;
    }
}
