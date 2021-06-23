package org.sertia.client.controllers;

import org.sertia.contracts.price.change.ClientPriceChangeRequest;
import org.sertia.contracts.screening.ticket.ClientSeatChooseRequest;
import org.sertia.contracts.screening.ticket.ClientSeatChooseResponse;
import org.sertia.contracts.screening.ticket.ClientSeatMap;

public class ClientPurchaseControl {

	private String purchaseSessionId = null;

	/**
	 * 
	 * @param screeningId
	 */
	public void getScreeningSeatMap(String screeningId) {
		// TODO - implement ClientPurchaseControl.getScreeningSeatMap
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param seatMap
	 */
	public void onSeatMapResponse(ClientSeatMap seatMap) {
		// TODO - implement ClientPurchaseControl.onSeatMapResponse
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param request
	 */
	public void chooseSeats(ClientSeatChooseRequest request) {
		// TODO - implement ClientPurchaseControl.chooseSeats
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param res
	 */
	public void onChooseSeatResponse(ClientSeatChooseResponse res) {
		// TODO - implement ClientPurchaseControl.onChooseSeatResponse
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param ClientPaymentRequest
	 */
	public void sendPaymentForScreening(int ClientPaymentRequest) {
		// TODO - implement ClientPurchaseControl.sendPaymentForScreening
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param ClientPaymentResponse
	 */
	public void onPaymentResponse(int ClientPaymentResponse) {
		// TODO - implement ClientPurchaseControl.onPaymentResponse
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param purcahseId
	 */
	public void cancelScreeningTicket(int purcahseId) {
		// TODO - implement ClientPurchaseControl.cancelScreeningTicket
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param ClientStreamingPaymentRequest
	 */
	public void purchaseStreaming(int ClientStreamingPaymentRequest) {
		// TODO - implement ClientPurchaseControl.purchaseStreaming
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param purchaseId
	 */
	public void cancelStreamingTicket(int purchaseId) {
		// TODO - implement ClientPurchaseControl.cancelStreamingTicket
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param payment
	 */
	public void purchaseVoucher(ClientPriceChangeRequest payment) {
		// TODO - implement ClientPurchaseControl.purchaseVoucher
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param voucherId
	 */
	public void requestVoucherBalance(String voucherId) {
		// TODO - implement ClientPurchaseControl.requestVoucherBalance
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param balance
	 */
	public void onVoucherBalanceReceive(int balance) {
		// TODO - implement ClientPurchaseControl.onVoucherBalanceReceive
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param voucherId
	 */
	public void useVoucher(String voucherId) {
		// TODO - implement ClientPurchaseControl.useVoucher
		throw new UnsupportedOperationException();
	}

}