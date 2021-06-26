package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;
import org.sertia.contracts.movies.catalog.SertiaMovie;

public class AddMovieRequest extends SertiaBasicRequest {
    public SertiaMovie sertiaMovie;
}
