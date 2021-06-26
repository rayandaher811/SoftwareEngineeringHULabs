package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaClientRequest;
import org.sertia.contracts.movies.catalog.SertiaMovie;

public class AddMovieRequest extends SertiaClientRequest {
    public SertiaMovie sertiaMovie;
}
