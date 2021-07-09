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
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Button removeScreeningBtn;

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
            Alert.AlertType type;
            String msg = "";
            if (response.isSuccessful){
                type = Alert.AlertType.INFORMATION;
                msg = "operation ended successfully!";
            } else {
                type = Alert.AlertType.ERROR;
                msg = response.failReason;
            }
            Alert errorAlert = new Alert(type);
            errorAlert.setTitle("Buying from sertia system");
            errorAlert.setContentText(msg);
            errorAlert.showAndWait();
        }
        App.setRoot("authorized/media.manager/availableMoviesForEdit");
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
            Alert missingKeyFieldsAlert = new Alert(Alert.AlertType.INFORMATION);
            missingKeyFieldsAlert.setTitle("Remove screening");
            missingKeyFieldsAlert.setContentText("Remove operation ended successfully!");
            missingKeyFieldsAlert.show();
            try {
                backToPreviousPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert missingKeyFieldsAlert = new Alert(Alert.AlertType.ERROR);
            missingKeyFieldsAlert.setTitle("Remove screening");
            missingKeyFieldsAlert.setContentText(response.failReason);
            missingKeyFieldsAlert.show();
        }
    }
}
