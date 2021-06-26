package org.sertia.client.controllers;


import org.sertia.client.communication.SertiaClient;
import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.request.*;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;

public class ClientCatalogControl {

	private SertiaClient client;

	public ClientCatalogControl() {
		client = SertiaClient.getInstance();
	}

	/**
	 * 
	 * @param cinemaId
	 */
	public void requestSpecificCinemaCatalog(int cinemaId) {
		// TODO - implement ClientCatalogControl.requestSpecificCinemaCatalog
		throw new UnsupportedOperationException();
	}

	public void requestAllMoviesCatalog() {
		// TODO - implement ClientCatalogControl.requestAllMoviesCatalog
		throw new UnsupportedOperationException();
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

	public boolean tryAddScreening(CinemaScreeningMovie parameter) {
		return client.request(new AddScreeningRequest(parameter), SertiaBasicResponse.class).isSuccessful;
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