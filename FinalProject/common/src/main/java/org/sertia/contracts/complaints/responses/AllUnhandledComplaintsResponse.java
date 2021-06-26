package org.sertia.contracts.complaints.responses;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.complaints.ClientOpenComplaint;

import java.util.List;

public class AllUnhandledComplaintsResponse extends SertiaBasicResponse {
    public List<ClientOpenComplaint> openComplaints;

    public AllUnhandledComplaintsResponse(boolean isSuccessful) {
        super(isSuccessful);
    }
}
