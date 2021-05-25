package org.sertia.server.communication.messages;

import java.util.LinkedList;

public class MoviesCatalog {
    private LinkedList<CinemaScreeningMovie> moviesCatalog;

    public MoviesCatalog(LinkedList<CinemaScreeningMovie> moviesCatalog) {
        this.moviesCatalog = moviesCatalog;
    }

    public MoviesCatalog() {
        this.moviesCatalog = new LinkedList<>();
    }

    public void addMovie(CinemaScreeningMovie cinemaScreeningMovie) {
        moviesCatalog.add(cinemaScreeningMovie);
    }
}
