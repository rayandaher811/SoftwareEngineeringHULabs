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
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.screening.ticket.request.ScreeningTicketWithCovidRequest;
import org.sertia.contracts.screening.ticket.response.ScreeningPaymentResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SpecificScreeningPurchaseView extends BasicPresenterWithValidations implements Initializable {
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

    public void back() {
        try {
            App.setRoot("unauthorized/moviesCatalogPresenter");
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
//                    ScreeningTicketWithCovidRequest request = new ScreeningTicketWithCovidRequest();
//                    ScreeningPaymentResponse response = ClientPurchaseControl.getInstance().purchaseScreeningTicketsWithCovid();
                    popupAlert();
                    App.setRoot("unauthorized/paymentView");
                } else {
                    App.setRoot("unauthorized/seatMapView");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientScreening screening = ScreeningHolder.getInstance().getScreening();
        ClientMovie movie = MovieHolder.getInstance().getMovie();
        movieNameLabel.setText(movie.getName());
        branchNameLabel.setText(screening.getCinemaName());
        hallNumber.setText(String.valueOf(screening.getHallId()));
        String movieScreeningTime = screening.getScreeningTime().toString();
        DateTime dateTime = DateTime.parse(movieScreeningTime);
        datePickerComp.setValue(LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()));
        screeningTimeTxt.setText(parseTimeWithoutDate(movieScreeningTime));
        numberOfTicketsToPurchase.setFocusTraversable(false);
    }

    private String parseTimeWithoutDate(String dateTime) {
        int pivotIndex = dateTime.indexOf(":");
        return dateTime.substring(pivotIndex - 2, pivotIndex + 3);
    }

    @Override
    protected boolean isDataValid() {
        boolean isNumberOfTicketsValid = isItNumber(numberOfTicketsToPurchase.getText(),
                "Number of tickets must appear and be a number!");
        return isNumberOfTicketsValid;
    }
}