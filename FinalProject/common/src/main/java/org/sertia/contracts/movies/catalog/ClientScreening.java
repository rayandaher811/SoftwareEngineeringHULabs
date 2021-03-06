package org.sertia.contracts.movies.catalog;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ClientScreening implements Serializable {
    public int screeningId;
    public int hallId;
    public LocalDateTime screeningTime;
    public String cinemaName;

    public String getCinemaName() {
        return cinemaName;
    }

    public int getScreeningId() {
        return screeningId;
    }

    public int getHallId() {
        return hallId;
    }

    public LocalDateTime getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(LocalDateTime screeningTime) {
        this.screeningTime = screeningTime;
    }
}