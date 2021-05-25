package org.sertia.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.sertia.client.App;
import org.sertia.client.communication.messages.CinemaScreeningMovie;
import org.sertia.client.global.LoggedInUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditMovieScreeningTimePresenter implements Initializable {

    @FXML
    private Label mainLabel;

    @FXML
    private void backToEmployeesView() throws IOException {
        App.setRoot("employeesForm");
    }

    @FXML
    private void requestChangeFromServer() throws IOException {
//        App.setRoot("employeesForm");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CinemaScreeningMovie movie = LoggedInUser.getInstance().getChosenMovieForUpdateTimeOperation();
        if (movie != null) {
            mainLabel.setText(mainLabel.getText() + movie.getName());
            mainLabel.setMaxWidth(400);
        }

    }
}
