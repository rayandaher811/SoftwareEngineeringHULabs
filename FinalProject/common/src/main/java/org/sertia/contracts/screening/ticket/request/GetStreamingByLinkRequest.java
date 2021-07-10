package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.SertiaBasicRequest;

public class GetStreamingByLinkRequest extends SertiaBasicRequest {
    public String link;

    public GetStreamingByLinkRequest(String link) {
        this.link = link;
    }
}
