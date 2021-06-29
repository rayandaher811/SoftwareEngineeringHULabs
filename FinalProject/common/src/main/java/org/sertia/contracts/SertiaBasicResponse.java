package org.sertia.contracts;

import java.io.Serializable;

public class SertiaBasicResponse implements Serializable {
    public boolean isSuccessful;
    public String failReason;

    public SertiaBasicResponse(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public SertiaBasicResponse(boolean isSuccessful, String failReason) {
        this.isSuccessful = isSuccessful;
        this.failReason = failReason;
    }

    public SertiaBasicResponse setSuccessful(boolean successful) {
        isSuccessful = successful;
        return this;
    }

    public SertiaBasicResponse setFailReason(String failReason) {
        this.failReason = failReason;
        return this;
    }
}
