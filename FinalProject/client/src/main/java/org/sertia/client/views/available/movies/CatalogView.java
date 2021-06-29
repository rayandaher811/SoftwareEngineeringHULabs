package org.sertia.client.views.available.movies;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.sertia.client.App;
import org.sertia.client.communication.SertiaClient;
import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.client.views.BasicView;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

/*
    public int userLoginButton;
    public int openComplaintButton;
    public int purchaseVoucherButton;
    public int createMovieButton;
 */
public class CatalogView extends BasicView implements Initializable {

    @FXML
    private ListView<String> lstView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HashSet<String> availableMovies = new HashSet<>();
        ObservableList<String> list = FXCollections.observableArrayList();
        MoviesCatalog catalog = SertiaClient.getInstance().getMoviesCatalog();
        catalog.getMoviesCatalog().forEach(cinemaScreeningMovie -> availableMovies.add(cinemaScreeningMovie.getMovieDetails().getName()));
        list.addAll(availableMovies);
        lstView.setItems(list);
    }

    @FXML
    private void backToClientsView() throws IOException {
        App.setRoot("primary");
    }
}