package org.sertia.client.controllers;

import org.sertia.contracts.reports.request.GetCinemaReports;
import org.sertia.contracts.reports.request.GetSertiaReports;
import org.sertia.contracts.reports.response.ClientReportsResponse;

public class ClientReportsControl extends ClientControl {

    public ClientReportsResponse getSertiaReports() {
        return client.request(new GetSertiaReports(), ClientReportsResponse.class);
    }

    public ClientReportsResponse getCinemaReports(String cinemaId) {
        return client.request(new GetCinemaReports(), ClientReportsResponse.class);
    }
}