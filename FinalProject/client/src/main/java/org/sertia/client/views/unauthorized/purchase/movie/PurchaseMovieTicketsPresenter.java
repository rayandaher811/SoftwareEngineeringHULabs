package org.sertia.client.views.unauthorized.purchase.movie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PurchaseMovieTicketsPresenter implements Initializable {
    @FXML
    private TextField amountOfTicketsTxt;
    @FXML
    private Accordion moviesAccordion;

    private HashMap<String, List<ClientScreening>> cinemaToScreenings(Map.Entry<CinemaScreeningMovie, List<ClientScreening>> movieToScreenings) {
        HashMap<String, List<ClientScreening>> cinemaToScreenings = new HashMap<>();

        for (ClientScreening screening : movieToScreenings.getValue()) {
            if (cinemaToScreenings.containsKey(screening.getCinemaName())) {
                cinemaToScreenings.get(screening.getCinemaName()).add(screening);
            } else {
                cinemaToScreenings.put(screening.getCinemaName(), new ArrayList<>() {{
                    add(screening);
                }});
            }
        }

        return cinemaToScreenings;
    }

    private TitledPane screeningMovieToTilePane(Map.Entry<CinemaScreeningMovie, List<ClientScreening>> movieToScreenings) {
        Accordion moviesAccordion = new Accordion();

        HashMap<String, List<ClientScreening>> cinemaToScreenings = cinemaToScreenings(movieToScreenings);

        ArrayList<TitledPane> values = new ArrayList<>();
        cinemaToScreenings.entrySet().forEach(movieScreenings -> {
            ListView<Button> allScreeningsInCinemaOfSpecificMovie = new ListView<>();
            ObservableList<Button> buttonObservableList = FXCollections.observableArrayList();
            movieScreenings.getValue().stream().forEach(cinemaScreeningMovie -> {
                Button btn = new Button();
                btn.setText(cinemaScreeningMovie.screeningTime.toString());
                buttonObservableList.add(btn);
                btn.setOnMouseClicked(mouseEvent -> {
                    try {
                        int numberOfTicketsPurchased = Integer.parseInt(this.amountOfTicketsTxt.getText());
                        // TODO: fixme, it should be seat map or something like that
                        ScreeningHolder.getInstance().setScreening(cinemaScreeningMovie);
                        MovieHolder.getInstance().setMovie(movieToScreenings.getKey(), false);
                        boolean isCovidLimitationsEnbaled = false;
                        System.out.println("number of tickets is: " + numberOfTicketsPurchased);
//                        if (ClientCovidRegulationsControl.getInstance().areRegulationsActive()){
                        if (isCovidLimitationsEnbaled) {
                            System.out.println("Need to go to covid view");
                        } else {
                            System.out.println("Need to show available spots");
                            App.setRoot("unauthorized/seatMapView");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            });
            allScreeningsInCinemaOfSpecificMovie.getItems().setAll(buttonObservableList);
            values.add(new TitledPane(movieScreenings.getKey(), allScreeningsInCinemaOfSpecificMovie));
        });

        moviesAccordion.getPanes().setAll(values);

        TitledPane tiledPane = new TitledPane("AllMoviesByCinema", moviesAccordion);
        tiledPane.setAnimated(false);
        tiledPane.setText(movieToScreenings.getKey().getMovieDetails().getName());
        tiledPane.setContent(moviesAccordion);
        return tiledPane;
    }

    @FXML
    public void toMain() throws IOException {
        App.setRoot("unauthorized/primary");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<TitledPane> list = FXCollections.observableArrayList();
        SertiaCatalogResponse response = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
        if (!response.isSuccessful) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Fetch movies catalog");
            errorAlert.setContentText("failed fetch catalog, error msg: " + response.failReason);
            errorAlert.showAndWait();
        } else {
            List<SertiaMovie> screeningMovieArrayList = response.movies;
            HashMap<CinemaScreeningMovie, List<ClientScreening>> movieToScreenings = new HashMap<>();

            for (int i = 0; i < screeningMovieArrayList.size(); i++) {
                final SertiaMovie screeningMovie = screeningMovieArrayList.get(i);

                if (movieToScreenings.containsKey(screeningMovie)) {
                    movieToScreenings.get(screeningMovie).addAll(screeningMovie.getScreenings());
                } else {
                    movieToScreenings.put(screeningMovie, screeningMovie.getScreenings());
                }
            }

            movieToScreenings.entrySet().forEach(specificMovieToScreenings -> list.add(screeningMovieToTilePane(specificMovieToScreenings)));

            moviesAccordion.getPanes().addAll(list);
        }
    }
}