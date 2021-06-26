package org.sertia.contracts.complaints.requests;

import org.sertia.contracts.SertiaBasicRequest;

public class PurchaseCancellationFromComplaintRequest extends SertiaBasicRequest {
    public int complaintId;
    public double refundAmount;

    public PurchaseCancellationFromComplaintRequest(int complaintId, double refundAmount) {
        this.complaintId = complaintId;
        this.refundAmount = refundAmount;
    }
}
