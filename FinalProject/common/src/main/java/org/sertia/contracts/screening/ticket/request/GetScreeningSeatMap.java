package org.sertia.contracts.screening.ticket.request;

import org.sertia.contracts.SertiaBasicRequest;

public class GetScreeningSeatMap extends SertiaBasicRequest {
    public int screeningId;

    public GetScreeningSeatMap(int screeningId) {
        this.screeningId = screeningId;
    }
}
