package org.sertia.order.summary;

import org.sertia.PaymentMethod;
import org.sertia.users.Customer;

public abstract class AbstractOrderSummary {
    private String movieName;
    private Customer customer;
    private PaymentMethod paymentMethod;

    protected AbstractOrderSummary(String movieName, Customer customer, PaymentMethod paymentMethod) {
        this.movieName = movieName;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerMessage(){
        return "Movie name: " + movieName;
    }
}
