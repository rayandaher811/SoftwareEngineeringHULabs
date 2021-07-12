package org.sertia.client.views.authorized.media.manager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.views.TicketType;
import org.sertia.client.views.Utils;
import org.sertia.client.views.BasicPresenterWithValidations;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.price.change.ClientTicketType;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.sertia.client.Constants.*;

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

    public void addMovie(MouseEvent mouseEvent) {
        if (isInputValid()) {
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
                    Utils.popAlert(Alert.AlertType.INFORMATION, ADD_MOVIE, ADD_MOVIE_COMPLETED_SUCCESSFULLY);
                    App.setRoot("authorized/employeesForm");
                } else {
                    Utils.popAlert(Alert.AlertType.ERROR, ADD_MOVIE, response.failReason);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utils.popAlert(Alert.AlertType.ERROR, ADD_MOVIE, MOVIE_DETAILS_INVALID);
        }
    }

    protected boolean isStreamingValid() {
        if (!availableOnline.isSelected() && !isPriceValid(streamingPriceTxt.getText(), ClientTicketType.Streaming)) {
            userMistakes.add(PURCHASE_LINK_WITH_PRICE);
            return false;
        }
        return true;
    }
    @Override
    protected boolean isDataValid() {
        boolean isStreamingValid = true;
        if (availableOnline.isSelected()) {
            isStreamingValid = isStreamingValid();
        }

        boolean isHebrewNameValid = isStringNotEmpty(movieHebrewNameTxt.getText(), HEBREW_NAME_FOR_MOVIE_ERROR);
        boolean isMovieNameValid = isStringNotEmpty(movieNameTxt.getText(), NAME_FOR_MOVIE_ERROR);
        boolean isMovieDescriptionValid = isStringNotEmpty(movieDescriptionTxt.getText(), DESCRIPTION_FOR_MOVIE_ERROR);
        boolean areMainActorsValid = isStringNotEmpty(mainActorsTxt.getText(), MAIN_ACTORS_FOR_MOVIE_ERROR);
        boolean isProducerNameValid = isStringNotEmpty(producerNameTxt.getText(), PRODUCER_NAME_FOR_MOVIE_ERROR);
        boolean isPhotoUrlValid = isStringNotEmpty(moviePhotoUrl.getText(), PHOTO_URL_FOR_MOVIE_ERROR);
        boolean isTicketPriceValid = isPriceValid(ticketPriceTxt.getText(), ClientTicketType.Screening);
        boolean isMovieLengthValid = movieLengthValid(movieLengthTxt.getText());
        return isStreamingValid && isHebrewNameValid && isMovieNameValid && isMovieDescriptionValid &&
                areMainActorsValid && isProducerNameValid && isPhotoUrlValid && isTicketPriceValid && isMovieLengthValid;
    }

    public boolean isPriceValid(String price, ClientTicketType ticketType) {
        if (price == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(price);
        } catch (NumberFormatException nfe) {
            if (ticketType == ClientTicketType.Screening){
                userMistakes.add(MOVIE_SCREENING_PRICE_ERROR);
            } else if (ticketType == ClientTicketType.Streaming){
                userMistakes.add(MOVIE_STREAMING_PRICE_ERROR);
            }
            return false;
        }
        return true;
    }

    public boolean movieLengthValid(String length) {
        if (length == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(length);
        } catch (NumberFormatException nfe) {
            userMistakes.add("נא צייני אורך סרט בדקות");
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