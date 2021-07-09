package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;

import java.time.LocalDateTime;

public class AddScreeningRequest extends SertiaBasicRequest {
    public int movieId;
    public int cinemaId;
    public int hallId;
    public LocalDateTime screeningTime;

    public AddScreeningRequest(int movieId, int hallId, LocalDateTime screeningTime, int cinemaId) {
        this.movieId = movieId;
        this.hallId = hallId;
        this.screeningTime = screeningTime;
        this.cinemaId = cinemaId;
    }
}
