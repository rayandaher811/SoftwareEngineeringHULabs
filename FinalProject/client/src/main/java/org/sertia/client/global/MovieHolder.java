package org.sertia.client.global;

import org.sertia.contracts.movies.catalog.SertiaMovie;

public class MovieHolder {
    private SertiaMovie cinemaScreeningMovie;
    private final static MovieHolder instance = new MovieHolder();
    private boolean isOnlineLinkPurchaseRequest;

    public static MovieHolder getInstance() {
        return instance;
    }

    public void setMovie(SertiaMovie sertiaMovie, boolean isOnlineLinkPurchaseRequest) {
        cinemaScreeningMovie = sertiaMovie;
        this.isOnlineLinkPurchaseRequest = isOnlineLinkPurchaseRequest;
    }

    public SertiaMovie getCinemaScreeningMovie() {
        return cinemaScreeningMovie;
    }

    public boolean isOnlineLinkPurchaseRequest() {
        return isOnlineLinkPurchaseRequest;
    }
}
