package org.sertia.client.views;

import javafx.fxml.FXML;
import org.sertia.client.App;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void toLoginView() throws IOException {
        App.setRoot("userLoginForm");
    }

    @FXML
    private void toMoviesCatalog() throws IOException {
        App.setRoot("moviesCatalogPresenter");
    }

    @FXML
    private void toAddComplaintView() throws IOException {
        App.setRoot("createComplaintView");
    }

    @FXML
    private void toMovieTicketsPurchase() throws IOException {
        App.setRoot("purchaseMovieTickets");
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
