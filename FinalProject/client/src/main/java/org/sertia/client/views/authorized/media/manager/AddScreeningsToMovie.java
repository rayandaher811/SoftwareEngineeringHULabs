package org.sertia.client.views.authorized.media.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.SertiaMovieHolder;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddScreeningsToMovie implements Initializable {
    @FXML
    private ComboBox branchName;

    public void addScreenings() {
        SertiaMovie currentMovie = SertiaMovieHolder.getInstance().getSertiaMovie();

        // add screenings to currentMovie
        SertiaBasicResponse response = ClientCatalogControl.getInstance().tryCreateMovie(currentMovie);
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
    }

    public void back() {
        try {
            App.setRoot("authorized/media.manager/addNewMovieData");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> ticketTypes = FXCollections.observableList(ClientCatalogControl.getInstance().getAllBranchesName());
        branchName.setItems(ticketTypes);
    }
}
