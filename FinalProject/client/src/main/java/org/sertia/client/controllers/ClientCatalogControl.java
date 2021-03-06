package org.sertia.client.controllers;


import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.request.*;
import org.sertia.contracts.movies.catalog.response.CinemaAndHallsResponse;
import org.sertia.contracts.movies.catalog.response.GetMovieByIdResponse;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;
import org.sertia.contracts.screening.ticket.request.GetStreamingByLinkRequest;
import org.sertia.contracts.screening.ticket.response.GetStreamingByLinkResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClientCatalogControl extends ClientControl {

    private static ClientCatalogControl instance = null;

    private ClientCatalogControl() {

    }

    public static ClientCatalogControl getInstance() {
        if (instance == null)
            instance = new ClientCatalogControl();

        return instance;
    }

    public SertiaBasicResponse tryCreateMovie(SertiaMovie movie) {
        return client.request(new AddMovieRequest(movie), SertiaBasicResponse.class);
    }

    public SertiaBasicResponse tryRemoveMovie(int movieId) {
        return client.request(new RemoveMovieRequest(movieId), SertiaBasicResponse.class);
    }

    public SertiaBasicResponse tryUpdateScreeningTime(ClientScreening screening) {
        return client.request(new ScreeningTimeUpdateRequest(screening), SertiaBasicResponse.class);
    }

    public SertiaBasicResponse tryAddScreening(int movieId, LocalDateTime screeningTime, int hallId, int cinemaId) {
        return client.request(new AddScreeningRequest(movieId, hallId, screeningTime, cinemaId), SertiaBasicResponse.class);
    }

    public SertiaBasicResponse tryRemoveScreening(int screeningId) {
        return client.request(new RemoveScreeningRequest(screeningId), SertiaBasicResponse.class);
    }

    public SertiaBasicResponse tryAddStreaming(int movieId, double pricePerStream) {
        return client.request(new StreamingAdditionRequest(movieId, pricePerStream), SertiaBasicResponse.class);
    }

    public SertiaBasicResponse tryRemoveStreaming(int movieId) {
        return client.request(new StreamingRemovalRequest(movieId), SertiaBasicResponse.class);
    }

    public SertiaCatalogResponse requestAllMoviesCatalog() {
        SertiaCatalogResponse response = client.request(new SertiaCatalogRequest(), SertiaCatalogResponse.class);
        return response;
    }

    public GetMovieByIdResponse requestMovieById(int movieId) {
        GetMovieByIdResponse response = client.request(new GetMovieByIdRequest(movieId), GetMovieByIdResponse.class);
        return response;
    }

    public CinemaAndHallsResponse getCinemasAndHalls() {
        return client.request(new RequestCinemasAndHalls(), CinemaAndHallsResponse.class);
    }

    public GetStreamingByLinkResponse getStreamingByLink(String link){
        return client.request(new GetStreamingByLinkRequest(link), GetStreamingByLinkResponse.class);
    }
}