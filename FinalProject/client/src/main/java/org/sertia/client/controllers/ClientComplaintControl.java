package org.sertia.client.controllers;

import org.sertia.client.communication.SertiaClient;
import org.sertia.contracts.complaints.ClientOpenComplaint;
import org.sertia.contracts.complaints.requests.CloseComplaintRequest;
import org.sertia.contracts.complaints.requests.CreateNewComplaintRequest;
import org.sertia.contracts.complaints.requests.GetAllUnhandledComplaintsRequest;
import org.sertia.contracts.complaints.requests.PurchaseCancellationFromComplaintRequest;
import org.sertia.contracts.complaints.responses.AllUnhandledComplaintsResponse;
import org.sertia.contracts.price.change.ClientTicketType;
import org.sertia.contracts.price.change.request.ApprovePriceChangeRequest;

import java.util.List;

public class ClientComplaintControl {

	private SertiaClient client;

	public ClientComplaintControl() {
		client = SertiaClient.getInstance();
	}

	public boolean tryResolveComplaint(int complaintId, double refundAmount) {
		return client.request(new PurchaseCancellationFromComplaintRequest(complaintId, refundAmount)).isSuccessful;
	}

	public boolean tryCloseComplaint(int complaintId) {
		return client.request(new CloseComplaintRequest(complaintId)).isSuccessful;
	}

	public List<ClientOpenComplaint> getOpenedComplaints() {
		AllUnhandledComplaintsResponse response =  client.request(new GetAllUnhandledComplaintsRequest());

		return response.openComplaints;
	}

	public void createComplaint(String customerName, String customerPhoneNumber, String customerEmail, String description, int ticketId, ClientTicketType ticketType) {
		client.request(new CreateNewComplaintRequest(new ClientOpenComplaint(customerName, customerPhoneNumber, customerEmail, description, ticketId, ticketType)));
	}
}