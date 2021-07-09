package org.sertia.client.views.unauthorized.movies;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AbstractMoviesPresenter {

    @FXML
    private Accordion moviesAccordion;

    private HashMap<String, ArrayList<CinemaScreeningMovie>> mapMoviesByMovieNameToCinemaScreeningMovie(Map.Entry<String,
            ArrayList<CinemaScreeningMovie>> cinemaToScreeningMovies) {
        HashMap<String, ArrayList<CinemaScreeningMovie>> movieToDetailsMapping = new HashMap<>();
        for (int i = 0; i < cinemaToScreeningMovies.getValue().size(); i++) {
            final CinemaScreeningMovie screeningMovie = cinemaToScreeningMovies.getValue().get(i);
            if (movieToDetailsMapping.containsKey(screeningMovie.getMovieDetails().getName())) {
                movieToDetailsMapping.get(screeningMovie.getMovieDetails().getName()).add(screeningMovie);
            } else {
                movieToDetailsMapping.put(screeningMovie.getMovieDetails().getName(), new ArrayList<>() {{
                    add(screeningMovie);
                }});
            }
        }

        return movieToDetailsMapping;
    }

//    private TitledPane screeningMovieToTilePane(Map.Entry<String,
//            ArrayList<CinemaScreeningMovie>> cinemaToScreeningMovies) {
//        Accordion moviesAccordion = new Accordion();
//        ObservableList<TitledPane> list = FXCollections.observableArrayList();
//
//        HashMap<String, ArrayList<CinemaScreeningMovie>> moviesToScreening =
//                mapMoviesByMovieNameToCinemaScreeningMovie(cinemaToScreeningMovies);
//        ArrayList<TitledPane> values = new ArrayList<>();
//
//        moviesToScreening.entrySet().forEach(s -> {
//            ListView<Button> buttonListView = new ListView<>();
//
//            ObservableList<Button> buttonObservableList = FXCollections.observableArrayList();
//            s.getValue().stream().forEach(cinemaScreeningMovie -> {
//
//
//                Button btn = new Button();
//                btn.setText(cinemaScreeningMovie.getScreeningTimeStampStr());
//                buttonObservableList.add(btn);
//                btn.setOnMouseClicked(mouseEvent -> {
//                    try {
//                        LoggedInUser.getInstance().setChosenMovieForUpdateTimeOperation(cinemaScreeningMovie);
//                        App.setRoot("authorized/media.manager/editMovieScreeningTimePresenter");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//
//            });
//            buttonListView.getItems().setAll(buttonObservableList);
//            values.add(new TitledPane(s.getKey(), buttonListView));
//        });
//
//        list.addAll(values);
//        moviesAccordion.getPanes().setAll(values);
//
//        TitledPane tiledPane = new TitledPane("AllMoviesByCinema", moviesAccordion);
//        tiledPane.setAnimated(false);
//        tiledPane.setText(cinemaToScreeningMovies.getKey());
////        tiledPane.setContent(moviesAccordion);
//        return tiledPane;
//    }

}
