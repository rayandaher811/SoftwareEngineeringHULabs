package org.sertia.client.views.unauthorized.purchase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCovidRegulationsControl;

import java.io.IOException;

public class PaymentMethodSelectionPresenter {

    @FXML
    public void toCreditCard() {
        try {
            App.setRoot("unauthorized/payment/byCreditCardForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void toPrepaidTickets() {
        try {
            App.setRoot("unauthorized/payment/purchaseMovieFromPrepaidBalance");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        if (ClientCovidRegulationsControl.getInstance().getCovidRegulationsStatus().isActive) {
            try {
                App.setRoot("unauthorized/movie/screeningOrderDataSelection");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                App.setRoot("unauthorized/movie/seatMapView");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
