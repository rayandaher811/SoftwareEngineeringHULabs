package org.sertia.movie;

import org.joda.time.DateTime;
import org.sertia.cinema.CinemaHall;
import org.sertia.cinema.LeadingActor;

import java.util.Collection;
import java.util.Collections;

public class ScreeningMovie extends Movie {
    private CinemaHall cinemaHall;
    private DateTime playingTime;

    public ScreeningMovie(String name, String hebrewName, String producer, Collection<LeadingActor> leadingActors,
                          String summary, Object image, CinemaHall cinemaHall, DateTime playingTime) {
        super(name, hebrewName, producer, leadingActors, summary, image);
        this.cinemaHall = cinemaHall;
        this.playingTime = playingTime;
    }

    public Collection<MovieTicket> purchaseTickets(int numberOfTickets, String cinemaName) {
        Collection<HallEntranceTicket> hallEntranceTickets = cinemaHall.purchaseTickets(numberOfTickets);
        if (hallEntranceTickets.size() == numberOfTickets) {
            hallEntranceTickets.forEach(entranceTicket -> new MovieTicket(cinemaName, entranceTicket));
        }
        return Collections.emptyList();
    }

    public boolean equals(Object o) {
        // TODO: implement
        return true;
    }
}
