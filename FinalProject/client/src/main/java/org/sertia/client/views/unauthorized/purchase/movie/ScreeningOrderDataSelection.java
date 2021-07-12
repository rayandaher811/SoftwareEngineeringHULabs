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
import org.sertia.client.views.Utils;
import org.sertia.client.views.BasicPresenterWithValidations;
import org.sertia.contracts.covidRegulations.responses.ClientCovidRegulationsStatus;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.screening.ticket.response.ClientSeatMapResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.sertia.client.Constants.*;

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

    private int usedTickets;

    public void back() {
        try {
            App.setRoot("unauthorized/sertiaCatalogPresenter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void next() {
        if (isInputValid()) {
            try {
                NumberOfTicketsHolder.getInstance().setNumberOfTickets(
                        Integer.parseInt(numberOfTicketsToPurchase.getText()));
                if (ClientCovidRegulationsControl.getInstance().getCovidRegulationsStatus().isActive) {
                    Utils.popAlert(Alert.AlertType.INFORMATION, COVID_NOTIFICATION, SEATS_BY_SYSTEM_DECISION_MSG);
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
        ClientCovidRegulationsStatus response = ClientCovidRegulationsControl.getInstance().getCovidRegulationsStatus();
        if (response.isActive) {
            int hallCapacity = seatMapResponse.hallSeats.size();
            int allowedCapacity = getMaxTicketsForHall(hallCapacity, response.maxNumberOfPeople);
            usedTickets = hallCapacity - numberOfFreeTickets;
            int allowedToBuy = allowedCapacity - usedTickets;
            numberOfFreeSeatsLabel.setText(String.valueOf(allowedToBuy));
        } else {
            numberOfFreeSeatsLabel.setText(String.valueOf(numberOfFreeTickets));
        }
    }

    private String parseTimeWithoutDate(String dateTime) {
        int pivotIndex = dateTime.indexOf(":");
        return dateTime.substring(pivotIndex - 2, pivotIndex + 3);
    }

    private int getMaxTicketsForHall(int hallCapacity, int maxNumberOfPeople) {
        if (hallCapacity > 1.2 * maxNumberOfPeople) {
            return maxNumberOfPeople;
        }

        if (hallCapacity > 0.8 * maxNumberOfPeople) {
            return (int) Math.floor(0.8 * maxNumberOfPeople);
        }

        return Math.floorDiv(hallCapacity, 2);
    }

    @Override
    protected boolean isDataValid() {
        ClientCovidRegulationsStatus response = ClientCovidRegulationsControl.getInstance().getCovidRegulationsStatus();

        if (!response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.ERROR, COVID_REGULATIONS_TITLE, response.failReason);
        } else {
            boolean isNumberOfTicketsValid = isItNumber(numberOfTicketsToPurchase.getText(),
                    NUMBER_OF_TICKETS_MUST_BE_A_NUMBER);
            boolean isEnoughSpaceInHall = false;
            if (isNumberOfTicketsValid) {
                isEnoughSpaceInHall = Integer.parseInt(numberOfTicketsToPurchase.getText()) <= numberOfFreeTickets;
                if (!isEnoughSpaceInHall) {
                    userMistakes.add(PURCHASE_FAILED_SINCE_THERE_ARE_NO_LEFT_SEATS_AT_HALL);
                }

                int numOfTicketsToPurchase = Integer.parseInt(numberOfTicketsToPurchase.getText());
                boolean isNotOverTheLimit = true;
                if (usedTickets + numOfTicketsToPurchase > response.maxNumberOfPeople){
                    isNotOverTheLimit = false;
                    userMistakes.add(EXCEEDED_TAV_SAGOL_MAX_ALLOWED_PEOPLE_REGULATIONS);
                }
                return isEnoughSpaceInHall && isNotOverTheLimit;
            }
        }

        return false;
    }
}