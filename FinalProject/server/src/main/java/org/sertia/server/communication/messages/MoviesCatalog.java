package org.sertia.server.communication.messages;

import java.util.Collection;
import java.util.Collections;

public class MoviesCatalog {
    private Collection<CinemaScreeningMovie> moviesCatalog;

    public MoviesCatalog(Collection<CinemaScreeningMovie> moviesCatalog) {
        this.moviesCatalog = moviesCatalog;
    }

    public MoviesCatalog() {
        this.moviesCatalog = Collections.emptyList();
    }

    public void addMovie(CinemaScreeningMovie cinemaScreeningMovie){
        moviesCatalog.add(cinemaScreeningMovie);
    }
}
