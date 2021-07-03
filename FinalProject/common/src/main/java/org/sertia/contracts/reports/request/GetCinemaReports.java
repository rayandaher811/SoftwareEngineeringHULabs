package org.sertia.contracts.reports.request;

import org.sertia.contracts.SertiaBasicRequest;

public class GetCinemaReports extends SertiaBasicRequest {
    public int cinemaId;

    public GetCinemaReports(int cinemaId) {
        this.cinemaId = cinemaId;
    }
}
