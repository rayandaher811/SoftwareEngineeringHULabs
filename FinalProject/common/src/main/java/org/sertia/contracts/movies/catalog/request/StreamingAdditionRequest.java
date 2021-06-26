package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaClientRequest;

public class StreamingAdditionRequest extends SertiaClientRequest {
    public int movieId;
    public double pricePerStream;
}
