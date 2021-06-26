package org.sertia.contracts.complaints.requests;

import org.sertia.contracts.SertiaBasicRequest;

public class PurchaseCancellationFromComplaintRequest extends SertiaBasicRequest {
    public int complaintId;
    public double refundAmount;
}
