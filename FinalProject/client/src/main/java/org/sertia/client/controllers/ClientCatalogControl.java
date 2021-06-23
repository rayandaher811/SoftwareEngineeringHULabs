package org.sertia.client.controllers;


import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.contracts.movies.catalog.controller.ClientMovie;
import org.sertia.contracts.movies.catalog.controller.ClientScreening;
import org.sertia.contracts.movies.catalog.controller.SertiaCatalog;

public class ClientCatalogControl {

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

	/**
	 * 
	 * @param movie
	 */
	public void createMovie(ClientMovie movie) {
		// TODO - implement ClientCatalogControl.createMovie
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param parameter
	 */
	public void editScreening(ClientScreening parameter) {
		// TODO - implement ClientCatalogControl.editScreening
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param parameter
	 */
	public void addScreening(ClientScreening parameter) {
		// TODO - implement ClientCatalogControl.addScreening
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param screeningId
	 */
	public void removeScreening(String screeningId) {
		// TODO - implement ClientCatalogControl.removeScreening
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param streaming
	 */
//	public void addStreaming(ClientStreaming streaming) {
//		// TODO - implement ClientCatalogControl.addStreaming
//		throw new UnsupportedOperationException();
//	}

	/**
	 * 
	 * @param streaming
	 */
	public void removeStreaming(String streaming) {
		// TODO - implement ClientCatalogControl.removeStreaming
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param catalog
	 */
	public void onSpecificCatalogReceive(MoviesCatalog catalog) {
		// TODO - implement ClientCatalogControl.onSpecificCatalogReceive
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param catalog
	 */
	public void onAllMoviesCatalogReceive(SertiaCatalog catalog) {
		// TODO - implement ClientCatalogControl.onAllMoviesCatalogReceive
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param movieId
	 */
	public void removeMovie(String movieId) {
		// TODO - implement ClientCatalogControl.removeMovie
		throw new UnsupportedOperationException();
	}

}