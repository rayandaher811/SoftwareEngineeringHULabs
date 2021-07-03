package org.sertia.server.bl.Services;

import org.sertia.contracts.reports.ClientReport;

import java.util.List;

public interface Reportable {

	List<ClientReport> createSertiaReports();

	/**
	 *
	 * @param cinemaId
	 * @return
	 */
	List<ClientReport> createCinemaReports(String cinemaId);

}