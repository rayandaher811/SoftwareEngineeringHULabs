package org.sertia.client.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.sertia.client.App;
import org.sertia.client.communication.SertiaClient;
import org.sertia.client.communication.messages.MoviesCatalog;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class OnlineMovieLinkForm implements Initializable {
    // TODO: set to list view of buttons or somethong and popup when someone clicks
    @FXML
    private ListView<String> lstView;

    @FXML
    public void toMain() throws IOException {
        App.setRoot("primary");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        HashSet<String> availableMovies = new HashSet<>();
        ObservableList<String> list = FXCollections.observableArrayList();
        MoviesCatalog catalog = SertiaClient.getInstance().getMoviesCatalog();
        catalog.getMoviesCatalog().forEach(cinemaScreeningMovie -> availableMovies.add(cinemaScreeningMovie.getName()));
        list.addAll(availableMovies);
        lstView.setItems(list);
    }
}
