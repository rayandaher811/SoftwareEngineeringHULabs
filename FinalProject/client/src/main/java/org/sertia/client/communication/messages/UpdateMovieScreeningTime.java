package org.sertia.client.communication.messages;

import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;

public class UpdateMovieScreeningTime {
    private BaseClientServerMessage metadata;
    private CinemaScreeningMovie currentMovie;
    private String newDateTimeAsString;
    private final String messageName = "UPDATE_SCREENING_REQ";

    public UpdateMovieScreeningTime(String clientId, CinemaScreeningMovie currentMovie, String newDateTimeAsString) {
        this.metadata = new BaseClientServerMessage(clientId);
        this.currentMovie = currentMovie;
        this.newDateTimeAsString = newDateTimeAsString;
    }

    public String getMessageId() {
        return metadata.getMessageId();
    }

    public CinemaScreeningMovie getCurrentMovie() {
        return currentMovie;
    }

    public void setCurrentMovie(CinemaScreeningMovie currentMovie) {
        this.currentMovie = currentMovie;
    }

    public String getNewDateTimeAsString() {
        return newDateTimeAsString;
    }

    public void setNewDateTimeAsString(String newDateTimeAsString) {
        this.newDateTimeAsString = newDateTimeAsString;
    }
}
