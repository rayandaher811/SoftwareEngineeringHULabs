package org.sertia.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.sertia.client.App;
import org.sertia.client.communication.ServerCommunicationHandler;
import org.sertia.client.communication.messages.CinemaScreeningMovie;
import org.sertia.client.communication.messages.UpdateMovieScreeningTime;
import org.sertia.client.global.LoggedInUser;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
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

    @FXML
    private void backToEmployeesView() throws IOException {
        App.setRoot("employeesForm");
    }

    @FXML
    private void requestChangeFromServer() throws IOException {
        String newHour = screeningTimeTxt.getText();
        LocalDate inputDate = datePickerComp.getValue();

        if (isCorrectHour(newHour)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(inputDate.getYear(), inputDate.getMonth().getValue() - 1, inputDate.getDayOfMonth(), getHour(newHour), getMin(newHour));
            LocalDateTime dateTime = LocalDateTime.fromCalendarFields(calendar);
            CinemaScreeningMovie movie = LoggedInUser.getInstance().getChosenMovieForUpdateTimeOperation();
            UpdateMovieScreeningTime updateMovieScreeningTime = new UpdateMovieScreeningTime(LoggedInUser.getInstance().getUuid(), movie, dateTime.toString());
            ServerCommunicationHandler.getInstance().requestMovieScreeningTimeChange(updateMovieScreeningTime);
        }
        App.setRoot("availableMoviesForEdit");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CinemaScreeningMovie movie = LoggedInUser.getInstance().getChosenMovieForUpdateTimeOperation();
        if (movie != null) {
            mainLabel.setText(mainLabel.getText() + movie.getName());
            mainLabel.setMaxWidth(400);
            movieNameLabel.setText(movie.getName());
            actorNameLabel.setText(movie.getMainActorName());
            branchNameLabel.setText(movie.getBranchName());
            hallNumber.setText(String.valueOf(movie.getHallNumber()));
            String movieScreeningTime = movie.getScreeningTimeStampStr();
            DateTime dateTime = DateTime.parse(movieScreeningTime);
            datePickerComp.setValue(LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()));
//            datePickerComp.setValue(LocalDateTime.parse(movieScreeningTime).toLocalDate());
            screeningTimeTxt.setText(parseTimeWithoutDate(movieScreeningTime));
        }
    }

    private String parseTimeWithoutDate(String dateTime) {
//        2021-08-01T01:30:00.000
        int pivotIndex = dateTime.indexOf(":");
        return dateTime.substring(pivotIndex - 2, pivotIndex + 3);
    }
}
