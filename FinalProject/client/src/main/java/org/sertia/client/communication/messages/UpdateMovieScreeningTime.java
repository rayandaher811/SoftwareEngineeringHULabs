package org.sertia.client.communication.messages;

import java.util.Date;

public class UpdateMovieScreeningTime {
    private BaseClientServerMessage metadata;
    private CinemaScreeningMovie currentMovie;
    private Date newDateTime;
    private final String messageName = "UPDATE_MOVIE_SCREENING_TIME";

    public UpdateMovieScreeningTime(String clientId, CinemaScreeningMovie currentMovie, Date newDateTime) {
        this.metadata = new BaseClientServerMessage(clientId);
        this.currentMovie = currentMovie;
        this.newDateTime = newDateTime;
    }

    public String getMessageId() {
        return metadata.getMessageId();
    }
}
