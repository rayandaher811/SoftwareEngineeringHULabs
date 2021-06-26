package org.sertia.contracts.movies.catalog.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;

import java.util.List;

public class CinemaCatalog extends SertiaBasicResponse {
    public List<CinemaScreeningMovie> movies;
}