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

	private static ClientComplaintControl instance;

	protected ClientComplaintControl() {
		super();
	}

	public static ClientComplaintControl getInstance() {
		if (instance == null){
			instance = new ClientComplaintControl();
		}
		return instance;
	}

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

	// TODO: after validations return response object with response status or somehing.. UI must know that response 200 returned
	public void createComplaint(String customerName, String customerPhoneNumber, String customerEmail, String description, int ticketId, ClientTicketType ticketType) {
		client.request(new CreateNewComplaintRequest(new ClientOpenComplaint(customerName, customerPhoneNumber, customerEmail, description, ticketId, ticketType)),
				SertiaBasicResponse.class);
	}
}