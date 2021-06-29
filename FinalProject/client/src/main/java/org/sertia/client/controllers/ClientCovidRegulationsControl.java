package org.sertia.client.controllers;

import org.sertia.contracts.movies.catalog.ClientScreening;

public class ClientCovidRegulationsControl extends ClientControl {

    private int areRegulationsActive;

    public int areRegulationsActive() {
        return this.areRegulationsActive;
    }

    /**
     * @param ClientCovidRegulation
     */
    public void sendNewRegulations(int ClientCovidRegulation) {
        // TODO - implement ClientCovidRegulationsControl.sendNewRegulations
        throw new UnsupportedOperationException();
    }

    public void cancelRegulations() {
        // TODO - implement ClientCovidRegulationsControl.cancelRegulations
        throw new UnsupportedOperationException();
    }

    public void getProblematicScreenings() {
        // TODO - implement ClientCovidRegulationsControl.getProblematicScreenings
        throw new UnsupportedOperationException();
    }

    /**
     * @param screenings
     */
    public void onProblematicScreeningsReponse(ClientScreening[] screenings) {
        // TODO - implement ClientCovidRegulationsControl.onProblematicScreeningsReponse
        throw new UnsupportedOperationException();
    }

    public void areRegulationsActivated() {
        // TODO - implement ClientCovidRegulationsControl.areRegulationsActivated
        throw new UnsupportedOperationException();
    }

    public void onRegulationsReceive() {
        // TODO - implement ClientCovidRegulationsControl.onRegulationsReceive
        throw new UnsupportedOperationException();
    }

}