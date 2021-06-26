package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;

public class AddScreeningRequest extends SertiaBasicRequest {
    public CinemaScreeningMovie cinemaScreeningMovie;
}
