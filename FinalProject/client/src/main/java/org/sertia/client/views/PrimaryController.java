package org.sertia.client.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.SpecificViewHolder;
import org.sertia.contracts.movies.catalog.response.CinemaAndHallsResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.*;

public class PrimaryController implements Initializable {
    public Button availableMoviesInSertiaBtn;
    public Label welcomeLabel;

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

    @FXML
    private void toStreamLink() throws IOException {
        App.setRoot("unauthorized/streamLink");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (System.getenv(SPECIFIC_BRANCH) != null && !System.getenv(SPECIFIC_BRANCH).isEmpty()) {
            CinemaAndHallsResponse response = ClientCatalogControl.getInstance().getCinemasAndHalls();
            if (!response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.ERROR, PRIMARY_CONTROLLER, CINIMAS_LIST_FETCH_ERROR);
            } else {
                String cinemaName = System.getenv(SPECIFIC_BRANCH);
                if (response.cinemaToHalls.keySet().contains(cinemaName)) {
                    SpecificViewHolder.getInstance().initializeSpecificBranch(cinemaName);
                    welcomeLabel.setText(VIEW_WORKSTATION + cinemaName);
                    availableMoviesInSertiaBtn.setText(AVAILABLE_MOVIES_IN_BRANCH);
                }
            }
        }
    }
}
