package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.SertiaBasicRequest;

public class CancelStreamingTicketRequest extends SertiaBasicRequest {
    public int streamingId;

    public CancelStreamingTicketRequest(int streamingId) {
        this.streamingId = streamingId;
    }
}
