package org.sertia.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.sertia.client.App;
import org.sertia.client.communication.ServerCommunicationHandler;
import org.sertia.client.communication.messages.CinemaScreeningMovie;
import org.sertia.client.communication.messages.MoviesCatalog;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AvailableMoviesPresenter implements Initializable {

    @FXML
    private ListView<String> lstView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HashSet<String> availableMovies = new HashSet<>();
        ObservableList<String> list = FXCollections.observableArrayList();
        MoviesCatalog catalog = ServerCommunicationHandler.getInstance().getMoviesCatalog();
        catalog.getMoviesCatalog().forEach(cinemaScreeningMovie -> availableMovies.add(cinemaScreeningMovie.getName()));
        list.addAll(availableMovies);
        lstView.setItems(list);
    }

    @FXML
    private void backToClientsView() throws IOException {
        App.setRoot("customerView");
    }
}
