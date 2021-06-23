package org.sertia.server.bl.Services;

import org.sertia.contracts.reports.controller.ClientReport;

public interface Reportable {

	ClientReport[] createSertiaReports();

	/**
	 * 
	 * @param cinemaId
	 */
	ClientReport[] createCinemaReports(String cinemaId);

}