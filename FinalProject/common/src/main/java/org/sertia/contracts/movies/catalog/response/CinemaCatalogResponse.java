package org.sertia.contracts.movies.catalog.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;

import java.util.ArrayList;
import java.util.List;

public class CinemaCatalogResponse extends SertiaBasicResponse {
    public List<CinemaScreeningMovie> movies;

    public CinemaCatalogResponse(boolean isSuccessful) {
        super(isSuccessful);
        this.movies = new ArrayList<>();
    }
}