package org.sertia.contracts.reports.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.reports.ClientReport;

import java.util.ArrayList;
import java.util.List;

public class ClientReportsResponse extends SertiaBasicResponse {
    public List<ClientReport> reports;

    public ClientReportsResponse(boolean isSuccessful) {
        super(isSuccessful);
        reports = new ArrayList<>();
    }

    public void addReport(ClientReport clientReport) {
        reports.add(clientReport);
    }
}
