package org.sertia.client.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sertia.client.App;
import org.sertia.client.communication.SertiaClient;
import org.sertia.client.communication.messages.CinemaScreeningMovie;
import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.client.global.LoggedInUser;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AvailableMoviesForEdit implements Initializable {

    @FXML
    private Accordion moviesAccordion;

    private HashMap<String, ArrayList<CinemaScreeningMovie>> mapMoviesByMovieNameToCinemaScreeningMovie(Map.Entry<String,
            ArrayList<CinemaScreeningMovie>> cinemaToScreeningMovies){
        HashMap<String, ArrayList<CinemaScreeningMovie>> movieToDetailsMapping = new HashMap<>();
        for (int i = 0; i < cinemaToScreeningMovies.getValue().size(); i++) {
            final CinemaScreeningMovie screeningMovie = cinemaToScreeningMovies.getValue().get(i);
            if (movieToDetailsMapping.containsKey(screeningMovie.getName())) {
                movieToDetailsMapping.get(screeningMovie.getName()).add(screeningMovie);
            } else {
                movieToDetailsMapping.put(screeningMovie.getName(), new ArrayList<>() {{
                    add(screeningMovie);
                }});
            }
        }

        return movieToDetailsMapping;
    }

    private TitledPane screeningMovieToTilePane(Map.Entry<String,
            ArrayList<CinemaScreeningMovie>> cinemaToScreeningMovies) {
        Accordion moviesAccordion = new Accordion();
        ObservableList<TitledPane> list = FXCollections.observableArrayList();

        HashMap<String, ArrayList<CinemaScreeningMovie>> moviesToScreening =
                mapMoviesByMovieNameToCinemaScreeningMovie(cinemaToScreeningMovies);
        ArrayList<TitledPane> values = new ArrayList<>();

        moviesToScreening.entrySet().forEach(s -> {
            ListView<Button> buttonListView = new ListView<>();

            ObservableList<Button> buttonObservableList = FXCollections.observableArrayList();
            s.getValue().stream().forEach(cinemaScreeningMovie -> {


                Button btn = new Button();
                btn.setText(cinemaScreeningMovie.getScreeningTimeStampStr());
                buttonObservableList.add(btn);
                btn.setOnMouseClicked(mouseEvent -> {
                    try {
                        LoggedInUser.getInstance().setChosenMovieForUpdateTimeOperation(cinemaScreeningMovie);
                        App.setRoot("editMovieScreeningTimePresenter");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            });
            buttonListView.getItems().setAll(buttonObservableList);
            values.add(new TitledPane(s.getKey(), buttonListView));
        });

        list.addAll(values);
        moviesAccordion.getPanes().setAll(values);

        TitledPane tiledPane = new TitledPane("AllMoviesByCinema", moviesAccordion);
        tiledPane.setAnimated(false);
        tiledPane.setText(cinemaToScreeningMovies.getKey());
//        tiledPane.setContent(moviesAccordion);
        return tiledPane;
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
        MoviesCatalog catalog = SertiaClient.getInstance().getMoviesCatalog();

        HashMap<String, ArrayList<CinemaScreeningMovie>> cinemaNameToMovie = new HashMap<>();

        ArrayList<CinemaScreeningMovie> screeningMovieArrayList = (ArrayList<CinemaScreeningMovie>) catalog.getMoviesCatalog();
        for (int i = 0; i < catalog.getMoviesCatalog().size(); i++) {
            final CinemaScreeningMovie screeningMovie = screeningMovieArrayList.get(i);
            if (cinemaNameToMovie.containsKey(screeningMovie.getBranchName())) {
                cinemaNameToMovie.get(screeningMovie.getBranchName()).add(screeningMovie);
            } else {
                cinemaNameToMovie.put(screeningMovie.getBranchName(), new ArrayList<>() {{
                    add(screeningMovie);
                }});
            }
        }

        cinemaNameToMovie.entrySet().forEach(stringCollectionEntry -> list.add(screeningMovieToTilePane(stringCollectionEntry)));

        moviesAccordion.getPanes().addAll(list);

    }
}
