package org.sertia.contracts.screening.ticket;

import java.io.Serializable;
import java.util.List;

public class ClientSeatMap implements Serializable {
    List<HallSeat> hallSeats;

    public ClientSeatMap(List<HallSeat> hallSeats) {
        this.hallSeats = hallSeats;
    }
}