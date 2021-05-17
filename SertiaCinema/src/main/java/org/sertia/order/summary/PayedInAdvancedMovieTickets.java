package org.sertia.order.summary;

import org.sertia.PaymentMethod;
import org.sertia.users.Customer;

import java.util.ArrayList;
import java.util.Collection;

public class PayedInAdvancedMovieTickets extends AbstractOrderSummary {
    private static final int MAX_ALLOWED_MOVIES = 20;
    private Collection<CinemaMovieTicket> usedTickets;

    public PayedInAdvancedMovieTickets(Customer customer, PaymentMethod paymentMethod) {
        super("", customer, paymentMethod);
        this.usedTickets = new ArrayList<>();
    }

    public int getAvailableTickets(){
        return MAX_ALLOWED_MOVIES - usedTickets.size();
    }

    @Override
    public String getCustomerMessage() {
        return null;
    }
}
