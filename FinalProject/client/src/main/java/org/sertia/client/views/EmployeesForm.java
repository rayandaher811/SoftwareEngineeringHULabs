package org.sertia.client.views;

import javafx.fxml.FXML;
import org.sertia.client.App;
import org.sertia.client.global.LoggedInUser;

import java.io.IOException;

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