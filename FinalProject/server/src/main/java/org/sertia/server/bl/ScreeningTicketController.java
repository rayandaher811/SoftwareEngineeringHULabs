package org.sertia.server.bl;

import org.sertia.contracts.reports.controller.ClientReport;
import org.sertia.contracts.screening.ticket.*;
import org.sertia.server.bl.Services.Reportable;

public class ScreeningTicketController implements Reportable {

	/**
	 * 
	 * @param parameter
	 * @param seatMap
	 * @param numberOfSeats
	 */
	private HallSeat[] automaticChoseSeats(int parameter, ClientSeatMap seatMap, int numberOfSeats) {
		// TODO - implement ScreeningTicketController.automaticChoseSeats
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param parameter
	 */
	public ClientSeatChooseResponse seatChoseRequest(ClientSeatChooseRequest parameter) {
		// TODO - implement ScreeningTicketController.seatChoseRequest
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param parameter
	 */
	public ScreeningPaymentResponse payForScreeningTicket(ClientPaymentRequest parameter) {
		// TODO - implement ScreeningTicketController.payForScreeningTicket
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param purchaseId
	 */
	public void cancelTicket(int purchaseId) {
		// TODO - implement ScreeningTicketController.cancelTicket
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param paymentDetails
	 */
	public PaymentResponse buyVoucher(ClientPaymentRequest paymentDetails) {
		// TODO - implement ScreeningTicketController.buyVoucher
		throw new UnsupportedOperationException();
	}

	public boolean getAreRegulationsActivated() {
		// TODO - implement ScreeningTicketController.getAreRegulationsActivated
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param areRegulationsActivated
	 */
	public void setAreRegulationsActivated(boolean areRegulationsActivated) {
		// TODO - implement ScreeningTicketController.setAreRegulationsActivated
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param screeningId
	 */
	public void getScreeningSeatMap(ClientSeatMap screeningId) {
		// TODO - implement ScreeningTicketController.getScreeningSeatMap
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param voucherId
	 */
	public void getVoucherBalance(String voucherId) {
		// TODO - implement ScreeningTicketController.getVoucherBalance
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param voucherId
	 */
	public void useVoucher(String voucherId) {
		// TODO - implement ScreeningTicketController.useVoucher
		throw new UnsupportedOperationException();
	}

	@Override
	public ClientReport[] createSertiaReports() {
		return new ClientReport[0];
	}

	@Override
	public ClientReport[] createCinemaReports(String cinemaId) {
		return new ClientReport[0];
	}
}