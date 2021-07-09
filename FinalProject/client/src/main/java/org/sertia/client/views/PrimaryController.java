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
        App.setRoot("unauthorized/sertiaCatalogPresenter");
    }

    @FXML
    private void toAddComplaintView() throws IOException {
        App.setRoot("unauthorized/complaint/createComplaintView");
    }

    @FXML
    private void toPrepaidPurchaseView() throws IOException {
        App.setRoot("unauthorized/prepaid/prepaidTicketsPurchaseForm");
    }

    @FXML
    private void toPrepaidBalance() throws IOException {
        App.setRoot("unauthorized/prepaid/checkCurrentBalance");
    }

    @FXML
    private void toCancelPurchase() throws IOException {
        App.setRoot("unauthorized/cancelPurchaseForm");
    }
}
