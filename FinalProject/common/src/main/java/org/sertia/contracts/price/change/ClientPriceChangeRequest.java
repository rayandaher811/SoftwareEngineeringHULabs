package org.sertia.contracts.price.change;

public class ClientPriceChangeRequest {

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