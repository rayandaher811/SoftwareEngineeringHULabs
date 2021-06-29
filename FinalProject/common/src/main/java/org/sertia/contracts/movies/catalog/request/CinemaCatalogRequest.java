package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;

public class CinemaCatalogRequest extends SertiaBasicRequest {
    public CinemaCatalogRequest(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    public int cinemaId;
}
