package org.sertia.client.views.authorized.media.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.views.Utils;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientHall;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.response.CinemaAndHallsResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDateTime;

import static org.sertia.client.Constants.ADD_SCREENING_TO_MOVIE;
import static org.sertia.client.Constants.SCREENING_ADDED_SUCCESSFULLY;

public class AddScreeningsToMovie implements Initializable {
    public Label movieNameLabel;
    public TextField hallNumberTxt;
    public DatePicker datePickerComp;
    public TextField screeningTimeTxt;
    @FXML
    private ComboBox branchName;

    //  error in add screenings, getting error from server
    public void addScreenings() throws IOException {
        CinemaScreeningMovie currentMovie = MovieHolder.getInstance().getCinemaScreeningMovie();
        CinemaAndHallsResponse response = ClientCatalogControl.getInstance().getCinemasAndHalls();
        LocalDate inputDate = datePickerComp.getValue();
        String screeningHour = screeningTimeTxt.getText();
        LocalDateTime screeningTime = LocalDateTime.of(inputDate.getYear(),
                inputDate.getMonth(), inputDate.getDayOfMonth(), getHour(screeningHour), getMin(screeningHour));
        List<ClientHall> clientHallsInCinema = response.cinemaToHalls.get(branchName.getSelectionModel().getSelectedItem());
        // TODO: use hall number from client hall
        // TODO: there is no mapping between hall name which client knows to it's ID, must make one.. or create an endpoint RYAN
        int cinemaId = 2;

        SertiaBasicResponse addScreeningResponse =
                ClientCatalogControl.getInstance().tryAddScreening(currentMovie.getMovieId(), screeningTime, Integer.parseInt(hallNumberTxt.getText()), cinemaId);

        if (addScreeningResponse.isSuccessful){
            Utils.popAlert(Alert.AlertType.INFORMATION, ADD_SCREENING_TO_MOVIE, SCREENING_ADDED_SUCCESSFULLY);
            App.setRoot("authorized/employeesForm");
        } else {
            Utils.popAlert(Alert.AlertType.ERROR, ADD_SCREENING_TO_MOVIE, addScreeningResponse.failReason);
            // TODO: fail reason is null
        }
    }

    public void back() {
        try {
            App.setRoot("authorized/media.manager/availableMoviesForEdit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> ticketTypes = FXCollections.observableList(ClientCatalogControl.getInstance().getAllBranchesName());
        branchName.setItems(ticketTypes);
        CinemaScreeningMovie currentMovie =  MovieHolder.getInstance().getCinemaScreeningMovie();
        movieNameLabel.setText(movieNameLabel.getText() + " " + currentMovie.getMovieDetails().getHebrewName());
    }

    private int getHour(String hour) {
        return Integer.parseInt(hour.split(":")[0]);
    }

    private int getMin(String hour) {
        return Integer.parseInt(hour.split(":")[1]);
    }

}
