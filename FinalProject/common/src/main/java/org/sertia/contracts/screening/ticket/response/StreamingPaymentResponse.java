package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;

import java.time.LocalDateTime;

public class StreamingPaymentResponse extends SertiaBasicResponse {
	public String streamingLink;
	public int price;
	public LocalDateTime startTime;
	public LocalDateTime endTime;

	public StreamingPaymentResponse(boolean isSuccessful) {
		super(isSuccessful);
	}
}