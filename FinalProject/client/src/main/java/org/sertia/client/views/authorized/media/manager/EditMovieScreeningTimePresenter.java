package org.sertia.client.views.authorized.media.manager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.joda.time.DateTime;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.client.views.Utils;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EditMovieScreeningTimePresenter implements Initializable {

    public Label mainLabel;
    public Label movieNameLabel;
    public Label actorNameLabel;
    public Label branchNameLabel;
    public Label hallNumber;
    public DatePicker datePickerComp;
    public TextField screeningTimeTxt;
    public Button removeScreeningBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CinemaScreeningMovie movie = MovieHolder.getInstance().getCinemaScreeningMovie();
        ClientScreening screening = ScreeningHolder.getInstance().getScreening();
        mainLabel.setText(mainLabel.getText() + movie.getMovieDetails().getName());
        mainLabel.setMaxWidth(400);
        movieNameLabel.setText(movie.getMovieDetails().getName());
        actorNameLabel.setText(movie.getMovieDetails().getMainActorName());
        branchNameLabel.setText(screening.getCinemaName());
        hallNumber.setText(String.valueOf(screening.getHallId()));
        String movieScreeningTime = screening.getScreeningTime().toString();
        DateTime dateTime = DateTime.parse(movieScreeningTime);
        datePickerComp.setValue(LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()));
        screeningTimeTxt.setText(parseTimeWithoutDate(movieScreeningTime));
        removeScreeningBtn.setStyle("-fx-background-color: #ff1500");
    }

    @FXML
    private void backToPreviousPage() throws IOException {
        App.setRoot("authorized/media.manager/availableMoviesForEdit");
    }

    // TODO: INPUT VALIDAION AND HANDLE ERRORS
    @FXML
    private void requestChangeFromServer() throws IOException {
        String newHour = screeningTimeTxt.getText();
        LocalDate inputDate = datePickerComp.getValue();

        if (isCorrectHour(newHour)) {
            ClientScreening screening = ScreeningHolder.getInstance().getScreening();
            LocalDateTime newDateTime = LocalDateTime.of(inputDate.getYear(),
                    inputDate.getMonth(), inputDate.getDayOfMonth(), getHour(newHour), getMin(newHour));
            screening.setScreeningTime(newDateTime);
            SertiaBasicResponse response = ClientCatalogControl.getInstance().tryUpdateScreeningTime(screening);

            if (response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.INFORMATION, "Buying from sertia system", "operation ended successfully!");
                App.setRoot("authorized/media.manager/availableMoviesForEdit");
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, "Buying from sertia system", response.failReason);
            }
        }
    }

    private int getHour(String hour) {
        return Integer.parseInt(hour.split(":")[0]);
    }

    private int getMin(String hour) {
        return Integer.parseInt(hour.split(":")[1]);
    }

    private boolean isCorrectHour(String newHour) {
        if (newHour.split(":").length == 2) {
            int hour = getHour(newHour);
            int min = getMin(newHour);
            return 0 <= hour && hour <= 23 && 0 <= min && min <= 59;
        }
        return false;
    }

    private String parseTimeWithoutDate(String dateTime) {
        int pivotIndex = dateTime.indexOf(":");
        return dateTime.substring(pivotIndex - 2, pivotIndex + 3);
    }

    public void removeScreening(MouseEvent mouseEvent) {
        ClientScreening screening = ScreeningHolder.getInstance().getScreening();
        SertiaBasicResponse response =
                ClientCatalogControl.getInstance().tryRemoveScreening(screening.getScreeningId());

        if (response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.INFORMATION, "Remove screening", "Remove operation ended successfully!");
            try {
                backToPreviousPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utils.popAlert(Alert.AlertType.ERROR, "Remove screening", response.failReason);
        }
    }
}
