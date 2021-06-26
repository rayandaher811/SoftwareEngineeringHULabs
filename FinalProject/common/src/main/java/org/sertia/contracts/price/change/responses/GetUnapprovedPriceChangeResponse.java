package org.sertia.contracts.price.change.responses;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;

import java.util.List;

public class GetUnapprovedPriceChangeResponse extends SertiaBasicResponse {
    public List<BasicPriceChangeRequest> unapprovedRequests;

    public GetUnapprovedPriceChangeResponse(boolean isSuccessful) {
        super(isSuccessful);
    }
}
