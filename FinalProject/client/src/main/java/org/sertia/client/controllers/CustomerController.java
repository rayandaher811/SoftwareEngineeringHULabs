package org.sertia.client.controllers;

import javafx.fxml.FXML;
import org.sertia.client.App;
import org.sertia.client.communication.ServerCommunicationHandler;
import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.client.dialogs.AvailableMoviesDialog;
import org.sertia.client.pojos.ScreeningMovie;

import java.io.IOException;
import java.util.Collection;

public class CustomerController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void presentAvailableMovies() {
        MoviesCatalog movies = ServerCommunicationHandler.getInstance().getMoviesCatalog();

        AvailableMoviesDialog.loginAndGetUserData(movies);
    }
}
