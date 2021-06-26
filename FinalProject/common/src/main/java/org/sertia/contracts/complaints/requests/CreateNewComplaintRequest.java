package org.sertia.contracts.complaints.requests;

import org.sertia.contracts.SertiaClientRequest;
import org.sertia.contracts.complaints.ClientOpenComplaint;

public class CreateNewComplaintRequest extends SertiaClientRequest {
    public ClientOpenComplaint complaint;
}
