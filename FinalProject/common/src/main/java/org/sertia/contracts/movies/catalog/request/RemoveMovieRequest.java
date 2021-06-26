package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;

public class RemoveMovieRequest extends SertiaBasicRequest {
    public int movieId;

    public RemoveMovieRequest(int movieId) {
        this.movieId = movieId;
    }
}
