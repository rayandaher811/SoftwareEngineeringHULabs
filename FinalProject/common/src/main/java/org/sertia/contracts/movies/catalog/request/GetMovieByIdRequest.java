package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;

public class GetMovieByIdRequest extends SertiaBasicRequest {
    public int movieId;

    public GetMovieByIdRequest(int movieId) {
        this.movieId = movieId;
    }
}
