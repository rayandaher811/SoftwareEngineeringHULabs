package org.sertia.client.views.authorized.media.manager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.views.Utils;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

// TODO: base validations
public class AddNewMovie extends BasicPresenterWithValidations {
    @FXML
    private TextField movieNameTxt;
    @FXML
    private TextField movieHebrewNameTxt;
    @FXML
    private TextField producerNameTxt;
    @FXML
    private TextField movieLengthTxt;
    @FXML
    private TextField mainActorsTxt;
    @FXML
    private TextField movieDescriptionTxt;
    @FXML
    private TextField moviePhotoUrl;
    @FXML
    private TextField ticketPriceTxt;
    @FXML
    private TextField streamingPriceTxt;
    @FXML
    private CheckBox availableOnline;

    @Override
    protected boolean isDataValid() {
        boolean isStreamingValid = true;
        if(availableOnline.isSelected() && !isPriceValid(streamingPriceTxt.getText())) {
            isStreamingValid = false;
        }

        return movieHebrewNameTxt != null && !movieHebrewNameTxt.getText().isBlank() && !movieHebrewNameTxt.getText().isEmpty()
                && movieNameTxt != null && !movieNameTxt.getText().isBlank() && !movieNameTxt.getText().isEmpty()
                && movieDescriptionTxt != null && !movieDescriptionTxt.getText().isBlank() && !movieDescriptionTxt.getText().isEmpty()
                && mainActorsTxt != null && !mainActorsTxt.getText().isBlank() && !mainActorsTxt.getText().isEmpty()
                && producerNameTxt != null && !producerNameTxt.getText().isBlank() && !producerNameTxt.getText().isEmpty()
                && moviePhotoUrl != null && !moviePhotoUrl.getText().isBlank() && !moviePhotoUrl.getText().isEmpty()
                && ticketPriceTxt != null && !ticketPriceTxt.getText().isBlank() && !ticketPriceTxt.getText().isEmpty()
                && isPriceValid(ticketPriceTxt.getText()) && movieLengthValid(movieLengthTxt.getText()) && isStreamingValid;
    }

    public void addMovie(MouseEvent mouseEvent) {
        if(isInputValid()) {
            ClientMovie clientMovie = new ClientMovie(movieHebrewNameTxt.getText(), movieNameTxt.getText(),
                    movieDescriptionTxt.getText(), mainActorsTxt.getText(),
                    producerNameTxt.getText(), moviePhotoUrl.getText(), Duration.ZERO);
            clientMovie.duration = Duration.ofMinutes(Integer.parseInt(movieLengthTxt.getText()));

            List<ClientScreening> screeningList = new ArrayList<>();
            double ticketPrice = Double.parseDouble(ticketPriceTxt.getText());

            double streamingPrice = 0;
            boolean isStreamable = availableOnline.isSelected();
            if (isStreamable) {
                streamingPrice = Double.parseDouble(streamingPriceTxt.getText());
            }

            SertiaMovie movie = new SertiaMovie(clientMovie, screeningList, ticketPrice, isStreamable, streamingPrice);
            SertiaBasicResponse response = ClientCatalogControl.getInstance().tryCreateMovie(movie);

            try {
                if (response.isSuccessful) {
                    Utils.popAlert(Alert.AlertType.INFORMATION, "הוספת סרט", "הסרט התווסף בהצלחה וכעת ניתן להוסיף הקרנות");
                    App.setRoot("authorized/employeesForm");
                } else {
                    Utils.popAlert(Alert.AlertType.ERROR, "הוספת סרט", response.failReason);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utils.popAlert(Alert.AlertType.ERROR,"הוספת סרט", "נתוני הסרט אינם תקינים");
        }
    }

    public static boolean isPriceValid(String price) {
        if (price == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(price);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean movieLengthValid(String length) {
        if (length == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(length);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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