package org.sertia.cinema;

import org.sertia.PaymentMethod;
import org.sertia.movie.ScreeningMovie;
import org.sertia.order.summary.CinemaMovieTicket;
import org.sertia.users.CinemaManager;
import org.sertia.users.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class Cinema {
    private Collection<ScreeningMovie> availableMovies;
    private String branchName;
    private String address;
    private CinemaManager manager;

    private Collection<CinemaHall> cinemaHalls;

    public Cinema(Collection<ScreeningMovie> availableMovies, String branchName, CinemaManager manager, String adderess) {
        this.availableMovies = availableMovies;
        this.branchName = branchName;
        this.manager = manager;
        this.address = adderess;
    }

    public void printPlayingMovies(){
        // TODO: print without duplicates
        availableMovies.forEach(System.out::println);
    }

    public CinemaMovieTicket buyTickets(int numberOfTickets, ScreeningMovie movie, Customer customer, PaymentMethod paymentMethod){
        for (ScreeningMovie movie1 : availableMovies) {
            if (movie1.equals(movie)) {
                return new CinemaMovieTicket(branchName, availableMovies.stream().filter(movie2 -> equals(movie)).findFirst().get().purchaseTickets(numberOfTickets, branchName), customer, paymentMethod);
            }
        }
        return null;
    }
}
