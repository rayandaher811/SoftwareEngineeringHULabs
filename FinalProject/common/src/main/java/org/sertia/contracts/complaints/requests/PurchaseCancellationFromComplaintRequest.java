package org.sertia.contracts.complaints.requests;

import org.sertia.contracts.SertiaClientRequest;

public class PurchaseCancellationFromComplaintRequest extends SertiaClientRequest {
    public int complaintId;
    public double refundAmount;
}
