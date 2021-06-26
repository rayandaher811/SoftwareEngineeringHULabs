package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;
import org.sertia.contracts.movies.catalog.ClientScreening;

public class ScreeningTimeUpdateRequest extends SertiaBasicRequest {
    public ClientScreening screening;

    public ScreeningTimeUpdateRequest(ClientScreening screening) {
        this.screening = screening;
    }
}
