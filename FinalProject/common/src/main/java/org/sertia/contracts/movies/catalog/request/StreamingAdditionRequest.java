package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;

public class StreamingAdditionRequest extends SertiaBasicRequest {
    public int movieId;
    public double pricePerStream;
}
