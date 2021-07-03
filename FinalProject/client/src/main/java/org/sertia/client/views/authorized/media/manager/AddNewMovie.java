package org.sertia.client.views.authorized.media.manager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.SertiaMovieHolder;
import org.sertia.client.views.unauthorized.didntuse.BasicPresenter;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddNewMovie extends BasicPresenter {
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
        ClientMovie clientMovie = new ClientMovie(movieHebrewNameTxt.getText(), movieNameTxt.getText(),
                movieDescriptionTxt.getText(), mainActorsTxt.getText(),
                producerNameTxt.getText(), moviePhotoUrl.getText());

        List<ClientScreening> screeningList = new ArrayList<>();
        double ticketPrice = Double.valueOf(ticketPriceTxt.getText());
        boolean isStreamable = availableOnline.isSelected();
        boolean isComingSoon = isComingSoonCb.isSelected();
        SertiaMovie movie = new SertiaMovie(clientMovie, screeningList, ticketPrice, isStreamable, isComingSoon);
        SertiaMovieHolder.getInstance().setSertiaMovie(movie);
        try {
            App.setRoot("authorized/media.manager/addNewMovieScreenings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back(){
        try {
            App.setRoot("authorized/employeesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}