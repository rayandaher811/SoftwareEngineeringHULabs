package org.sertia.client.controllers;

import javafx.fxml.FXML;
import org.sertia.client.App;
import org.sertia.client.communication.ServerCommunicationHandler;
import org.sertia.client.dialogs.AvailableMoviesDialog;
import org.sertia.client.global.LoggedInUser;
import org.sertia.client.pojos.ScreeningMovie;

import java.io.IOException;
import java.util.Collection;

public class EmployeesForm {

    @FXML
    private void logOut() throws IOException {
        LoggedInUser.onDisconnection();
        App.setRoot("primary");
    }

    @FXML
    private void updateMovie() throws IOException {
        App.setRoot("availableMoviesForEdit");
    }
}