package org.sertia.server.bl.Services;


import org.sertia.server.dl.classes.CustomerPaymentDetails;

public class CreditCardService {

	/**
	 * 
	 * @param creditDetails
	 * @param amount
	 */
	public void chargeCredit(ClientCreditDetails creditDetails, int amount) {
		// TODO - implement CreditCardService.chargeCredit
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param creditDetails
	 * @param amound
	 */
	public void refund(ClientCreditDetails creditDetails, int amound) {
		// TODO - implement CreditCardService.refund
		throw new UnsupportedOperationException();
	}

	public void refund(CustomerPaymentDetails customerPaymentDetails, double amound) {
		// TODO - implement CreditCardService.refund
		throw new UnsupportedOperationException();
	}

}