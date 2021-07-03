package org.sertia.contracts.movies.catalog;

import java.io.Serializable;
import java.util.List;

public class CinemaScreeningMovie implements Serializable {
    public int movieId;
    public ClientMovie movieDetails;
    public List<ClientScreening> screenings;
    public double ticketPrice;

    public CinemaScreeningMovie() {
    }

    public CinemaScreeningMovie(ClientMovie movieDetails, List<ClientScreening> screenings, double ticketPrice) {
        this.movieDetails = movieDetails;
        this.screenings = screenings;
        this.ticketPrice = ticketPrice;
    }

    public ClientMovie getMovieDetails() {
        return movieDetails;
    }

    public List<ClientScreening> getScreenings() {
        return screenings;
    }

    public int getMovieId() {
        return movieId;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }
}