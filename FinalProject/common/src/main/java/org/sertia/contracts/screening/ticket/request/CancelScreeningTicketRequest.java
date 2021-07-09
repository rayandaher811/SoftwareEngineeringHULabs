package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.PurchaseRelatedRequest;

public class CancelScreeningTicketRequest extends PurchaseRelatedRequest {
    public int ticketId;


    public CancelScreeningTicketRequest(int ticketId, String userId) {
        super(userId);
        this.ticketId = ticketId;
    }
}
