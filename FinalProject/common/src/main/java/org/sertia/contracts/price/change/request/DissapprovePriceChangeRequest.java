package org.sertia.contracts.price.change.request;

import org.sertia.contracts.SertiaBasicRequest;

public class DissapprovePriceChangeRequest extends SertiaBasicRequest {
    public int priceChangeRequestId;

    public DissapprovePriceChangeRequest(int priceChangeRequestId) {
        this.priceChangeRequestId = priceChangeRequestId;
    }
}
