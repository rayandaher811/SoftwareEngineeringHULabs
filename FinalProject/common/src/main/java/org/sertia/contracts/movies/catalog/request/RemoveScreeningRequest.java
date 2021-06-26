package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;

public class RemoveScreeningRequest extends SertiaBasicRequest {
    public int screeningId;

    public RemoveScreeningRequest(int screeningId) {
        this.screeningId = screeningId;
    }
}
