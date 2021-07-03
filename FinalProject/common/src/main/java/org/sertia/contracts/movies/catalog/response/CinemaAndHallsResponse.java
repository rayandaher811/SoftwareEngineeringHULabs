package org.sertia.contracts.movies.catalog.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.ClientHall;

import java.util.List;
import java.util.Map;

public class CinemaAndHallsResponse extends SertiaBasicResponse {
    public Map<String, List<ClientHall>> cinemaToHalls;

    public CinemaAndHallsResponse(boolean isSuccessful, Map<String, List<ClientHall>> cinemaToHalls) {
        super(isSuccessful);
        this.cinemaToHalls = cinemaToHalls;
    }
}
