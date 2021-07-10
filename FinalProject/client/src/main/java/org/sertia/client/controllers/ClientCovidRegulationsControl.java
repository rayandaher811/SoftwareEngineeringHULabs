package org.sertia.client.controllers;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.covidRegulations.requests.*;
import org.sertia.contracts.covidRegulations.responses.ClientCovidRegulationsStatus;

import java.time.LocalDateTime;

public class ClientCovidRegulationsControl extends ClientControl {

    private static final ClientCovidRegulationsControl instance = new ClientCovidRegulationsControl();

    public static ClientCovidRegulationsControl getInstance() {
        return instance;
    }

    public ClientCovidRegulationsStatus getCovidRegulationsStatus() {
        return client.request(new GetCovidRegulationsStatusRequest(), ClientCovidRegulationsStatus.class);
    }

    public SertiaBasicResponse cancelRegulations() {
        return client.request(new CancelCovidRegulationsRequest(), SertiaBasicResponse.class);
    }

    public SertiaBasicResponse activeRegulations() {
        return client.request(new ActiveCovidRegulationsRequest(), SertiaBasicResponse.class);
    }

    public SertiaBasicResponse cancelAllScreeningsDueCovid(LocalDateTime cancellationStartDate, LocalDateTime cancellationEndDate) {
        return client.request(new CancelAllScreeningsDueCovidRequest(cancellationStartDate, cancellationEndDate), SertiaBasicResponse.class);
    }

    public SertiaBasicResponse updateCovidCrowdingRegulationsRequest(int newMaxNumberOfPeople) {
        return client.request(new UpdateCovidCrowdingRegulationsRequest(newMaxNumberOfPeople), SertiaBasicResponse.class);
    }
}