package org.sertia.client.controllers;

import org.sertia.client.communication.SertiaClient;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.request.StreamingAdditionRequest;
import org.sertia.contracts.price.change.ClientTicketType;
import org.sertia.contracts.price.change.request.ApprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.contracts.price.change.request.DissapprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.GetUnapprovedPriceChangeRequest;
import org.sertia.contracts.price.change.responses.GetUnapprovedPriceChangeResponse;

import java.util.List;

public class ClientPriceChangeControl {

	private SertiaClient client;

	public ClientPriceChangeControl() {
		client = SertiaClient.getInstance();
	}

	public void requestPriceChange(int movieId, ClientTicketType clientTicketType, double newPrice) {
		client.request(new BasicPriceChangeRequest(movieId, clientTicketType, newPrice), SertiaBasicResponse.class);
	}

	public boolean tryApprovePriceChange(int requestId) {
		return client.request(new ApprovePriceChangeRequest(requestId), SertiaBasicResponse.class).isSuccessful;
	}

	public boolean tryDisapprovePriceChange(int requestId) {
		return client.request(new DissapprovePriceChangeRequest(requestId), SertiaBasicResponse.class).isSuccessful;
	}

	public List<BasicPriceChangeRequest> getAllOpenedPriceChangeRequests() {
		GetUnapprovedPriceChangeResponse response = client.request(new GetUnapprovedPriceChangeRequest(), GetUnapprovedPriceChangeResponse.class);
		return response.unapprovedRequests;
	}

}