package org.sertia.movie;

import org.sertia.PaymentMethod;
import org.sertia.cinema.LeadingActor;
import org.sertia.order.summary.OnlineMovieLink;
import org.sertia.users.Customer;

import java.util.Collection;

public class OnlineWatchMovie extends Movie {
    protected OnlineWatchMovie(String name, String hebrewName, String producer, Collection<LeadingActor> leadingActors, String summary, Object image) {
        super(name, hebrewName, producer, leadingActors, summary, image);
    }

    public OnlineMovieLink purchaseOnlineViewLink(Customer customer, PaymentMethod paymentMethod){
        return new OnlineMovieLink(this.name, customer, paymentMethod);
    }
}
