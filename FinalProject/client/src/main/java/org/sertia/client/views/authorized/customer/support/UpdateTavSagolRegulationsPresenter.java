package org.sertia.client.views.authorized.customer.support;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCovidRegulationsControl;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.covidRegulations.responses.ClientCovidRegulationsStatus;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.*;

public class UpdateTavSagolRegulationsPresenter implements Initializable {
    public DatePicker fromDatePickerComp;
    public DatePicker toDatePickerComp;
    public TextField maxAllowedPeopleTxt;
    public Button updateMaxPeopleBtn;
    public Button backBtn;
    public Button enableOrDisableRegulationsBtn;
    public Button startOrStopRegulationsBtn;
    public Label tavSagolLabel;
    private ClientCovidRegulationsStatus covidRegulationsStatus;

    private boolean areDatesValid() {
        LocalDate fromDate = fromDatePickerComp.getValue();
        LocalDate toDate = toDatePickerComp.getValue();
        return fromDate.isBefore(toDate);
    }

    private boolean isMaximumCapacityIsNumber() {
        try {
            Integer.parseInt(maxAllowedPeopleTxt.getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private LocalDateTime localDateToLocalDateTime(LocalDate date) {
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0 ,0);
    }

    @FXML
    public void updateRegulations() {
        if (areDatesValid()) {
            LocalDate fromDate = fromDatePickerComp.getValue();
            LocalDate toDate = toDatePickerComp.getValue();
            LocalDateTime fromDateLocalDateTime = localDateToLocalDateTime(fromDate);
            LocalDateTime toDateLocalDateTime = localDateToLocalDateTime(toDate);
            SertiaBasicResponse response =
                    ClientCovidRegulationsControl.getInstance().cancelAllScreeningsDueCovid(fromDateLocalDateTime, toDateLocalDateTime);
            if (response != null) {
                if (response.isSuccessful) {
                    Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                    errorAlert.setTitle("Update covid regulations by date");
                    errorAlert.setContentText("Covid regulations by date range set successfully!");
                    errorAlert.showAndWait();
                    back();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Failed to set regulations");
                    errorAlert.setContentText(response.failReason);
                    errorAlert.showAndWait();
                }
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Failed to set regulations");
            errorAlert.setContentText("Choose dates in incremental order");
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void updateMaximumCapacity() {
        if (isMaximumCapacityIsNumber()) {
            int newMaxCapacity = Integer.parseInt(maxAllowedPeopleTxt.getText());
            SertiaBasicResponse response =
                    ClientCovidRegulationsControl.getInstance().updateCovidCrowdingRegulationsRequest(newMaxCapacity);
            if (response != null) {
                if (response.isSuccessful) {
                    Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                    errorAlert.setTitle("Update covid regulations by max allowed people in hall");
                    errorAlert.setContentText("Covid regulations by max amount of people set successfully!");
                    errorAlert.showAndWait();
                    back();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Failed to set max people in hall regulations");
                    errorAlert.setContentText(response.failReason);
                    errorAlert.showAndWait();
                }
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Failed to set regulations");
            errorAlert.setContentText("new amount of people in hall must be a number");
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void back() {
        try {
            App.setRoot("authorized/employeesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: if there are regulations, in specific dates, client don't know them and cannot show that
        covidRegulationsStatus = ClientCovidRegulationsControl.getInstance().getCovidRegulationsStatus();
        if (covidRegulationsStatus.isActive) {
            maxAllowedPeopleTxt.setText(String.valueOf(covidRegulationsStatus.maxNumberOfPeople));
            startOrStopRegulationsBtn.setText(CANCEL_REGULATIONS);
            startOrStopRegulationsBtn.setStyle("-fx-background-color: #ff1500");
        } else {
            tavSagolLabel.setText(tavSagolLabel.getText() + " , " + TAV_SAGOL_LABEL_TXT);
            startOrStopRegulationsBtn.setStyle("-fx-background-color: #00ff00");
            startOrStopRegulationsBtn.setText(START_TAV_SAGOL_REGULATIONS_TXT);
        }
    }

    public void startOrStopRegulation(ActionEvent actionEvent) {
        SertiaBasicResponse response = null;
        if (covidRegulationsStatus.isActive) {
            response = ClientCovidRegulationsControl.getInstance().cancelRegulations();
        } else {
            response = ClientCovidRegulationsControl.getInstance().activeRegulations();
        }

        Alert.AlertType type;
        String msg;
        if (response.isSuccessful) {
            type = Alert.AlertType.INFORMATION;
            msg = "Regulations update has finished successfully!";
            Alert alert = new Alert(type);
            alert.setTitle("Update tav sagol regulations");
            alert.setContentText(msg);
            alert.showAndWait();
            back();
        } else {
            type = Alert.AlertType.ERROR;
            msg = response.failReason;
            Alert alert = new Alert(type);
            alert.setTitle("Update tav sagol regulations");
            alert.setContentText(msg);
            alert.showAndWait();
        }


    }
}
