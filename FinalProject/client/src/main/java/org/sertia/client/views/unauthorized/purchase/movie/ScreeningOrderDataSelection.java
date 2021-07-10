package org.sertia.client.views.unauthorized.purchase.movie;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.joda.time.DateTime;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCovidRegulationsControl;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.global.NumberOfTicketsHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.screening.ticket.response.ClientSeatMapResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ScreeningOrderDataSelection extends BasicPresenterWithValidations implements Initializable {
    public Label numberOfFreeSeatsLabel;
    @FXML
    private TextField numberOfTicketsToPurchase;
    @FXML
    private Label movieNameLabel;
    @FXML
    private Label branchNameLabel;
    @FXML
    private Label hallNumber;
    @FXML
    private DatePicker datePickerComp;
    @FXML
    private TextField screeningTimeTxt;

    private int numberOfFreeTickets;
    public void back() {
        try {
            App.setRoot("unauthorized/sertiaCatalogPresenter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void popupAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setTitle("Covid19 notification");
        errorAlert.setContentText("Were sorry, according to TAV-SAGOL rules, we will choose seats for you");
        errorAlert.showAndWait();
    }

    public void next() {
        if (isInputValid()) {
            try {
                NumberOfTicketsHolder.getInstance().setNumberOfTickets(
                        Integer.parseInt(numberOfTicketsToPurchase.getText()));
                if (ClientCovidRegulationsControl.getInstance().getCovidRegulationsStatus().isActive) {
                    popupAlert();
                    App.setRoot("unauthorized/payment/selectionMethodForm");
                } else {
                    App.setRoot("unauthorized/movie/seatMapView");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientScreening screening = ScreeningHolder.getInstance().getScreening();
        ClientSeatMapResponse seatMapResponse =
                ClientPurchaseControl.getInstance().getScreeningSeatMap(screening.getScreeningId());
        numberOfFreeTickets =
                seatMapResponse
                        .getHallSeats()
                        .stream()
                        .filter(hallSeat -> !hallSeat.isTaken)
                        .collect(Collectors.toSet()).size();
        CinemaScreeningMovie movie = MovieHolder.getInstance().getCinemaScreeningMovie();
        movieNameLabel.setText(movie.getMovieDetails().getName());
        branchNameLabel.setText(screening.getCinemaName());
        hallNumber.setText(String.valueOf(screening.getHallId()));
        String movieScreeningTime = screening.getScreeningTime().toString();
        DateTime dateTime = DateTime.parse(movieScreeningTime);
        datePickerComp.setValue(LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()));
        screeningTimeTxt.setText(parseTimeWithoutDate(movieScreeningTime));
        numberOfTicketsToPurchase.setFocusTraversable(false);
        numberOfFreeSeatsLabel.setText(String.valueOf(numberOfFreeTickets));
    }

    private String parseTimeWithoutDate(String dateTime) {
        int pivotIndex = dateTime.indexOf(":");
        return dateTime.substring(pivotIndex - 2, pivotIndex + 3);
    }

    @Override
    protected boolean isDataValid() {
        boolean isNumberOfTicketsValid = isItNumber(numberOfTicketsToPurchase.getText(),
                "Number of tickets must appear and be a number!");
        boolean isEnoughSpaceInHall = false;
        if (isNumberOfTicketsValid) {
            isEnoughSpaceInHall = Integer.parseInt(numberOfTicketsToPurchase.getText()) <= numberOfFreeTickets;
            if (!isEnoughSpaceInHall){
                userMistakes.add("You requested to buy more tickets than available in hall");
            }
        }
        return isNumberOfTicketsValid && isEnoughSpaceInHall;
    }
}