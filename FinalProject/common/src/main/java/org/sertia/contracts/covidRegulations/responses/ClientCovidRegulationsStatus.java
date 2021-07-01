package org.sertia.contracts.covidRegulations.responses;

import org.sertia.contracts.SertiaBasicResponse;

public class ClientCovidRegulationsStatus extends SertiaBasicResponse {
    public ClientCovidRegulationsStatus(int maxNumberOfPeople, boolean isActive) {
        super(true);
        this.maxNumberOfPeople = maxNumberOfPeople;
        this.isActive = isActive;
    }

    public ClientCovidRegulationsStatus(boolean isSuccessful) {
        super(isSuccessful);
    }

    public int maxNumberOfPeople;

    public boolean isActive;
}
