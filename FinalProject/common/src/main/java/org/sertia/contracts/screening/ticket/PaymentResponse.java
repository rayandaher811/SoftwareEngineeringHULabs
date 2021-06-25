package org.sertia.contracts.screening.ticket;

public class PaymentResponse {
	public boolean isSuccessful;
	public String failReason;

	public PaymentResponse setSuccessful(boolean successful) {
		isSuccessful = successful;
		return this;
	}

	public PaymentResponse setFailReason(String failReason) {
		this.failReason = failReason;
		return this;
	}
}