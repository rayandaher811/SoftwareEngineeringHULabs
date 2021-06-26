package org.sertia.contracts.screening.ticket.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.screening.ticket.HallSeat;

import java.util.List;

public class ClientSeatMapResponse extends SertiaBasicResponse {
    public List<HallSeat> hallSeats;

    public ClientSeatMapResponse(boolean isSuccessful, List<HallSeat> hallSeats) {
        super(isSuccessful);
        this.hallSeats = hallSeats;
    }
}