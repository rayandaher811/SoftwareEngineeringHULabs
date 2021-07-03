package org.sertia.client.global;

import org.sertia.contracts.movies.catalog.ClientScreening;

public class ScreeningHolder {
    private ClientScreening screening;

    private final static ScreeningHolder instance = new ScreeningHolder();

    public static ScreeningHolder getInstance() {
        return instance;
    }

    public void setScreening(ClientScreening sertiaMovie) {
        screening = sertiaMovie;
    }

    public ClientScreening getScreening() {
        return screening;
    }
}
