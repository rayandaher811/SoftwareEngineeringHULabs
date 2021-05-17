package org.sertia.order.summary;

import org.joda.time.DateTime;
import org.sertia.PaymentMethod;
import org.sertia.users.Customer;

public class OnlineMovieLink extends AbstractOrderSummary{
    private DateTime expirationTime;
    private String link;

    // TODO: impl
    public OnlineMovieLink(String movieName, Customer customer, PaymentMethod paymentMethod) {
        super(movieName, customer, paymentMethod);
    }

    @Override
    public String getCustomerMessage() {
        return "";
    }
}
