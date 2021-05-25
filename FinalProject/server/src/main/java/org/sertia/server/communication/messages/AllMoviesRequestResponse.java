package org.sertia.server.communication.messages;

public class AllMoviesRequestResponse {
    private AllMoviesRequestMsg originReq;
    private MoviesCatalog moviesCatalog;
    private String originRequestId;

    public AllMoviesRequestResponse(AllMoviesRequestMsg originReq, MoviesCatalog moviesCatalog) {
        this.originReq = originReq;
        this.moviesCatalog = moviesCatalog;
        this.originRequestId = this.originReq.getMessageId();
    }

    public AllMoviesRequestResponse() {
    }

    public AllMoviesRequestMsg getOriginReq() {
        return originReq;
    }

    public MoviesCatalog getMoviesCatalog() {
        return moviesCatalog;
    }

    public String getOriginRequestId() {
        return originRequestId;
    }
}
