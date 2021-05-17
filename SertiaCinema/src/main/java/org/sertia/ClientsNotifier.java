package org.sertia;

import org.sertia.movie.ScreeningMovie;
import org.sertia.order.summary.OnlineMovieLink;
import org.sertia.users.Customer;

public class ClientsNotifier {
    private static ClientsNotifier clientsNotifier = null;

    protected ClientsNotifier(){

    }

    public static ClientsNotifier getInstance(){
        if (clientsNotifier == null)
            clientsNotifier = new ClientsNotifier();
        return clientsNotifier;
    }

    public void addMovieToNotifyingList(ScreeningMovie movie){

    }

    public void removeMovieFromNotifyingList(ScreeningMovie movie){

    }

    public void addUserToLinkedMoviesNotifications(Customer customer, OnlineMovieLink onlineMovieLink){

    }

    public void removeUserFromLinkNotifications(Customer customer, OnlineMovieLink onlineMovieLink){

    }

    private void notifyClient(Customer customer, String message){
        // TODO: notify relevant client with message
    }

}
