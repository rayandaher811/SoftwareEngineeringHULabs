package org.sertia.client.global;

import org.sertia.contracts.movies.catalog.ClientMovie;

public class MovieHolder {
    private ClientMovie movie;
    private final static MovieHolder instance = new MovieHolder();

    public static MovieHolder getInstance() {
        return instance;
    }

    public void setMovie(ClientMovie sertiaMovie) {
        movie = sertiaMovie;
    }

    public ClientMovie getMovie() {
        return movie;
    }
}
