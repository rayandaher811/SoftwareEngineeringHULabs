package org.sertia.order.summary;

import org.sertia.PaymentMethod;
import org.sertia.movie.HallEntranceTicket;
import org.sertia.movie.MovieTicket;
import org.sertia.users.Customer;

import java.util.Collection;

public class CinemaMovieTicket extends AbstractOrderSummary {
    private String branchName;
    private Collection<MovieTicket> movieTickets;

    public CinemaMovieTicket(String branchName, Collection<MovieTicket> movieTickets, Customer customer, PaymentMethod paymentMethod) {
        super(movieTickets.stream().findFirst().get().getMovieName(), customer, paymentMethod);
        this.branchName = branchName;
        this.movieTickets = movieTickets;
    }

    @Override
    public String getCustomerMessage() {
        HallEntranceTicket firstEntranceTicket = movieTickets.stream().findFirst().get().getEntranceTicket();
        String message =
                super.getCustomerMessage() + ", Branch Name: " + branchName + ", Hall number: " + firstEntranceTicket.getHallNumber();
//        movieTickets.forEach(movieTicket ->
//                message += ", seat: row: " + movieTicket.getEntranceTicket().getRow() + ", seatNumber: " + movieTicket.getEntranceTicket().getCol());
        message += ", Total number of tickets: " + movieTickets.size();
        return message;
    }
}
