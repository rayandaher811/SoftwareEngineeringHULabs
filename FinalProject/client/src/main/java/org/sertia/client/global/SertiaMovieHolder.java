package org.sertia.client.global;

import org.sertia.contracts.movies.catalog.SertiaMovie;

public class SertiaMovieHolder {
    private SertiaMovie sertiaMovie;
    private final static SertiaMovieHolder instance = new SertiaMovieHolder();

    public static SertiaMovieHolder getInstance() {
        return instance;
    }

    public void setSertiaMovie(SertiaMovie sertiaMovie) {
        sertiaMovie = sertiaMovie;
    }

    public SertiaMovie getSertiaMovie() {
        return sertiaMovie;
    }
}
