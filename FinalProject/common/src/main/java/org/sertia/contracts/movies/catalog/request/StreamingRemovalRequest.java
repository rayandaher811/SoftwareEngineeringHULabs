package org.sertia.contracts.movies.catalog.request;

import org.sertia.contracts.SertiaBasicRequest;

public class StreamingRemovalRequest extends SertiaBasicRequest {
    public int streamingId;

    public StreamingRemovalRequest(int streamingId) {
        this.streamingId = streamingId;
    }
}
