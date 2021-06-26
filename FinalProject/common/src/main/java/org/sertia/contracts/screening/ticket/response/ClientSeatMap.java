package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.screening.ticket.HallSeat;

import java.util.List;

public class ClientSeatMap extends SertiaBasicResponse {
    public List<HallSeat> hallSeats;

    public ClientSeatMap(List<HallSeat> hallSeats) {
        this.hallSeats = hallSeats;
    }
}