package org.sertia.client.views;

import javafx.fxml.FXML;
import org.sertia.client.App;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void toLoginView() throws IOException {
        App.setRoot("authorized/userLoginForm");
    }

    @FXML
    private void toMoviesCatalog() throws IOException {
        App.setRoot("unauthorized/moviesCatalogPresenter");
    }

    @FXML
    private void toAddComplaintView() throws IOException {
        App.setRoot("unauthorized/createComplaintView");
    }

    @FXML
    private void toMovieTicketsPurchase() throws IOException {
        App.setRoot("unauthorized/purchaseMovieTickets");
    }

    @FXML
    private void toPrepaidPurchaseView() throws IOException {
        App.setRoot("unauthorized/prepaidTicketsPurchaseForm");
    }

    @FXML
    private void toHomeLinkView() throws IOException {
        App.setRoot("unauthorized/onlineWatchLinkForm");
    }
}
