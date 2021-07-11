package org.sertia.client.views.unauthorized.purchase;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sertia.client.App;
import org.sertia.client.global.BuyOnlineScreeningLinkDataHolder;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.views.Utils;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.*;

public class OnlineMovieLinkPresenter extends BasicPresenterWithValidations implements Initializable {
    @FXML
    public TextField movieName;
    public TextField movieLength;
    public TextField nameTxtField;
    public TextField phoneTxTextField;
    public TextField emailTxTextField;
    public TextField extraHoursTextField;
    public Label numberOfAvailabilityHours;
    public Label numberOfAvailabilityHoursLabel;
    public DatePicker datePickerFrom;
    public ComboBox<Integer> hourPicker;
    public ComboBox<Integer> minutePicker;
    boolean isFromDateSet;
    long totalHours;

    @FXML
    public void back() throws IOException {
        BuyOnlineScreeningLinkDataHolder.getInstance().clear();
        App.setRoot("unauthorized/sertiaCatalogPresenter");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        totalHours = -1;
        isFromDateSet = false;
        BuyOnlineScreeningLinkDataHolder linkDataHolder = BuyOnlineScreeningLinkDataHolder.getInstance();
        if (linkDataHolder.isInitialized()) {
            nameTxtField.setText(linkDataHolder.getClientName());
            phoneTxTextField.setText(linkDataHolder.getPhone());
            emailTxTextField.setText(linkDataHolder.getEmail());
            LocalDateTime chosenStartDateTime = linkDataHolder.getStartDateTime();
            hourPicker.setValue(chosenStartDateTime.getHour());
            minutePicker.setValue(chosenStartDateTime.getMinute());
            datePickerFrom.setValue(chosenStartDateTime.toLocalDate());
            LocalDate incrementedDateTime = chosenStartDateTime.toLocalDate();
            incrementedDateTime.plusDays(linkDataHolder.getNumberOfDaysForRental());
            totalHours = ChronoUnit.DAYS.between(chosenStartDateTime.toLocalDate(), incrementedDateTime);
            numberOfAvailabilityHoursLabel.setVisible(true);
            numberOfAvailabilityHours.setText(String.valueOf(totalHours));
        }

        CinemaScreeningMovie movie = MovieHolder.getInstance().getCinemaScreeningMovie();
        movieName.setText(movie.getMovieDetails().getName());
        Duration duration = MovieHolder.getInstance().getCinemaScreeningMovie().getMovieDetails().duration;
        numberOfAvailabilityHours.setText(String.valueOf(getBaseHours()));
        movieLength.setText(duration.toMinutes() + " דקות ");
        extraHoursTextField.setText("0");
        extraHoursTextField.textProperty().addListener(this::changed);
        datePickerFrom.valueProperty().addListener((observableValue, date, t1) -> {
            LocalDate nowDate = LocalDate.now();
            if (t1.isBefore(nowDate)) {
                datePickerFrom.setStyle("-fx-background-color: #ff1500");
                extraHoursTextField.setStyle("-fx-background-color: #ff1500");
                Utils.popAlert(Alert.AlertType.ERROR, BUY_ONLINE_STREAMING_LINK, PLEASE_CHOOSE_START_DATE_GREATER_THAN_TODAY);
                totalHours = -1;
            } else {
                datePickerFrom.setStyle("");
                extraHoursTextField.setStyle("");
                totalHours = -1;
            }
        });

        hourPicker.getItems().addAll(HOURS);
        minutePicker.getItems().addAll(MINUTES);
    }

    private void changed(ObservableValue<? extends String> observable, String oldHours, String newHours) {
        int hours = 0;
        if (newHours.isBlank() || newHours.isEmpty()) {
            totalHours = 0;
            extraHoursTextField.setText("0");
        } else {
            try {
                hours = Integer.parseInt(newHours);
                extraHoursTextField.setText(String.valueOf(hours));
            } catch (NumberFormatException ignored) {
                Utils.popAlert(Alert.AlertType.ERROR, BUY_ONLINE_STREAMING_LINK, PLEAS_ENTER_VALID_HOURS);
                totalHours = 0;
                extraHoursTextField.setText("0");
                return;
            }
        }

        int baseHours = getBaseHours();
        totalHours = baseHours + hours;
        numberOfAvailabilityHours.setText(String.valueOf(totalHours));
    }

    private int getBaseHours() {
        return (int) Math.ceil((double) MovieHolder.getInstance().getCinemaScreeningMovie().movieDetails.duration.toMinutes() / 60);
    }

    @FXML
    public void buyLink() {
        if (isInputValid()) {
            try {
                BuyOnlineScreeningLinkDataHolder.
                        getInstance()
                        .setClientData(nameTxtField.getText(),
                                emailTxTextField.getText(),
                                phoneTxTextField.getText(),
                                Integer.parseInt(extraHoursTextField.getText()),
                                LocalDateTime.of(datePickerFrom.getValue(),
                                        LocalTime.of(hourPicker.getSelectionModel().getSelectedItem(), minutePicker.getSelectionModel().getSelectedItem()))
                        );
                App.setRoot("unauthorized/payment/byCreditCardForm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean isDataValid() {
        boolean isNameValid = isFullNameValid(nameTxtField.getText());
        boolean isPhoneValid = isPhoneValid(phoneTxTextField.getText());
        boolean isEmailValid = isEmailValid(emailTxTextField.getText());
        boolean isHourValid = hourPicker.getSelectionModel().getSelectedItem() != null;
        boolean isMinuteValid = minutePicker.getSelectionModel().getSelectedItem() != null;
        LocalDateTime startTime = LocalDateTime.of(datePickerFrom.getValue(),
                LocalTime.of(hourPicker.getSelectionModel().getSelectedItem(), minutePicker.getSelectionModel().getSelectedItem()));
        boolean isStartTimeValid = startTime.isAfter(LocalDateTime.now());
        if(!isStartTimeValid) {
            userMistakes.add(BEFORE_NOW_TIME);
        }

        if (!isHourValid)
            userMistakes.add(CHOOSE_VALID_HOUR);
        if (!isMinuteValid)
            userMistakes.add(CHOOSE_VALID_MIN);
        return isEmailValid && isPhoneValid && isNameValid && isHourValid && isMinuteValid && isStartTimeValid;
    }
}
