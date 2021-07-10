package org.sertia.client.views.authorized.media.manager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.views.Utils;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

// TODO: base validations
public class AddNewMovie {
    @FXML
    private TextField movieNameTxt;
    @FXML
    private TextField movieHebrewNameTxt;
    @FXML
    private TextField producerNameTxt;
    @FXML
    private TextField mainActorsTxt;
    @FXML
    private TextField movieDescriptionTxt;
    @FXML
    private TextField moviePhotoUrl;
    @FXML
    private TextField ticketPriceTxt;
    @FXML
    private CheckBox availableOnline;
    @FXML
    private CheckBox isComingSoonCb;


    private boolean isInputValid() {
        boolean areMovieDataFieldsValid = validateMovieData();
        return areMovieDataFieldsValid;
    }

    private boolean isTicketPriceValid() {
        return ticketPriceTxt != null && !ticketPriceTxt.getText().isEmpty() && !ticketPriceTxt.getText().isBlank();
    }

    private boolean validateMovieData() {
        return movieHebrewNameTxt != null && movieHebrewNameTxt.getText().isBlank() && !movieHebrewNameTxt.getText().isEmpty()
                && movieNameTxt != null && movieNameTxt.getText().isBlank() && !movieNameTxt.getText().isEmpty()
                && movieDescriptionTxt != null && movieDescriptionTxt.getText().isBlank() && !movieDescriptionTxt.getText().isEmpty()
                && mainActorsTxt != null && mainActorsTxt.getText().isBlank() && !mainActorsTxt.getText().isEmpty()
                && producerNameTxt != null && producerNameTxt.getText().isBlank() && !producerNameTxt.getText().isEmpty()
                && moviePhotoUrl != null && moviePhotoUrl.getText().isBlank() && !moviePhotoUrl.getText().isEmpty();
    }

    public void addMovie(MouseEvent mouseEvent) {
        // TODO: fix duration after put it in project
        ClientMovie clientMovie = new ClientMovie(movieHebrewNameTxt.getText(), movieNameTxt.getText(),
                movieDescriptionTxt.getText(), mainActorsTxt.getText(),
                producerNameTxt.getText(), moviePhotoUrl.getText(), Duration.ZERO);

        List<ClientScreening> screeningList = new ArrayList<>();
        double ticketPrice = Double.parseDouble(ticketPriceTxt.getText());
        boolean isStreamable = availableOnline.isSelected();
        boolean isComingSoon = isComingSoonCb.isSelected();
        SertiaMovie movie = new SertiaMovie(clientMovie, screeningList, ticketPrice, isStreamable, isComingSoon);
        SertiaBasicResponse response = ClientCatalogControl.getInstance().tryCreateMovie(movie);

        try {
            if (response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.INFORMATION, "Add movie", "Movie added successfully! Need to add screenings");
                App.setRoot("authorized/employeesForm");
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, "Add movie", response.failReason);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back() {
        try {
            App.setRoot("authorized/media.manager/addOrRemoveMovie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}