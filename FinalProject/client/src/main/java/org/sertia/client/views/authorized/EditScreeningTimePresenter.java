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
import org.sertia.client.global.ScreeningHolder;
import org.sertia.client.views.unauthorized.movies.AbstractMoviesPresenter;
import org.sertia.contracts.movies.catalog.ClientMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;

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
    private void back() throws IOException {
        App.setRoot("employeesForm");
    }

    private void updateMovie(ClientScreening screening, ClientMovie movie) {
        MovieHolder.getInstance().setMovie(movie);
        ScreeningHolder.getInstance().setScreening(screening);
        try {
            App.setRoot("editMovieScreeningTimePresenter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<ClientMovie, HashMap<String, List<ClientScreening>>> cinemaToScreenings(List<SertiaMovie> movies) {
        HashMap<ClientMovie, HashMap<String, List<ClientScreening>>> movieToCinemaAndScreenings = new HashMap<>();

        for (SertiaMovie screeningMovie : movies) {
            for (ClientScreening specificScreening : screeningMovie.getScreenings()) {
                if (movieToCinemaAndScreenings.containsKey(screeningMovie.getMovieDetails())) {
                    if (movieToCinemaAndScreenings.get(screeningMovie.getMovieDetails()).containsKey(specificScreening.getCinemaName())){
                        movieToCinemaAndScreenings.get(screeningMovie.getMovieDetails()).get(specificScreening.getCinemaName()).add(specificScreening);
                    } else {
                        movieToCinemaAndScreenings.get(screeningMovie.getMovieDetails()).put(specificScreening.getCinemaName(), new ArrayList<>(){{add(specificScreening);}});
                    }
                } else {
                    movieToCinemaAndScreenings.put(screeningMovie.getMovieDetails(), new HashMap(){{put(specificScreening.getCinemaName(), new ArrayList<>(){{add(specificScreening);}});}});
                }
            }
        }

        return movieToCinemaAndScreenings;
    }

    private TitledPane getCurrentlyPlayingMoviesAsTitledPane(List<SertiaMovie> currentlyAvailableMovies) {
        Accordion currentlyPlayingMoviesAccordion = new Accordion();

        HashMap<ClientMovie, HashMap<String, List<ClientScreening>>> branchToMovies = cinemaToScreenings(currentlyAvailableMovies);
        ArrayList<TitledPane> values = new ArrayList<>();
        for (Map.Entry<ClientMovie, HashMap<String, List<ClientScreening>>> moviesInBranch : branchToMovies.entrySet()){
            Accordion moviePlayingTimeAccordion = new Accordion();
            ArrayList<TitledPane> specificCinemaList = new ArrayList<>();
            for (Map.Entry<String, List<ClientScreening>> cinemaToScreenings : moviesInBranch.getValue().entrySet()){
                ListView<Button> allScreeningsInCinemaOfSpecificMovie = new ListView<>();
                ObservableList<Button> buttonObservableList = FXCollections.observableArrayList();
                cinemaToScreenings.getValue().stream().forEach(cinemaScreeningMovie -> {
                    Button btn = new Button();
                    btn.setText(cinemaScreeningMovie.screeningTime.toString());
                    buttonObservableList.add(btn);
                    btn.setOnMouseClicked(mouseEvent -> {
                        updateMovie(cinemaScreeningMovie, moviesInBranch.getKey());
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
            TitledPane tiledPane = new TitledPane();
            tiledPane.setAnimated(true);
            tiledPane.setText(moviesInBranch.getKey().getName());
            tiledPane.setContent(moviePlayingTimeAccordion);
            values.add(tiledPane);

        }
        currentlyPlayingMoviesAccordion.getPanes().addAll(values);
        TitledPane tiledPane = new TitledPane("currentlyPlaying", currentlyPlayingMoviesAccordion);
        tiledPane.setAnimated(true);
        tiledPane.setText(CURRENTLY_AVAILABLE);
        return tiledPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<TitledPane> list = FXCollections.observableArrayList();
        List<SertiaMovie> availableMovies = ClientCatalogControl.getInstance().getAvailableMovies();
        list.add(getCurrentlyPlayingMoviesAsTitledPane(availableMovies));
        moviesKindAndDataAccordion.getPanes().addAll(list);
    }
}
