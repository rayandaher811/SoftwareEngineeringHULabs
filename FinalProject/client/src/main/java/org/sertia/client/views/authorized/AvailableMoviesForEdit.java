package org.sertia.client.views.authorized;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.LoggedInUser;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.views.unauthorized.movies.AbstractMoviesPresenter;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.sertia.client.Constants.*;

public class AvailableMoviesForEdit extends AbstractMoviesPresenter implements Initializable {
    @FXML
    private Accordion moviesKindAndDataAccordion;

    public AvailableMoviesForEdit() {
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

    private HashMap<ClientMovie, HashMap<String, List<ClientScreening>>> cinemaToScreenings(ArrayList<SertiaMovie> movies) {
        HashMap<ClientMovie, HashMap<String, List<ClientScreening>>> movieToCinemaAndScreenings = new HashMap<>();

        for (SertiaMovie screeningMovie : movies) {
            for (ClientScreening specificScreening : screeningMovie.getScreenings()) {
                if (movieToCinemaAndScreenings.containsKey(screeningMovie.getMovieDetails())) {
                    if (movieToCinemaAndScreenings.get(screeningMovie.getMovieDetails()).containsKey(specificScreening.getCinemaName())) {
                        movieToCinemaAndScreenings.get(screeningMovie.getMovieDetails()).get(specificScreening.getCinemaName()).add(specificScreening);
                    } else {
                        movieToCinemaAndScreenings.get(screeningMovie.getMovieDetails()).put(specificScreening.getCinemaName(), new ArrayList<>() {{
                            add(specificScreening);
                        }});
                    }
                } else {
                    movieToCinemaAndScreenings.put(screeningMovie.getMovieDetails(), new HashMap() {{
                        put(specificScreening.getCinemaName(), new ArrayList<>() {{
                            add(specificScreening);
                        }});
                    }});
                }
            }
        }

        return movieToCinemaAndScreenings;
    }

    private TitledPane getCurrentlyPlayingMoviesAsTitledPane(Map.Entry<String, ArrayList<SertiaMovie>> currentlyAvailableMovies) {
        Accordion currentlyPlayingMoviesAccordion = new Accordion();

        HashMap<ClientMovie, HashMap<String, List<ClientScreening>>> branchToMovies = cinemaToScreenings(currentlyAvailableMovies.getValue());
        ArrayList<TitledPane> values = new ArrayList<>();
        for (Map.Entry<ClientMovie, HashMap<String, List<ClientScreening>>> moviesInBranch : branchToMovies.entrySet()) {
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
                        System.out.println("currently available movie");
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
            bgBtn.setText(MOVIE_DETAILS);
            bgBtn.setOnMouseClicked(mouseEvent -> {
                try {
                    MovieHolder.getInstance().setMovie(moviesInBranch.getKey());
                    App.setRoot("movieDetails");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            hBox.getChildren().addAll(moviePlayingTimeAccordion, bgBtn);
            TitledPane tiledPane = new TitledPane();
            tiledPane.setAnimated(true);
            tiledPane.setText(moviesInBranch.getKey().getName());
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
            TitledPane p = new TitledPane();
            p.setText(sertiaMovie.getMovieDetails().getName());
            p.setOnMouseClicked(mouseEvent -> {
                try {
                    MovieHolder.getInstance().setMovie(sertiaMovie.getMovieDetails());
                    App.setRoot("movieDetails");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            values.add(p);
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
            TitledPane p = new TitledPane();
            p.setText(sertiaMovie.getMovieDetails().getName());
            p.setOnMouseClicked(mouseEvent -> {
                try {
                    MovieHolder.getInstance().setMovie(sertiaMovie.getMovieDetails());
                    App.setRoot("movieDetails");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            values.add(p);
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
        List<SertiaMovie> moviesList = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
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
            } else {
                if (moviesByType.containsKey("CURRENTLY-PLAYING")) {
                    moviesByType.get("CURRENTLY-PLAYING").add(sertiaMovie);
                } else {
                    moviesByType.put("CURRENTLY-PLAYING", new ArrayList<>() {{
                        add(sertiaMovie);
                    }});
                }
            }
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
        System.out.println("BVG");

    }
}
