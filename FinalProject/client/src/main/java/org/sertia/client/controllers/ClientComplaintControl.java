package org.sertia.client.controllers;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.complaints.ClientOpenComplaint;
import org.sertia.contracts.complaints.requests.CloseComplaintRequest;
import org.sertia.contracts.complaints.requests.CreateNewComplaintRequest;
import org.sertia.contracts.complaints.requests.GetAllUnhandledComplaintsRequest;
import org.sertia.contracts.complaints.requests.PurchaseCancellationFromComplaintRequest;
import org.sertia.contracts.complaints.responses.AllUnhandledComplaintsResponse;
import org.sertia.contracts.price.change.ClientTicketType;

import java.util.List;

public class ClientComplaintControl extends ClientControl {

	public boolean tryResolveComplaint(int complaintId, double refundAmount) {
		return client.request(new PurchaseCancellationFromComplaintRequest(complaintId, refundAmount), SertiaBasicResponse.class).isSuccessful;
	}

	public boolean tryCloseComplaint(int complaintId) {
		return client.request(new CloseComplaintRequest(complaintId), SertiaBasicResponse.class).isSuccessful;
	}

	public List<ClientOpenComplaint> getOpenedComplaints() {
		AllUnhandledComplaintsResponse response = client.request(new GetAllUnhandledComplaintsRequest(), AllUnhandledComplaintsResponse.class);

		return response.openComplaints;
	}

	public void createComplaint(String customerName, String customerPhoneNumber, String customerEmail, String description, int ticketId, ClientTicketType ticketType) {
		client.request(new CreateNewComplaintRequest(new ClientOpenComplaint(customerName, customerPhoneNumber, customerEmail, description, ticketId, ticketType)),
				SertiaBasicResponse.class);
	}
}