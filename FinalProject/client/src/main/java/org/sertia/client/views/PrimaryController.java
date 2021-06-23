package org.sertia.client.views;

import javafx.fxml.FXML;
import org.sertia.client.App;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void toLoginView() throws IOException {
        App.setRoot("loginForm");
    }

    @FXML
    private void toAvailableMoviesView() throws IOException {
        App.setRoot("availableMoviesPresenter");
    }

    @FXML
    private void toComplaintsView() throws IOException {
        App.setRoot("complaintsView");
    }

    @FXML
    private void toMovieTicketsPurchase() throws IOException {
        boolean isTavSagolRunning = true;
        if (isTavSagolRunning){
            App.setRoot("covidMovieTicketsPurchase");
        } else {
            App.setRoot("movieTicketPurchaseForm");
        }
    }

    @FXML
    private void toPrepaidPurchaseView() throws IOException {
        App.setRoot("prepaidTicketsPurchaseForm");
    }

    @FXML
    private void toHomeLinkView() throws IOException {
        App.setRoot("onlineWatchLinkForm");
    }
}
