package org.sertia.client.communication.messages;

import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.util.Collection;
import java.util.List;

public class MoviesCatalog {
    public List<SertiaMovie> movies;

    public MoviesCatalog(List<SertiaMovie> sertiaMovieList) {
        this.movies = sertiaMovieList;
    }

    public Collection<SertiaMovie> getMoviesCatalog() {
        return movies;
    }
}
