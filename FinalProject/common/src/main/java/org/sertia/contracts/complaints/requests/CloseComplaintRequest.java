package org.sertia.contracts.complaints.requests;

import org.sertia.contracts.SertiaBasicRequest;

public class CloseComplaintRequest extends SertiaBasicRequest {
    public int complaintId;

    public CloseComplaintRequest(int complaintId) {
        this.complaintId = complaintId;
    }
}
