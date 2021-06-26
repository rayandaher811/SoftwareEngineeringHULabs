package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;

public class StreamingPaymentResponse extends SertiaBasicResponse {
	public String streamingLink;

	public StreamingPaymentResponse(boolean isSuccessful) {
		super(isSuccessful);
	}
}