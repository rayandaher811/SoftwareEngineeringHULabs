package org.sertia.server.bl;

import org.sertia.contracts.complaints.controller.ClientOpenComplaint;
import org.sertia.contracts.reports.controller.ClientReport;
import org.sertia.server.bl.Services.Reportable;

public class ComplaintsController implements Reportable {

	/**
	 * 
	 * @param complaint
	 */
	public void createNewComplaint(ClientOpenComplaint complaint) {
		// TODO - implement ComplaintsController.createNewComplaint
		throw new UnsupportedOperationException();
	}

	public void getAllComplaints() {
		// TODO - implement ComplaintsController.getAllComplaints
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param complaintId
	 */
	public void closeComplaint(String complaintId) {
		// TODO - implement ComplaintsController.closeComplaint
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param complaintId
	 */
	public void cancelPurchaseFromComplaint(int complaintId) {
		// TODO - implement ComplaintsController.cancelPurchaseFromComplaint
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