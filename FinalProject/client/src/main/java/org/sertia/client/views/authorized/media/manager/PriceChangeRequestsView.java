package org.sertia.client.views.authorized.media.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.controllers.ClientPriceChangeControl;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.views.TicketType;
import org.sertia.client.views.Utils;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;
import org.sertia.contracts.price.change.ClientTicketType;

import java.io.IOException;
import java.net.URL;
import java.util.*;

// TODO: use infra
public class PriceChangeRequestsView extends BasicPresenterWithValidations implements Initializable {

    public ComboBox<TicketType> availableTicketsType;
    @FXML
    private ComboBox<SertiaMovie> moviesComboBox;
    @FXML
    private Label movieNameLabel;

    @FXML
    private TextField movieTicketPriceTxt;
    private String errorMessage;

    private SertiaMovie chosenMovie;

    @FXML
    public void requestPriceChange() {
        if (isDataValid()) {
            ClientTicketType ticketType = availableTicketsType.getValue().ticketType;
            int movieId = -1;

            // In case pf voucher price change the movie doesn't matter
            if(ticketType != ClientTicketType.Voucher) {
                SertiaMovie sertiaMovie = moviesComboBox.getSelectionModel().getSelectedItem();
                movieId = sertiaMovie.getMovieId();
            }

            SertiaBasicResponse response =
                    ClientPriceChangeControl.getInstance().requestPriceChange(movieId, ticketType, Double.parseDouble(movieTicketPriceTxt.getText()));
            if (response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.INFORMATION, "Buying from sertia system", "operation ended successfully!");
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, "Buying from sertia system", response.failReason);
            }
            try {
                App.setRoot("authorized/employeesForm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utils.popAlert(Alert.AlertType.ERROR, "Price change request validation failure", errorMessage);
        }
    }

    @FXML
    public void back() {
        try {
            App.setRoot("authorized/employeesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void valueChanged(SertiaMovie sertiaMovie) {
        chosenMovie = sertiaMovie;
        availableTicketsType.getItems().clear();
        HashSet<TicketType> ticketTypes = new HashSet<>();
        if (sertiaMovie.isStreamable) {
            ticketTypes.add(TicketType.STREAMING);
        }

        ticketTypes.add(TicketType.SCREENING);
        ticketTypes.add(TicketType.VOUCHER);
        availableTicketsType.getItems().addAll(ticketTypes);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availableTicketsType.setOnAction(e->
        {
            moviesComboBox.setVisible(availableTicketsType.getValue() != TicketType.VOUCHER);
            movieNameLabel.setVisible(availableTicketsType.getValue() != TicketType.VOUCHER);
        });
        SertiaCatalogResponse response = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
        if (!response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.ERROR, "Fetch movies catalog", "failed fetch catalog, error msg: " + response.failReason);
        } else {
            List<SertiaMovie> catalog = response.movies;
            moviesComboBox.getItems().addAll(catalog);
            moviesComboBox.valueProperty().addListener((observableValue, o, t1) -> {
                valueChanged(t1);
            });
            availableTicketsType.valueProperty().addListener((observableValue, clientTicketType, t1) -> {
                if (t1 != null) {
					if (t1.ticketType == ClientTicketType.Screening && chosenMovie != null) {
	                    movieTicketPriceTxt.setText(String.valueOf(chosenMovie.getTicketPrice()));
	                } else if (t1.ticketType == ClientTicketType.Streaming && chosenMovie != null) {
	                    movieTicketPriceTxt.setText(String.valueOf(chosenMovie.extraDayPrice));
	                } else {
	                    movieTicketPriceTxt.setText(String.valueOf(ClientPurchaseControl.getInstance().getVouchersInfo().price));
	                }
				}
            });

            // Voucher option don't need any movie
            availableTicketsType.getItems().addAll(TicketType.VOUCHER,TicketType.SCREENING);
        }
    }

    @Override
    protected boolean isDataValid() {
        errorMessage = "";
        boolean isMovieSelected = availableTicketsType.getValue() == TicketType.VOUCHER|| isMovieSelected();
        boolean isPriceSetAsExpected = isPriceSetAndValid();
        return isMovieSelected && isPriceSetAsExpected;
    }

    private boolean isMovieSelected() {
        if (moviesComboBox.getSelectionModel() != null && moviesComboBox.getSelectionModel().getSelectedItem() != null) {
            return true;
        }
        errorMessage += "Please select movie to change it's ticket's price" + "\n";
        return false;
    }

    private boolean isPriceSetAndValid() {
        try {
            Double.parseDouble(movieTicketPriceTxt.getText());
            return true;
        } catch (Exception e) {
            errorMessage += "Please set valid non-empty ticket price value" + "\n";
            return false;
        }
    }
}