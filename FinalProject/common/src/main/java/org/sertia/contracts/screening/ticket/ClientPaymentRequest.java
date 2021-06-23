package org.sertia.contracts.screening.ticket;

import org.joda.time.DateTime;

public class ClientPaymentRequest {
	public String cardHolderName;
	public String creditCardNumber;
	public String cvv;
	public DateTime expirationDate;
}