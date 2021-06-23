package org.sertia.server.bl;

import org.sertia.contracts.price.change.ClientPriceChangeRequest;
import org.sertia.server.dl.classes.PriceChangeRequest;

public class PriceChangeController {

	/**
	 * 
	 * @param priceChangeRequest
	 */
	public void requestPriceChange(PriceChangeRequest priceChangeRequest) {
		// TODO - implement PriceChangeController.requestPriceChange
		throw new UnsupportedOperationException();
	}

	public PriceChangeRequest[] getUnapprovedRequests() {
		// TODO - implement PriceChangeController.getUnapprovedRequests
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param priceChangeRequestId
	 */
	public void approveRequest(String priceChangeRequestId) {
		// TODO - implement PriceChangeController.approveRequest
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param priceChangeRequest
	 */
	private boolean isPriceChangeRequestValid(ClientPriceChangeRequest priceChangeRequest) {
		// TODO - implement PriceChangeController.isPriceChangeRequestValid
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param priceChangeRequestId
	 */
	public void disapprovePriceChangeRequest(String priceChangeRequestId) {
		// TODO - implement PriceChangeController.disapprovePriceChangeRequest
		throw new UnsupportedOperationException();
	}

}