package org.sertia.contracts.movies.catalog.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.util.List;

public class SertiaCatalog extends SertiaBasicResponse {
    public SertiaCatalog(List<SertiaMovie> movies) {
        this.movies = movies;
    }

    public List<SertiaMovie> movies;
}