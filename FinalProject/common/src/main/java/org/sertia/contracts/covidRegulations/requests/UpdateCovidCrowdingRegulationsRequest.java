package org.sertia.contracts.covidRegulations.requests;

import org.sertia.contracts.SertiaBasicRequest;

public class UpdateCovidCrowdingRegulationsRequest extends SertiaBasicRequest {
    public int newMaxNumberOfPeople;

    public UpdateCovidCrowdingRegulationsRequest(int newMaxNumberOfPeople) {
        this.newMaxNumberOfPeople = newMaxNumberOfPeople;
    }
}
