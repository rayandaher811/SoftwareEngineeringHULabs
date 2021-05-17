package org.sertia;

import org.sertia.order.summary.AbstractOrderSummary;
import org.sertia.order.summary.CinemaMovieTicket;
import org.sertia.users.Customer;

import java.util.Collection;

public class CustomerService {

    private Collection<AbstractOrderSummary> allPurchasedTickets;

    public CustomerService(Collection<AbstractOrderSummary> allPurchasedTickets){
        this.allPurchasedTickets = allPurchasedTickets;

    }

    public void updatePurpleTavPolicy(boolean canShowMovies, int maxAllowedCapacity){
        PurpleTavPolicy.getInstance().updatePolicy(canShowMovies, maxAllowedCapacity);
        returnMoneyForCancelledMovies();
    }

    private void returnMoneyForCancelledMovies(){
        // IMPL: stream all orders, and check if by changes of tav sagol, need to cancel shows
    }

    private void returnMoneyToCustomer(Customer customer, PaymentMethod paymentMethod){
        notifyClientForCancellation(customer);
    }

    private void notifyClientForCancellation(Customer customer){
        // TODO: impl
    }
}
