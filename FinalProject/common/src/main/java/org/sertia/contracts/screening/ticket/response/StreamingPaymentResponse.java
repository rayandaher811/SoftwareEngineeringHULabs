package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;

import java.time.LocalDateTime;

public class StreamingPaymentResponse extends SertiaBasicResponse {
	public int purchaseId;
	public String streamingLink;
	public double price;
	public LocalDateTime startTime;
	public LocalDateTime endTime;

	public StreamingPaymentResponse(boolean isSuccessful) {
		super(isSuccessful);
	}
}