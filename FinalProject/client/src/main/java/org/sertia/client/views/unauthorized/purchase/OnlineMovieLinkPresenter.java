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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.*;

public class OnlineMovieLinkPresenter extends BasicPresenterWithValidations implements Initializable {
    @FXML
    public TextField movieName;
    public TextField nameTxtField;
    public TextField phoneTxTextField;
    public TextField emailTxTextField;
    public Label numberOfRentalDays;
    public Label numberOfRentalDaysLabel;
    public DatePicker datePickerTo;
    public DatePicker datePickerFrom;
    public ComboBox<Integer> hourPicker;
    public ComboBox<Integer> minutePicker;
    boolean isFromDateSet;
    long totalDays;

    @FXML
    public void back() throws IOException {
        BuyOnlineScreeningLinkDataHolder.getInstance().clear();
        App.setRoot("unauthorized/sertiaCatalogPresenter");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        totalDays = -1;
        isFromDateSet = false;
        if (BuyOnlineScreeningLinkDataHolder.getInstance().isInitialized()) {
            nameTxtField.setText(BuyOnlineScreeningLinkDataHolder.getInstance().getClientName());
            phoneTxTextField.setText(BuyOnlineScreeningLinkDataHolder.getInstance().getPhone());
            emailTxTextField.setText(BuyOnlineScreeningLinkDataHolder.getInstance().getEmail());
            LocalDateTime chosenStartDateTime = BuyOnlineScreeningLinkDataHolder.getInstance().getStartDateTime();
            hourPicker.setValue(chosenStartDateTime.getHour());
            minutePicker.setValue(chosenStartDateTime.getMinute());
            datePickerFrom.setValue(chosenStartDateTime.toLocalDate());
            LocalDate incrementedDateTime = chosenStartDateTime.toLocalDate();
            incrementedDateTime.plusDays(BuyOnlineScreeningLinkDataHolder.getInstance().getNumberOfDaysForRental());
            datePickerTo.setValue(incrementedDateTime);
            totalDays = ChronoUnit.DAYS.between(chosenStartDateTime.toLocalDate(), incrementedDateTime);
            numberOfRentalDaysLabel.setVisible(true);
            numberOfRentalDays.setText(String.valueOf(totalDays));
        }
        CinemaScreeningMovie movie = MovieHolder.getInstance().getCinemaScreeningMovie();
        movieName.setText(movie.getMovieDetails().getName());
        datePickerTo.valueProperty().addListener(this::changed);
        datePickerTo.setEditable(false);
        datePickerFrom.setEditable(false);
        datePickerFrom.valueProperty().addListener((observableValue, date, t1) -> {
            LocalDate nowDate = LocalDate.now();
            if (t1.isBefore(nowDate)) {
                datePickerFrom.setStyle("-fx-background-color: #ff1500");
                Utils.popAlert(Alert.AlertType.ERROR, "Buying online streaming link", "Please choose start date greater than today");
                totalDays = -1;
            } else {
                datePickerFrom.setStyle("");
                datePickerTo.setEditable(true);
                totalDays = -1;
            }
        });


        hourPicker.getItems().addAll(HOURS);
        minutePicker.getItems().addAll(MINUTES);
    }

    private void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate date, LocalDate newDate) {
        LocalDate fromDate = datePickerFrom.getValue();
        totalDays = -1;
        numberOfRentalDaysLabel.setVisible(false);
        numberOfRentalDays.setVisible(false);
        if (fromDate.isBefore(newDate)) {
            totalDays = ChronoUnit.DAYS.between(fromDate, newDate);
            numberOfRentalDaysLabel.setVisible(true);
            numberOfRentalDays.setText(String.valueOf(totalDays));
            numberOfRentalDays.setVisible(true);
            datePickerFrom.setStyle("");
            datePickerTo.setStyle("");

        } else {
            Utils.popAlert(Alert.AlertType.ERROR, "Buying online streaming link", "please choose valid date range, start date must be before end date");
            datePickerTo.setStyle("-fx-background-color: #ff1500");
            isFromDateSet = false;
            totalDays = -1;
        }
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
                                Integer.parseInt(String.valueOf(totalDays)),
                                LocalDateTime.of(datePickerFrom.getValue(),
                                        LocalTime.of((int)hourPicker.getSelectionModel().getSelectedItem(),
                                                (int)minutePicker.getSelectionModel().getSelectedItem()))
                        );
                App.setRoot("unauthorized/payment/byCreditCardForm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean isDataValid() {
        boolean isNameValid = isStringNotEmpty(nameTxtField.getText(), "Please insert your name");
        boolean isPhoneValid = isPhoneValid(phoneTxTextField.getText());
        boolean isEmailValid = isEmailValid(emailTxTextField.getText());
        boolean isHourValid = hourPicker.getSelectionModel().getSelectedItem() != null;
        boolean isMinuteValid = minutePicker.getSelectionModel().getSelectedItem() != null;
        if (!isHourValid)
            userMistakes.add(CHOOSE_VALID_HOUR);
        if (!isMinuteValid)
            userMistakes.add(CHOOSE_VALID_MIN);
        return isEmailValid && isPhoneValid && isNameValid && isHourValid && isMinuteValid;
    }
}
