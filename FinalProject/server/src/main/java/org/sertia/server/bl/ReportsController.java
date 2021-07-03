package org.sertia.server.bl;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.reports.response.ClientReportsResponse;
import org.sertia.server.bl.Services.Reportable;

import java.util.Arrays;
import java.util.List;

public class ReportsController {

    private final List<Reportable> reportables;

    public ReportsController(Reportable... reportables) {
        this.reportables = Arrays.asList(reportables);
    }

    public SertiaBasicResponse getSertiaReports() {
        ClientReportsResponse clientReportsResponse = new ClientReportsResponse(true);
        reportables.stream()
                .flatMap(reportable -> reportable.createSertiaReports().stream())
                .forEach(clientReportsResponse::addReport);

        return clientReportsResponse;
    }

    public SertiaBasicResponse getCinemaReports(int cinemaId) {
        ClientReportsResponse clientReportsResponse = new ClientReportsResponse(true);
        reportables.stream()
                .flatMap(reportable -> reportable.createCinemaReports(cinemaId).stream())
                .forEach(clientReportsResponse::addReport);

        return clientReportsResponse;
    }
}