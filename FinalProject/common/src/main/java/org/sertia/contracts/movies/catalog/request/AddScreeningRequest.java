package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;

import java.time.LocalDateTime;

public class AddScreeningRequest extends SertiaBasicRequest {
    public int movieId;
    public int cinemaId;
    public int hallNumber;
    public LocalDateTime screeningTime;

    public AddScreeningRequest(int movieId, int hallNumber, LocalDateTime screeningTime, int cinemaId) {
        this.movieId = movieId;
        this.hallNumber = hallNumber;
        this.screeningTime = screeningTime;
        this.cinemaId = cinemaId;
    }
}
