package org.sertia.cinema;

import org.sertia.movie.HallEntranceTicket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class CinemaHall {
    private CinemaSeat[][] availableSeats;
    private int hallNumber;
    private int totalSeatsNumber;

    public CinemaHall(int hallNumber, int cinemaHallSize) {
        this.hallNumber = hallNumber;
        availableSeats = new CinemaSeat[cinemaHallSize][cinemaHallSize];
        this.totalSeatsNumber = cinemaHallSize * cinemaHallSize;
        initializeEmptyHall();
    }

    private void initializeEmptyHall() {
        // TODO: initialie all with true, or by definition with purple tav
    }

    public Collection<HallEntranceTicket> purchaseTickets(int numberOfTickets) {
        // TODO: validation & return empty collection if can't purchase
        ArrayList<HallEntranceTicket> tickets = new ArrayList<>();
        for (int i = 0; i < numberOfTickets; i++)
            tickets.add(new HallEntranceTicket(1, 2, hallNumber));
        return tickets;
    }

    public void setRestrictionPolicy(HashMap<Integer, Integer> sizeToMaxCapacity) {
        // TODO
    }
}
