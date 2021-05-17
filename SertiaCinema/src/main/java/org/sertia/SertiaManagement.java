package org.sertia;

import org.sertia.cinema.Cinema;
import org.sertia.movie.ComingSoonMovie;
import org.sertia.movie.OnlineWatchMovie;
import org.sertia.movie.ScreeningMovie;
import org.sertia.order.summary.CinemaMovieTicket;
import org.sertia.order.summary.OnlineMovieLink;
import org.sertia.order.summary.PayedInAdvancedMovieTickets;
import org.sertia.users.Customer;
import org.sertia.users.NetworkManager;

import java.util.Collection;
import java.util.HashMap;

public class SertiaManagement {
    private NetworkManager networkManager;
    private Collection<Cinema> cinemas;
    private Collection<ComingSoonMovie> comingSoonMovies;
    private Collection<OnlineWatchMovie> onlineWatchMovies;
    private HashMap<Customer, PayedInAdvancedMovieTickets> customersToPayedInAdvancedTickets;

    public SertiaManagement(Collection<Cinema> cinemas, Collection<ComingSoonMovie> comingSoonMovies, Collection<OnlineWatchMovie> onlineWatchMovies) {
        this.cinemas = cinemas;
        this.comingSoonMovies = comingSoonMovies;
        this.onlineWatchMovies = onlineWatchMovies;
        this.customersToPayedInAdvancedTickets = new HashMap<>();
    }

    public void showPlayingMoviesInChain() {
        cinemas.forEach(Cinema::printPlayingMovies);
    }

    public void showComingSoonMovies() {
        comingSoonMovies.forEach(System.out::println);
    }

    public void showOnlineWatchMovies() {
        onlineWatchMovies.forEach(System.out::println);
    }

    public PayedInAdvancedMovieTickets buyInAdvance(Customer customer, PaymentMethod paymentMethod) {
        PayedInAdvancedMovieTickets tickets = new PayedInAdvancedMovieTickets(customer, paymentMethod);
        registerToHistory(customer, paymentMethod);
        return tickets;
    }

    public int getAvailableTicketsInAdvancedCount(Customer customer) {
        return customersToPayedInAdvancedTickets.get(customer).getAvailableTickets();
    }

    public CinemaMovieTicket buyMovieTicket(Cinema cinemaToB, ScreeningMovie movieToWatch,
                                            int numberOfTickets, Customer customer, PaymentMethod paymentMethod) {
        CinemaMovieTicket orderSummary = cinemas.stream().filter(cinema -> equals(cinemaToB)).findAny().get().buyTickets(numberOfTickets, movieToWatch, customer, paymentMethod);
        if (orderSummary != null) {
            sendMessageToCustomer(customer, orderSummary.getCustomerMessage());
            registerToHistory(customer, paymentMethod);
            return orderSummary;
        }
        return null;
    }

    public OnlineMovieLink buyOnlineMovieLink(ComingSoonMovie onlineWatchMovie, Customer customer, PaymentMethod paymentMethod) {
        if (onlineWatchMovies.contains(onlineWatchMovie)) {
            OnlineMovieLink viewLink = onlineWatchMovies.stream().filter(onlineWatchMovie1 -> equals(onlineWatchMovie)).findAny().get().purchaseOnlineViewLink(customer, paymentMethod);
            addOnlineWatcherToNotifiedCustomers(customer, viewLink);
            sendMessageToCustomer(customer, viewLink.getCustomerMessage());
            registerToHistory(customer, paymentMethod);
            return viewLink;
        }
        return null;
    }

    public void returnTickets(CinemaMovieTicket cinemaMovieTicket, Customer customer) {
        // TODO: impl
        sendMessageToCustomer(customer, "TBD");
    }

    public void returnOnlineLink(OnlineMovieLink onlineMovieLink, Customer customer) {
        // TODO: impl
    }

    private void sendMessageToCustomer(Customer customer, String message) {
        // TODO: impl
    }

    private void addOnlineWatcherToNotifiedCustomers(Customer customer, OnlineMovieLink onlineMovieLink) {

    }

    private void registerToHistory(Customer customer, PaymentMethod paymentMethod) {
    }

    private void registerInAdvancedTickts(PayedInAdvancedMovieTickets tickets, Customer customer) {
        // TODO
    }
}
