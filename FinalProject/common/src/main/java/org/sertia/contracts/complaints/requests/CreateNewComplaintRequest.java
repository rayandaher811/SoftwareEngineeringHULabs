package org.sertia.contracts.complaints.requests;

import org.sertia.contracts.complaints.ClientOpenComplaint;
import org.sertia.contracts.movies.catalog.request.SertiaCatalogRequest;

public class CreateNewComplaintRequest extends SertiaCatalogRequest {
    public ClientOpenComplaint complaint;

    public CreateNewComplaintRequest(ClientOpenComplaint complaint) {
        this.complaint = complaint;
    }
}
