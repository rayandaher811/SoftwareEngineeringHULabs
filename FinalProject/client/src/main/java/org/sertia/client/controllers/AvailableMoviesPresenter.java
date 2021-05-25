package org.sertia.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.sertia.client.communication.ServerCommunicationHandler;
import org.sertia.client.communication.messages.CinemaScreeningMovie;
import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.client.dialogs.AvailableMoviesDialog;

import java.net.URL;
import java.util.ResourceBundle;

public class AvailableMoviesPresenter implements Initializable {

    @FXML
    private ListView<CinemaScreeningMovie> lstView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<CinemaScreeningMovie> list = FXCollections.observableArrayList();
        MoviesCatalog catalog = ServerCommunicationHandler.getInstance().getMoviesCatalog();
        list.addAll(catalog.getMoviesCatalog());
        lstView.setItems(list);
    }
}
