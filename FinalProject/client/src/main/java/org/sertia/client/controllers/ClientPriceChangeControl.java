package org.sertia.client.controllers;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.price.change.ClientTicketType;
import org.sertia.contracts.price.change.request.ApprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.contracts.price.change.request.DissapprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.GetUnapprovedPriceChangeRequest;
import org.sertia.contracts.price.change.responses.GetUnapprovedPriceChangeResponse;

import java.util.List;

public class ClientPriceChangeControl extends ClientControl {

	public SertiaBasicResponse requestPriceChange(int movieId, ClientTicketType clientTicketType, double newPrice) {
		return client.request(new BasicPriceChangeRequest(movieId, clientTicketType, newPrice), SertiaBasicResponse.class);
	}

	public SertiaBasicResponse tryApprovePriceChange(int requestId) {
		return client.request(new ApprovePriceChangeRequest(requestId), SertiaBasicResponse.class);
	}

	public SertiaBasicResponse tryDisapprovePriceChange(int requestId) {
		return client.request(new DissapprovePriceChangeRequest(requestId), SertiaBasicResponse.class);
	}

	public GetUnapprovedPriceChangeResponse getAllOpenedPriceChangeRequests() {
		return client.request(new GetUnapprovedPriceChangeRequest(), GetUnapprovedPriceChangeResponse.class);
	}

}