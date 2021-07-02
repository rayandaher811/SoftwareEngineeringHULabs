package org.sertia.client.views.available.movies;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import org.sertia.client.App;
import org.sertia.client.communication.messages.CinemaScreeningMovie;
import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.client.global.LoggedInUser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AvailableMoviesForEdit extends AbstractMoviesPresenter implements Initializable {

    public AvailableMoviesForEdit(){
        super();
    }
    @FXML
    private void back() throws IOException {
        LoggedInUser.onDisconnection();
        App.setRoot("primary");
    }

    @FXML
    private void updateMovie() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<TitledPane> list = FXCollections.observableArrayList();
        // Need to get movies by start time

        HashMap<String, ArrayList<CinemaScreeningMovie>> cinemaNameToMovie = new HashMap<>();

//        ArrayList<CinemaScreeningMovie> screeningMovieArrayList = (ArrayList<CinemaScreeningMovie>) catalog.getMoviesCatalog();
//        for (int i = 0; i < catalog.getMoviesCatalog().size(); i++) {
//            final CinemaScreeningMovie screeningMovie = screeningMovieArrayList.get(i);
//            if (cinemaNameToMovie.containsKey(screeningMovie.getBranchName())) {
//                cinemaNameToMovie.get(screeningMovie.getBranchName()).add(screeningMovie);
//            } else {
//                cinemaNameToMovie.put(screeningMovie.getBranchName(), new ArrayList<>() {{
//                    add(screeningMovie);
//                }});
//            }
//        }
//
//        cinemaNameToMovie.entrySet().forEach(stringCollectionEntry -> list.add(screeningMovieToTilePane(stringCollectionEntry)));
//
//        moviesAccordion.getPanes().addAll(list);

    }
}
