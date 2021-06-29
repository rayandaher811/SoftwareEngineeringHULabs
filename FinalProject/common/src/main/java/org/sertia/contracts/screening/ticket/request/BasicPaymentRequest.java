package org.sertia.contracts.screening.ticket.request;

import org.joda.time.DateTime;
import org.sertia.contracts.SertiaBasicRequest;

import java.time.LocalDateTime;

public class BasicPaymentRequest extends SertiaBasicRequest {
	public String cardHolderId;
	public String cardHolderName;
	public String creditCardNumber;
	public String cardHolderEmail;
	public String cardHolderPhone;
	public String cvv;
	public LocalDateTime expirationDate;
}