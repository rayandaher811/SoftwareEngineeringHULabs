package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;

import java.time.LocalDateTime;

public class AddScreeningRequest extends SertiaBasicRequest {
    public int movieId;
    public int hallId;
    public LocalDateTime screeningTime;
    public double price;

    public AddScreeningRequest(int movieId, int hallId, LocalDateTime screeningTime, double price) {
        this.movieId = movieId;
        this.hallId = hallId;
        this.screeningTime = screeningTime;
        this.price = price;
    }
}
