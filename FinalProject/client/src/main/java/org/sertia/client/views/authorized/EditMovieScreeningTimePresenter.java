package org.sertia.client.views.authorized;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.joda.time.DateTime;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

public class EditMovieScreeningTimePresenter implements Initializable {

    @FXML
    private Label mainLabel;

    @FXML
    private Label movieNameLabel;

    @FXML
    private Label actorNameLabel;

    @FXML
    private Label branchNameLabel;

    @FXML
    private Label hallNumber;

    @FXML
    private DatePicker datePickerComp;

    @FXML
    private TextField screeningTimeTxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientMovie movie = MovieHolder.getInstance().getMovie();
        ClientScreening screening = ScreeningHolder.getInstance().getScreening();
        mainLabel.setText(mainLabel.getText() + movie.getName());
        mainLabel.setMaxWidth(400);
        movieNameLabel.setText(movie.getName());
        actorNameLabel.setText(movie.getMainActorName());
        branchNameLabel.setText(screening.getCinemaName());
        hallNumber.setText(String.valueOf(screening.getHallId()));
        String movieScreeningTime = screening.getScreeningTime().toString();
        DateTime dateTime = DateTime.parse(movieScreeningTime);
        datePickerComp.setValue(LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()));
        screeningTimeTxt.setText(parseTimeWithoutDate(movieScreeningTime));
    }

    @FXML
    private void backToPreviusPage() throws IOException {
        App.setRoot("authorized/availableMoviesForEdit");
    }

    @FXML
    private void requestChangeFromServer() throws IOException {
        String newHour = screeningTimeTxt.getText();
        LocalDate inputDate = datePickerComp.getValue();

        if (isCorrectHour(newHour)) {
            ClientScreening screening = ScreeningHolder.getInstance().getScreening();
            LocalDateTime newDateTime = LocalDateTime.of(inputDate.getYear(),
                    inputDate.getMonth(), inputDate.getDayOfMonth(), getHour(newHour), getMin(newHour));
            screening.setScreeningTime(newDateTime);
            ClientCatalogControl.getInstance().tryUpdateScreeningTime(screening);
        }
        App.setRoot("authorized/availableMoviesForEdit");
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
}
