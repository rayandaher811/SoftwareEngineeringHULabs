package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.SertiaBasicRequest;

public class CancelScreeningTicketRequest extends SertiaBasicRequest {
    public int ticketId;

    public CancelScreeningTicketRequest(int ticketId) {
        this.ticketId = ticketId;
    }
}
