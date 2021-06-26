package org.sertia.contracts.price.change.request;

import org.sertia.contracts.SertiaBasicRequest;

public class ApprovePriceChangeRequest extends SertiaBasicRequest {
    public int priceChangeRequestId;

    public ApprovePriceChangeRequest(int priceChangeRequestId) {
        this.priceChangeRequestId = priceChangeRequestId;
    }
}
