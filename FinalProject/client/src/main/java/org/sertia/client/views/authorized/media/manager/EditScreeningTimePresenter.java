package org.sertia.client.views.authorized.media.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.client.views.Utils;
import org.sertia.client.views.unauthorized.movies.AbstractMoviesPresenter;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.sertia.client.Constants.*;

/**
 * As I understand, this presenter should allow edit just CURRENTLY_PLAYING movies!
 */
public class EditScreeningTimePresenter extends AbstractMoviesPresenter implements Initializable {
    @FXML
    private Accordion moviesKindAndDataAccordion;

    public EditScreeningTimePresenter() {
        super();
    }

    @FXML
    public void back() throws IOException {
        App.setRoot("authorized/employeesForm");
    }

    private HashMap<SertiaMovie, HashMap<String, List<ClientScreening>>> cinemaToScreenings(ArrayList<SertiaMovie> movies) {
        HashMap<SertiaMovie, HashMap<String, List<ClientScreening>>> movieToCinemaAndScreenings = new HashMap<>();

        for (SertiaMovie screeningMovie : movies) {
            movieToCinemaAndScreenings.put(screeningMovie, new HashMap());
            for (ClientScreening specificScreening : screeningMovie.getScreenings()) {
                if (movieToCinemaAndScreenings.containsKey(screeningMovie)) {
                    if (movieToCinemaAndScreenings.get(screeningMovie).containsKey(specificScreening.getCinemaName())) {
                        movieToCinemaAndScreenings.get(screeningMovie).get(specificScreening.getCinemaName()).add(specificScreening);
                    } else {
                        movieToCinemaAndScreenings.get(screeningMovie).put(specificScreening.getCinemaName(), new ArrayList<>() {{
                            add(specificScreening);
                        }});
                    }
                }
            }
        }

        return movieToCinemaAndScreenings;
    }

    private TitledPane getCurrentlyPlayingMoviesAsTitledPane(Map.Entry<String, ArrayList<SertiaMovie>> currentlyAvailableMovies) {
        Accordion currentlyPlayingMoviesAccordion = new Accordion();

        HashMap<SertiaMovie, HashMap<String, List<ClientScreening>>> branchToMovies = cinemaToScreenings(currentlyAvailableMovies.getValue());
        ArrayList<TitledPane> values = new ArrayList<>();
        for (Map.Entry<SertiaMovie, HashMap<String, List<ClientScreening>>> moviesInBranch : branchToMovies.entrySet()) {
            Accordion moviePlayingTimeAccordion = new Accordion();
            ArrayList<TitledPane> specificCinemaList = new ArrayList<>();
            for (Map.Entry<String, List<ClientScreening>> cinemaToScreenings : moviesInBranch.getValue().entrySet()) {
                ListView<Button> allScreeningsInCinemaOfSpecificMovie = new ListView<>();
                ObservableList<Button> buttonObservableList = FXCollections.observableArrayList();
                cinemaToScreenings.getValue().stream().forEach(cinemaScreeningMovie -> {
                    Button btn = new Button();
                    btn.setText(cinemaScreeningMovie.screeningTime.toString());
                    buttonObservableList.add(btn);
                    btn.setOnMouseClicked(mouseEvent -> {
                        try {
                            ScreeningHolder.getInstance().setScreening(cinemaScreeningMovie);
                            MovieHolder.getInstance().setMovie(moviesInBranch.getKey(), false);
                            App.setRoot("authorized/media.manager/editMovieScreeningTimePresenter");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                });
                allScreeningsInCinemaOfSpecificMovie.getItems().setAll(buttonObservableList);
                Accordion specificCinema = new Accordion();
                TitledPane tiledPane = new TitledPane(cinemaToScreenings.getKey(), specificCinema);
                tiledPane.setAnimated(true);
                tiledPane.setText(cinemaToScreenings.getKey());
                tiledPane.setContent(allScreeningsInCinemaOfSpecificMovie);
                specificCinemaList.add(tiledPane);
            }
            moviePlayingTimeAccordion.getPanes().addAll(specificCinemaList);
            HBox hBox = new HBox();
            Button bgBtn = new Button();
            bgBtn.setText(ADD_SCREENING);
            bgBtn.setOnMouseClicked(mouseEvent -> {
                try {
                    MovieHolder.getInstance().setMovie(moviesInBranch.getKey(), false);
                    App.setRoot("authorized/media.manager/addNewScreening");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            hBox.getChildren().addAll(moviePlayingTimeAccordion, bgBtn);
            TitledPane tiledPane = new TitledPane();
            tiledPane.setAnimated(true);
            tiledPane.setText(moviesInBranch.getKey().getMovieDetails().getName());
            tiledPane.setContent(hBox);
            values.add(tiledPane);

        }
        currentlyPlayingMoviesAccordion.getPanes().addAll(values);
        TitledPane tiledPane = new TitledPane("currentlyPlaying", currentlyPlayingMoviesAccordion);
        tiledPane.setAnimated(true);
        tiledPane.setText(CURRENTLY_AVAILABLE);
        return tiledPane;
    }

    private TitledPane getComingSoonMoviesAsTitledPane(Map.Entry<String, ArrayList<SertiaMovie>> comingSoonMovies) {
        Accordion comingSoonAccordion = new Accordion();
        ArrayList<TitledPane> values = new ArrayList<>();
        for (SertiaMovie sertiaMovie : comingSoonMovies.getValue()) {
            ListView<Button> allScreeningsInCinemaOfSpecificMovie = new ListView<>();
            ObservableList<Button> buttonObservableList = FXCollections.observableArrayList();
            Button moreInfoBtn = new Button();
            moreInfoBtn.setText(ADD_SCREENING);
            moreInfoBtn.setOnMouseClicked(mouseEvent -> {
                MovieHolder.getInstance().setMovie(sertiaMovie, true);
                try {
                    App.setRoot("authorized/media.manager/addNewScreening");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            HBox horizontalView = new HBox();
            horizontalView.getChildren().addAll(moreInfoBtn);

            buttonObservableList.addAll(moreInfoBtn);

            allScreeningsInCinemaOfSpecificMovie.getItems().setAll(buttonObservableList);
            Accordion specificCinema = new Accordion();
            TitledPane tiledPane = new TitledPane(sertiaMovie.getMovieDetails().getName(), specificCinema);
            tiledPane.setAnimated(true);
            tiledPane.setText(sertiaMovie.getMovieDetails().getName());
            tiledPane.setContent(horizontalView);
            specificCinema.getPanes().add(tiledPane);
            values.add(tiledPane);
        }
        comingSoonAccordion.getPanes().addAll(values);
        TitledPane tiledPane = new TitledPane("comingSoonMovies", comingSoonAccordion);
        tiledPane.setAnimated(false);
        tiledPane.setText(COMING_SOON);
        tiledPane.setContent(comingSoonAccordion);
        return tiledPane;
    }

    private TitledPane getStreamableMoviesAsTitledPane(Map.Entry<String, ArrayList<SertiaMovie>> streamableMovies) {
        Accordion streamableAccordion = new Accordion();
        ArrayList<TitledPane> values = new ArrayList<>();
        for (SertiaMovie sertiaMovie : streamableMovies.getValue()) {
            ListView<Button> allScreeningsInCinemaOfSpecificMovie = new ListView<>();
            ObservableList<Button> buttonObservableList = FXCollections.observableArrayList();
            Button moreInfoBtn = new Button();
            moreInfoBtn.setText(ADD_SCREENING);
            moreInfoBtn.setOnMouseClicked(mouseEvent -> {
                MovieHolder.getInstance().setMovie(sertiaMovie, true);
                try {
                    App.setRoot("authorized/media.manager/addNewScreening");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            HBox horizontalView = new HBox();
            horizontalView.getChildren().addAll(moreInfoBtn);

            buttonObservableList.addAll(moreInfoBtn);

            allScreeningsInCinemaOfSpecificMovie.getItems().setAll(buttonObservableList);
            Accordion specificCinema = new Accordion();
            TitledPane tiledPane = new TitledPane(sertiaMovie.getMovieDetails().getName(), specificCinema);
            tiledPane.setAnimated(true);
            tiledPane.setText(sertiaMovie.getMovieDetails().getName());
            tiledPane.setContent(horizontalView);
            specificCinema.getPanes().add(tiledPane);
            values.add(tiledPane);
        }
        streamableAccordion.getPanes().addAll(values);

        TitledPane tiledPane = new TitledPane("streamableMovies", streamableAccordion);
        tiledPane.setAnimated(true);
        tiledPane.setText(HOME_ONLINE_SCREENING);
        tiledPane.setContent(streamableAccordion);
        return tiledPane;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<TitledPane> list = FXCollections.observableArrayList();
        SertiaCatalogResponse response = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
        if (!response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.ERROR, FETCH_MOVIE_ERROR, MOVIES_CATALOG_FETCH + response.failReason);
        } else {
            List<SertiaMovie> moviesList = response.movies;
            HashMap<String, ArrayList<SertiaMovie>> moviesByType = new HashMap<>();

            moviesList.forEach(sertiaMovie -> {
                if (sertiaMovie.isComingSoon) {
                    if (moviesByType.containsKey("COMING-SOON")) {
                        moviesByType.get("COMING-SOON").add(sertiaMovie);
                    } else {
                        moviesByType.put("COMING-SOON", new ArrayList<>() {{
                            add(sertiaMovie);
                        }});
                    }
                }

                if (moviesByType.containsKey("CURRENTLY-PLAYING")) {
                    moviesByType.get("CURRENTLY-PLAYING").add(sertiaMovie);
                } else {
                    moviesByType.put("CURRENTLY-PLAYING", new ArrayList<>() {{
                        add(sertiaMovie);
                    }});
                }

                // TODO: what's about movies which can be streamable and also have screenings? they will appear twice... what do you think we have to do?
                if (sertiaMovie.isStreamable) {
                    if (moviesByType.containsKey("STREAMABLE")) {
                        moviesByType.get("STREAMABLE").add(sertiaMovie);
                    } else {
                        moviesByType.put("STREAMABLE", new ArrayList<>() {{
                            add(sertiaMovie);
                        }});
                    }
                }
            });

            moviesByType.entrySet().forEach(stringArrayListEntry -> {
                if (stringArrayListEntry.getKey().equals("CURRENTLY-PLAYING")) {
                    list.add(getCurrentlyPlayingMoviesAsTitledPane(stringArrayListEntry));
                } else if (stringArrayListEntry.getKey().equals("STREAMABLE")) {
                    list.add(getStreamableMoviesAsTitledPane(stringArrayListEntry));
                } else if (stringArrayListEntry.getKey().equals("COMING-SOON")) {
                    list.add(getComingSoonMoviesAsTitledPane(stringArrayListEntry));
                }
            });
            moviesKindAndDataAccordion.getPanes().addAll(list);
        }
    }
}
