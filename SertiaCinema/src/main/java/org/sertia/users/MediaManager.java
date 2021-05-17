package org.sertia.users;

import org.sertia.ClientsNotifier;
import org.sertia.movie.ScreeningMovie;

import java.util.Collection;

public class MediaManager extends BaseUser {
    public MediaManager(String userName, String password) {
        super(userName, password, Role.MEDIA_MANAGER);
    }

    public void updateMovieList(Collection<ScreeningMovie> newMovies){
        // TODO: insert or update
    }

    private void addMovieToClientsNotifier(ScreeningMovie newMovie){
        ClientsNotifier.getInstance().addMovieToNotifyingList(newMovie);
    }

    public void deleteMovies(Collection<ScreeningMovie> moviesToDelete){
        // TODO: delete
    }

    public void updateTicketsPrices(double newPrice){
        // TODO: update
        requestForAcceptFromManager();
    }

    private void requestForAcceptFromManager(){

    }
}
