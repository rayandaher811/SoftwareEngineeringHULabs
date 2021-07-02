package org.sertia.client.controllers;


import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.request.*;
import org.sertia.contracts.movies.catalog.response.CinemaCatalogResponse;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;

import java.time.LocalDateTime;
import java.util.List;

public class ClientCatalogControl extends ClientControl {

    private static ClientCatalogControl instance = null;

    private ClientCatalogControl(){

    }

    public static ClientCatalogControl getInstance() {
        if (instance == null)
            instance = new ClientCatalogControl();

        return instance;
    }

    public List<CinemaScreeningMovie> requestSpecificCinemaCatalog(int cinemaId) {
        return client.request(new CinemaCatalogRequest(cinemaId), CinemaCatalogResponse.class).movies;
    }

    public List<SertiaMovie> requestAllMoviesCatalog() {
        return client.request(new SertiaCatalogRequest(), SertiaCatalogResponse.class).movies;
    }

    public boolean tryCreateMovie(SertiaMovie movie) {
        return client.request(new AddMovieRequest(movie), SertiaBasicResponse.class).isSuccessful;
    }

    public boolean tryRemoveMovie(int movieId) {
        return client.request(new RemoveMovieRequest(movieId), SertiaBasicResponse.class).isSuccessful;
    }

    public boolean tryUpdateScreeningTime(ClientScreening screening) {
        return client.request(new ScreeningTimeUpdateRequest(screening), SertiaBasicResponse.class).isSuccessful;
    }

    public boolean tryAddScreening(int movieId, LocalDateTime screeningTime, int hallId) {
        return tryAddScreening(movieId, screeningTime, 0, hallId);
    }

    public boolean tryAddScreening(int movieId, LocalDateTime screeningTime, double price, int hallId) {
        return client.request(new AddScreeningRequest(movieId,hallId, screeningTime, price), SertiaBasicResponse.class).isSuccessful;
    }

    public boolean tryRemoveScreening(int screeningId) {
        return client.request(new RemoveScreeningRequest(screeningId), SertiaBasicResponse.class).isSuccessful;
    }

    public boolean tryAddStreaming(int movieId, double pricePerStream) {
        return client.request(new StreamingAdditionRequest(movieId, pricePerStream), SertiaBasicResponse.class).isSuccessful;
    }

    public boolean tryRemoveStreaming(int movieId) {
        return client.request(new StreamingRemovalRequest(movieId), SertiaBasicResponse.class).isSuccessful;
    }
}