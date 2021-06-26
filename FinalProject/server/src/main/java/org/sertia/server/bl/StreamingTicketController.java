package org.sertia.server.bl;

import org.sertia.contracts.reports.ClientReport;
import org.sertia.contracts.screening.ticket.request.StreamingPaymentRequest;
import org.sertia.contracts.screening.ticket.response.StreamingPaymentResponse;
import org.sertia.server.bl.Services.Reportable;

public class StreamingTicketController implements Reportable {

	/**
	 * 
	 * @param parameter
	 */
	public StreamingPaymentResponse purchaseStreamingTicket(StreamingPaymentRequest parameter) {
		// TODO - implement StreamingTicketController.purchaseStreamingTicket
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param purchaseId
	 */
	public void cancelStreamingTicket(String purchaseId) {
		// TODO - implement StreamingTicketController.cancelStreamingTicket
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