package org.sertia.client.views.authorized.media.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.views.Utils;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class RemoveMovie extends BasicPresenterWithValidations implements Initializable {

    @FXML
    private ComboBox movieToRemove;
    private HashMap<String, SertiaMovie> movieNameToId;
    private boolean isBug;
    protected String alertData;

    @FXML
    public void requestRemove() {
        if (isDataValid()) {
            int movieId = movieNameToId.get(movieToRemove.getSelectionModel().getSelectedItem()).getMovieId();
            SertiaBasicResponse response = ClientCatalogControl.getInstance().tryRemoveMovie(movieId);

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
            if (isBug) {
                Utils.popAlert(Alert.AlertType.ERROR, "Bug!", alertData);
            } else {
                Utils.popAlert(Alert.AlertType.WARNING, "Bug!", "Invalid using popup");
            }
        }
    }

    @FXML
    public void backToMenu() {
        try {
            App.setRoot("authorized/media.manager/addOrRemoveMovie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isBug = false;
        alertData = "";
        SertiaCatalogResponse response = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
        if (!response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.ERROR, "Fetch movies list", "failed fetch catalog, error msg: " + response.failReason);
        } else {
            List<SertiaMovie> catalog = response.movies;
            movieNameToId = new HashMap<>();
            catalog.forEach(sertiaMovie -> movieNameToId.put(sertiaMovie.getMovieDetails().getName(), sertiaMovie));
            ObservableList<String> ticketTypes = FXCollections.observableList(new ArrayList<>(movieNameToId.keySet()));
            movieToRemove.setItems(ticketTypes);
        }
    }

    @Override
    protected boolean isDataValid() {
        Object selectedItem = movieToRemove.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (selectedItem instanceof String) {
                return true;
            } else {
                isBug = true;
                alertData = "FATAL ERROR - BUG - Couldn't deserialize combobox option from object to sertia moview";
            }
        } else {
            alertData = "You should choose a movie to remove";
        }
        return false;
    }
}
