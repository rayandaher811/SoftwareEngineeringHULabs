package org.sertia.contracts.movies.catalog.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.ClientMovie;

public class GetMovieByIdResponse extends SertiaBasicResponse {
    public ClientMovie movie;
    public GetMovieByIdResponse(boolean isSuccessful) {
        super(isSuccessful);
    }
}
