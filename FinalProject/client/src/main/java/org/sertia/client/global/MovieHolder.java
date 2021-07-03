package org.sertia.client.global;

import org.sertia.contracts.movies.catalog.ClientMovie;

public class MovieHolder {
    private ClientMovie movie;
    private final static MovieHolder instance = new MovieHolder();
    private boolean isOnlineLinkPurchaseRequest;

    public static MovieHolder getInstance() {
        return instance;
    }

    public void setMovie(ClientMovie sertiaMovie, boolean isOnlineLinkPurchaseRequest) {
        movie = sertiaMovie;
        this.isOnlineLinkPurchaseRequest = isOnlineLinkPurchaseRequest;
    }

    public ClientMovie getMovie() {
        return movie;
    }

    public boolean isOnlineLinkPurchaseRequest() {
        return isOnlineLinkPurchaseRequest;
    }
}
