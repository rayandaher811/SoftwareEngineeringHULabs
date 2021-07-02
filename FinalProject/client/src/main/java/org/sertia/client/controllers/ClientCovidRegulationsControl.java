package org.sertia.client.controllers;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.covidRegulations.requests.*;
import org.sertia.contracts.covidRegulations.responses.ClientCovidRegulationsStatus;

import java.time.LocalDateTime;

public class ClientCovidRegulationsControl extends ClientControl {
    public boolean areRegulationsActive() {
        return client.request(new GetCovidRegulationsStatusRequest(), ClientCovidRegulationsStatus.class).isActive;
    }

    public ClientCovidRegulationsStatus getCovidRegulationsStatus() {
        return client.request(new GetCovidRegulationsStatusRequest(), ClientCovidRegulationsStatus.class);
    }

    public void cancelRegulations() {
        client.request(new CancelCovidRegulationsRequest(), SertiaBasicResponse.class);
    }

    public void activeRegulations() {
        client.request(new ActiveCovidRegulationsRequest(), SertiaBasicResponse.class);
    }

    public void cancelAllScreeningsDueCovid(LocalDateTime cancellationStartDate, LocalDateTime cancellationEndDate) {
        client.request(new CancelAllScreeningsDueCovidRequest(cancellationStartDate, cancellationEndDate), SertiaBasicResponse.class);
    }

    public void UpdateCovidCrowdingRegulationsRequest(int newMaxNumberOfPeople) {
        client.request(new UpdateCovidCrowdingRegulationsRequest(newMaxNumberOfPeople), SertiaBasicResponse.class);
    }

}