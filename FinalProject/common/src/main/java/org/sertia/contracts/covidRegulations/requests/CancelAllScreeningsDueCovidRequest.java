package org.sertia.contracts.covidRegulations.requests;

import org.sertia.contracts.SertiaBasicRequest;

import java.time.LocalDateTime;

public class CancelAllScreeningsDueCovidRequest extends SertiaBasicRequest {
    public LocalDateTime cancellationStartDate;
    public LocalDateTime cancellationEndDate;

    public CancelAllScreeningsDueCovidRequest(LocalDateTime cancellationStartDate, LocalDateTime cancellationEndDate) {
        this.cancellationStartDate = cancellationStartDate;
        this.cancellationEndDate = cancellationEndDate;
    }
}
