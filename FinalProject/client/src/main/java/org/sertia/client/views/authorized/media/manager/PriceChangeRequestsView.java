package org.sertia.client.views.authorized.media.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.controllers.ClientPriceChangeControl;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.price.change.ClientTicketType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
// TODO: use infra
public class PriceChangeRequestsView extends BasicPresenterWithValidations implements Initializable {

    @FXML
    private ComboBox movieToSet;
    @FXML
    private TextField movieTicketPriceTxt;

    private HashMap<String, SertiaMovie> movieNameToId;
    private String errorMessage;

    @FXML
    public void requestPriceChange() {
        if (isDataValid()){
//            int movieId, ClientTicketType clientTicketType, double newPrice
            SertiaMovie sertiaMovie = movieNameToId.get(movieToSet.getSelectionModel().getSelectedItem());
            int movieId = sertiaMovie.getMovieId();
            // TODO: how can we know that???
            ClientTicketType ticketType = ClientTicketType.Screening;
            SertiaBasicResponse response =
                    ClientPriceChangeControl.getInstance().requestPriceChange(movieId, ticketType, Double.parseDouble(movieTicketPriceTxt.getText()));
            Alert.AlertType type;
            String msg = "";
            if (response.isSuccessful){
                type = Alert.AlertType.INFORMATION;
                msg = "operation ended successfully!";
            } else {
                type = Alert.AlertType.ERROR;
                msg = response.failReason;
            }
            Alert errorAlert = new Alert(type);
            errorAlert.setTitle("Buying from sertia system");
            errorAlert.setContentText(msg);
            errorAlert.showAndWait();
            try {
                App.setRoot("authorized/employeesForm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Price change request validation failure");
            errorAlert.setContentText(errorMessage);
            errorAlert.show();
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

    public void valueChanged(String movieName) {
        movieTicketPriceTxt.setText(String.valueOf(movieNameToId.get(movieName).getTicketPrice()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<SertiaMovie> catalog = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
        movieNameToId = new HashMap<>();
        catalog.forEach(sertiaMovie -> movieNameToId.put(sertiaMovie.getMovieDetails().getName(), sertiaMovie));
        ObservableList<String> ticketTypes = FXCollections.observableList(new ArrayList<>(movieNameToId.keySet()));
        movieToSet.setItems(ticketTypes);
        movieToSet.valueProperty().addListener((observableValue, o, t1) -> {
            valueChanged((String)t1);
        });
    }

    @Override
    protected boolean isDataValid() {
        errorMessage = "";
        boolean isMovieSelected = isMovieSelected();
        boolean isPriceSetAsExpected = isPriceSetAndValid();
        return isMovieSelected && isPriceSetAsExpected;
    }

    private boolean isMovieSelected() {
        if (movieToSet.getSelectionModel() != null && movieToSet.getSelectionModel().getSelectedItem() != null){
            return true;
        }
        errorMessage += "Please select movie to change it's ticket's price" + "\n";
        return false;
    }

    private boolean isPriceSetAndValid() {
        try {
            double newPrice = Double.parseDouble(movieTicketPriceTxt.getText());
            return true;
        } catch (Exception e) {
            errorMessage += "Please set valid non-empty ticket price value" + "\n";
            return false;
        }
    }
}