package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaClientRequest;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;

public class AddScreeningRequest extends SertiaClientRequest {
    public CinemaScreeningMovie cinemaScreeningMovie;
}
