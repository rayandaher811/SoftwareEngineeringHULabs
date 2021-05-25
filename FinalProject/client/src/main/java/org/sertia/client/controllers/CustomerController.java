package org.sertia.client.controllers;

import javafx.fxml.FXML;
import org.sertia.client.App;

import java.io.IOException;

public class CustomerController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void presentAvailableMovies() throws IOException {
        App.setRoot("availableMoviesPresenter");
    }
}
