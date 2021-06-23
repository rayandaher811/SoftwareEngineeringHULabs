package org.sertia.contracts.screening.ticket;

import java.util.List;

public class ClientSeatMap {
    List<HallSeat> hallSeats;

    public ClientSeatMap(List<HallSeat> hallSeats) {
        this.hallSeats = hallSeats;
    }
}