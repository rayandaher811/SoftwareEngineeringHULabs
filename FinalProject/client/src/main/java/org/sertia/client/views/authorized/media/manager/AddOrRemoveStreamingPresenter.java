package org.sertia.client.views.authorized.media.manager;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.views.Utils;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static org.sertia.client.Constants.ADD_STREAMING_LINK;
import static org.sertia.client.Constants.REMOVE_STREAMING_LINK;

public class AddOrRemoveStreamingPresenter implements Initializable {
    public ComboBox availableMovies;
    public Button addOrRemoveBtn;
    public TextField streamingPriceTxt;
    private HashMap<String, SertiaMovie> movieNameToId;
    protected String alertData;

    @FXML
    public void back() {
        try {
            App.setRoot("authorized/employeesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertData = "";
        SertiaCatalogResponse response = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
        if (!response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.ERROR, "Fetch movies catalog", "failed fetch catalog, error msg: " + response.failReason);
        } else {
            List<SertiaMovie> catalog = response.movies;
            movieNameToId = new HashMap<>();
            catalog.forEach(sertiaMovie -> movieNameToId.put(sertiaMovie.getMovieDetails().getName(), sertiaMovie));
            ObservableList<String> ticketTypes = FXCollections.observableList(new ArrayList<>(movieNameToId.keySet()));
            availableMovies.setItems(ticketTypes);
            availableMovies.getSelectionModel().selectedItemProperty().addListener(this::onChange);
        }
    }

    public void addOrRemove(MouseEvent mouseEvent) {
        ObservableValue observableValue = availableMovies.getSelectionModel().selectedItemProperty();
        SertiaBasicResponse response = null;
        if (isStreamable(observableValue.getValue())) {
            response =
                    ClientCatalogControl.getInstance().tryRemoveStreaming(movieNameToId.get(observableValue.getValue()).getMovieId());
        } else {
            if (isInputValid()) {
                response =
                        ClientCatalogControl.getInstance().tryAddStreaming(movieNameToId.get(observableValue.getValue()).getMovieId(),
                                Double.parseDouble(streamingPriceTxt.getText()));
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, "Add streaming to movie", "Please insert valid number represents link's price");
            }
        }
        if (response != null) {
            if (response.isSuccessful) {
                Utils.popAlert(Alert.AlertType.INFORMATION, "Add screening dialog", "Screening added successfully!");
                try {
                    App.setRoot("authorized/employeesForm");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.popAlert(Alert.AlertType.ERROR, "Add screening dialog", response.failReason);
            }
        }
    }

    private void onChange(ObservableValue observableValue, Object o, Object o1) {
        if (isStreamable(o1)) {
            addOrRemoveBtn.setText(REMOVE_STREAMING_LINK);
            streamingPriceTxt.setVisible(false);
        } else {
            addOrRemoveBtn.setText(ADD_STREAMING_LINK);
            streamingPriceTxt.setVisible(true);
        }
    }

    private boolean isInputValid() {
        try {
            Double.parseDouble(streamingPriceTxt.getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isStreamable(Object o) {
        return movieNameToId.get(o).isStreamable;
    }
}
