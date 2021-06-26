package org.sertia.contracts.price.change.request;

import org.sertia.contracts.SertiaClientRequest;
import org.sertia.contracts.price.change.ClientTicketType;

public class ClientPriceChangeRequest extends SertiaClientRequest {

	public int requestId;
	public int movieId;
	public String userName;
	public ClientTicketType clientTicketType;
	public double newPrice;
	public boolean isApproved;

	public ClientPriceChangeRequest(int requestId, int movieId, String userName, ClientTicketType clientTicketType, double newPrice, boolean isApproved) {
		this.requestId = requestId;
		this.movieId = movieId;
		this.userName = userName;
		this.clientTicketType = clientTicketType;
		this.newPrice = newPrice;
		this.isApproved = isApproved;
	}
}