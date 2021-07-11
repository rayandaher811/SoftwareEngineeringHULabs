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

import static org.sertia.client.Constants.*;

public class RemoveMovie extends BasicPresenterWithValidations implements Initializable {

    @FXML
    private ComboBox<String> movieToRemove;
    private HashMap<String, SertiaMovie> movieNameToId;
    protected String alertData;

    @FXML
    public void requestRemove() {
        if (isInputValid()) {
            int movieId = movieNameToId.get(movieToRemove.getSelectionModel().getSelectedItem()).getMovieId();
            SertiaBasicResponse response = ClientCatalogControl.getInstance().tryRemoveMovie(movieId);

            if (response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.INFORMATION, REMOVE_FROM_SERTIA_SYSTEM, REMOVE_ENDED_SUCCESSFULLY);
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, REMOVE_FROM_SERTIA_SYSTEM, response.failReason);
            }
            try {
                App.setRoot("authorized/employeesForm");
            } catch (IOException e) {
                e.printStackTrace();
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
        alertData = "";
        SertiaCatalogResponse response = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
        if (!response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.ERROR, MOVIES_CATALOG_FETCH, FETCH_MOVIE_ERROR + response.failReason);
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
        if (selectedItem == null) {
            alertData = MUST_SPECIFIY_A_MOVIE_TO_REMOVE;
            return false;
        }

        return true;
    }
}
