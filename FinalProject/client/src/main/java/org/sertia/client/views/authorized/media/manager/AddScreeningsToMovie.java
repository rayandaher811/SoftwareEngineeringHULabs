package org.sertia.client.views.authorized.media.manager;

import javafx.beans.Observable;
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
import org.sertia.contracts.movies.catalog.response.CinemaAndHallsResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.ADD_SCREENING_TO_MOVIE;
import static org.sertia.client.Constants.SCREENING_ADDED_SUCCESSFULLY;

public class AddScreeningsToMovie implements Initializable {
    public Label movieNameLabel;
    public DatePicker datePickerComp;
    public TextField screeningTimeTxt;
    public ComboBox<Integer> hallNumberCombo;
    @FXML
    private ComboBox<String> branchName;
    private int cinemaId;

    public void addScreenings() throws IOException {
        if (cinemaId != -1) {
            CinemaScreeningMovie currentMovie = MovieHolder.getInstance().getCinemaScreeningMovie();
            LocalDate inputDate = datePickerComp.getValue();
            String screeningHour = screeningTimeTxt.getText();
            LocalDateTime screeningTime = LocalDateTime.of(inputDate.getYear(),
                    inputDate.getMonth(), inputDate.getDayOfMonth(), getHour(screeningHour), getMin(screeningHour));
            SertiaBasicResponse addScreeningResponse =
                    ClientCatalogControl.getInstance().tryAddScreening(currentMovie.getMovieId(), screeningTime, hallNumberCombo.getValue(), cinemaId);

            if (addScreeningResponse.isSuccessful) {
                Utils.popAlert(Alert.AlertType.INFORMATION, ADD_SCREENING_TO_MOVIE, SCREENING_ADDED_SUCCESSFULLY);
                App.setRoot("authorized/employeesForm");
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, ADD_SCREENING_TO_MOVIE, addScreeningResponse.failReason);
            }
        } else {
            System.out.println("BGBGBGBGB EERRRORORR!!");
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
        CinemaScreeningMovie currentMovie = MovieHolder.getInstance().getCinemaScreeningMovie();
        movieNameLabel.setText(movieNameLabel.getText() + " " + currentMovie.getMovieDetails().getHebrewName());
        branchName.valueProperty().addListener(this::onBranchSelection);
    }

    private void onBranchSelection(Observable observable) {
        hallNumberCombo.getItems().clear();
        CinemaAndHallsResponse response = ClientCatalogControl.getInstance().getCinemasAndHalls();
        List<ClientHall> clientHallsInCinema = response.cinemaToHalls.get(branchName.getSelectionModel().getSelectedItem());
        clientHallsInCinema.forEach(clientHall -> hallNumberCombo.getItems().add(clientHall.hallNumber));
    }

    private int getHour(String hour) {
        return Integer.parseInt(hour.split(":")[0]);
    }

    private int getMin(String hour) {
        return Integer.parseInt(hour.split(":")[1]);
    }

}
