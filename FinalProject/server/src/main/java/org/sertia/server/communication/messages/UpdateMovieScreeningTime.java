package org.sertia.server.communication.messages;

public class UpdateMovieScreeningTime {
    private BaseClientServerMessage metadata;
    private CinemaScreeningMovie currentMovie;
    private String newDateTimeStampAsString;
    private final String messageName = "UPDATE_SCREENING_REQ";

    public UpdateMovieScreeningTime(String clientId, CinemaScreeningMovie currentMovie, String newDateTimeStampAsString) {
        this.metadata = new BaseClientServerMessage(clientId);
        this.currentMovie = currentMovie;
        this.newDateTimeStampAsString = newDateTimeStampAsString;
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

    public String getNewDateTimeStampAsString() {
        return newDateTimeStampAsString;
    }

    public void setNewDateTimeStampAsString(String newDateTimeStampAsString) {
        this.newDateTimeStampAsString = newDateTimeStampAsString;
    }
}
