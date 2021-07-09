package org.sertia.contracts;

public class PurchaseRelatedRequest extends SertiaBasicRequest {
    public String userId;

    public PurchaseRelatedRequest(String userId) {
        this.userId = userId;
    }
}
