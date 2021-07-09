package org.sertia.client.global;

import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;

public class MovieHolder {
    private CinemaScreeningMovie cinemaScreeningMovie;
    private final static MovieHolder instance = new MovieHolder();
    private boolean isOnlineLinkPurchaseRequest;

    public static MovieHolder getInstance() {
        return instance;
    }

    public void setMovie(CinemaScreeningMovie sertiaMovie, boolean isOnlineLinkPurchaseRequest) {
        cinemaScreeningMovie = sertiaMovie;
        this.isOnlineLinkPurchaseRequest = isOnlineLinkPurchaseRequest;
    }

    public CinemaScreeningMovie getCinemaScreeningMovie() {
        return cinemaScreeningMovie;
    }

    public boolean isOnlineLinkPurchaseRequest() {
        return isOnlineLinkPurchaseRequest;
    }
}
