package org.sertia.contracts.screening.ticket;

import org.joda.time.DateTime;

public class ClientPaymentRequest {
	public String cardHolderId;
	public String cardHolderName;
	public String creditCardNumber;
	public String cardHolderEmail;
	public String cardHolderPhone;
	public String cvv;
	public DateTime expirationDate;
}