package org.sertia.server.communication.messages;

import org.sertia.server.pojos.ScreeningMovie;

import java.util.Collection;

public class AllMoviesRequestResponse {
    private AllMoviesRequestMsg originReq;
    private Collection<ScreeningMovie> screeningMovieCollection;
    private String originRequestId;

    public AllMoviesRequestResponse(AllMoviesRequestMsg originReq, Collection<ScreeningMovie> screeningMovieCollection) {
        this.originReq = originReq;
        this.screeningMovieCollection = screeningMovieCollection;
        this.originRequestId = this.originReq.getMessageId();
    }

    public AllMoviesRequestResponse() {
    }

    public AllMoviesRequestMsg getOriginReq() {
        return originReq;
    }

    public Collection<ScreeningMovie> getScreeningMovieCollection() {
        return screeningMovieCollection;
    }
}
