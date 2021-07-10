package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;

public class GetStreamingByLinkResponse extends SertiaBasicResponse {
    public String movieName;

    public GetStreamingByLinkResponse(boolean isSuccessful) {
        super(isSuccessful);
    }
}
