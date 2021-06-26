package org.sertia.contracts.movies.catalog.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.util.List;

public class SertiaCatalogResponse extends SertiaBasicResponse {
    public SertiaCatalogResponse(boolean isSuccessful, List<SertiaMovie> movies) {
        super(isSuccessful);
        this.movies = movies;
    }

    public List<SertiaMovie> movies;
}