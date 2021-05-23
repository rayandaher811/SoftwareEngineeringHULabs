package org.sertia.client.communication.messages;

import org.joda.time.DateTime;
import org.sertia.client.pojos.ScreeningMovie;

public class UpdateMovieScreeningTime {
    private BaseClientServerMessage metadata;
    private ScreeningMovie currentMovie;
    private DateTime newDateTime;

    public UpdateMovieScreeningTime(String clientId, ScreeningMovie currentMovie, DateTime newDateTime) {
        this.metadata = new BaseClientServerMessage(clientId);
        this.currentMovie = currentMovie;
        this.newDateTime = newDateTime;
    }

    public String getMessageId() {
        return metadata.getMessageId();
    }
}
