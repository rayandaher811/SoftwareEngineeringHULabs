package org.sertia.client.views.unauthorized.movies;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCatalogControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.client.global.SpecificViewHolder;
import org.sertia.client.views.Utils;
import org.sertia.contracts.movies.catalog.CinemaScreeningMovie;
import org.sertia.contracts.movies.catalog.ClientScreening;
import org.sertia.contracts.movies.catalog.SertiaMovie;
import org.sertia.contracts.movies.catalog.response.CinemaAndHallsResponse;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static org.sertia.client.Constants.*;

public class CatalogView implements Initializable {

    public ComboBox<String> filterByBranch;
    public DatePicker fromDatePicker;
    public DatePicker toDatePicker;
    public ListView filteredMoviesLst;
    @FXML
    private Accordion moviesKindAndDataAccordion;
    private ObservableList<SertiaMovie> filteredMovies;
    private List<SertiaMovie> queriedMoviesSinceLastLaunch;

    private HashMap<SertiaMovie, HashMap<String, List<ClientScreening>>> cinemaToScreenings(ArrayList<SertiaMovie> movies) {
        HashMap<SertiaMovie, HashMap<String, List<ClientScreening>>> movieToCinemaAndScreenings = new HashMap<>();

        for (SertiaMovie screeningMovie : movies) {
            for (ClientScreening specificScreening : screeningMovie.getScreenings()) {
                if (movieToCinemaAndScreenings.containsKey(screeningMovie)) {
                    if (movieToCinemaAndScreenings.get(screeningMovie).containsKey(specificScreening.getCinemaName())) {
                        movieToCinemaAndScreenings.get(screeningMovie).get(specificScreening.getCinemaName()).add(specificScreening);
                    } else {
                        movieToCinemaAndScreenings.get(screeningMovie).put(specificScreening.getCinemaName(), new ArrayList<>() {{
                            add(specificScreening);
                        }});
                    }
                } else {
                    movieToCinemaAndScreenings.put(screeningMovie, new HashMap() {{
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

        HashMap<SertiaMovie, HashMap<String, List<ClientScreening>>> branchToMovies = cinemaToScreenings(currentlyAvailableMovies.getValue());
        ArrayList<TitledPane> values = new ArrayList<>();
        for (Map.Entry<SertiaMovie, HashMap<String, List<ClientScreening>>> moviesInBranch : branchToMovies.entrySet()) {
            Accordion moviePlayingTimeAccordion = new Accordion();
            ArrayList<TitledPane> specificCinemaList = new ArrayList<>();
            for (Map.Entry<String, List<ClientScreening>> cinemaToScreenings : moviesInBranch.getValue().entrySet()) {
                if (SpecificViewHolder.instance.isInitialized() && cinemaToScreenings.getKey().equals(SpecificViewHolder.getInstance().getBranchName()) || (!SpecificViewHolder.instance.isInitialized())) {
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
                                App.setRoot("unauthorized/movie/screeningOrderDataSelection");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                    });
                    allScreeningsInCinemaOfSpecificMovie.getItems().setAll(buttonObservableList);
                    Accordion specificCinema = new Accordion();
                    TitledPane tiledPane = new TitledPane(cinemaToScreenings.getKey(), specificCinema);
                    tiledPane.setAnimated(true);
                    if (!SpecificViewHolder.instance.isInitialized()){
                        tiledPane.setText(cinemaToScreenings.getKey());
                    }
                    tiledPane.setContent(allScreeningsInCinemaOfSpecificMovie);
                    specificCinemaList.add(tiledPane);
                }
            }
            if (SpecificViewHolder.getInstance().isInitialized() && moviesInBranch.getValue().keySet().contains(SpecificViewHolder.getInstance().getBranchName()) || !SpecificViewHolder.instance.isInitialized()) {
                moviePlayingTimeAccordion.getPanes().addAll(specificCinemaList);
                HBox hBox = new HBox();
                Button bgBtn = new Button();
                bgBtn.setText(MOVIE_DETAILS);
                bgBtn.setOnMouseClicked(mouseEvent -> {
                    try {
                        MovieHolder.getInstance().setMovie(moviesInBranch.getKey(), false);
                        App.setRoot("unauthorized/movie/movieDetails");
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
            moreInfoBtn.setText(MOVIE_DETAILS);
            moreInfoBtn.setOnMouseClicked(mouseEvent -> {
                MovieHolder.getInstance().setMovie(sertiaMovie, true);
                try {
                    App.setRoot("unauthorized/movie/movieDetails");
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
            moreInfoBtn.setText(MOVIE_DETAILS);
            moreInfoBtn.setOnMouseClicked(mouseEvent -> {
                MovieHolder.getInstance().setMovie(sertiaMovie, true);
                try {
                    App.setRoot("unauthorized/movie/movieDetails");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Button buyLinkBtn = new Button();
            buyLinkBtn.setText(BUY_STREAMING_LINK);
            buyLinkBtn.setOnMouseClicked(mouseEvent -> {
                try {
                    MovieHolder.getInstance().setMovie(sertiaMovie, true);
                    App.setRoot("unauthorized/onlineLink/onlineWatchLinkForm");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            HBox horizontalView = new HBox();
            horizontalView.getChildren().addAll(moreInfoBtn, buyLinkBtn);

            buttonObservableList.addAll(moreInfoBtn, buyLinkBtn);

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
        if (SpecificViewHolder.getInstance().isInitialized()){
            filterByBranch.setValue(SpecificViewHolder.getInstance().getBranchName());
            filterByBranch.setVisible(false);
        }
        ObservableList<TitledPane> list = FXCollections.observableArrayList();
        SertiaCatalogResponse response = ClientCatalogControl.getInstance().requestAllMoviesCatalog();
        if (!response.isSuccessful) {
            Utils.popAlert(Alert.AlertType.ERROR, "Fetch movies catalog", "failed fetch catalog, error msg: " + response.failReason);
        } else {
            queriedMoviesSinceLastLaunch = response.movies;
            List<SertiaMovie> moviesList = List.copyOf(queriedMoviesSinceLastLaunch);
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
            fromDatePicker.valueProperty().addListener(this::fromDateValueChanged);
            toDatePicker.valueProperty().addListener(this::toDateValueChanged);
            filterByBranch.valueProperty().addListener(this::onBranchChanged);

            CinemaAndHallsResponse cinemaResponse = ClientCatalogControl.getInstance().getCinemasAndHalls();
            if (!cinemaResponse.isSuccessful) {
                Utils.popAlert(Alert.AlertType.ERROR, "Catalog view", "couldnt fetch cinemas list");
            } else {
                filterByBranch.getItems().addAll(cinemaResponse.cinemaToHalls.keySet());
            }

            filteredMovies = filteredMoviesLst.getItems();
        }
    }

    private void renderMoviesList() {
        filteredMoviesLst.getItems().clear();
        boolean isBranchSelected = filterByBranch.getSelectionModel().getSelectedItem() != null;
        boolean isFromDateSelected = fromDatePicker.getValue() != null;
        boolean isToDateSelected = toDatePicker.getValue() != null;

        if (isBranchSelected) {
            if (isFromDateSelected) {
                if (isToDateSelected) {
                    filterByBranchStartDateAndEndDate();
                } else {
                    filterByBranchAndStartDate();
                }
            } else {
                if (isToDateSelected) {
                    filterByBranchAndEndDate();
                } else {
                    filterByBranchOnly(); //
                }
            }
        } else {
            if (isFromDateSelected) {
                if (isToDateSelected) {
                    filterByStartAndEndDate(); //
                } else {
                    filterByStartDate(); //
                }
            } else {
                if (isToDateSelected) {
                    filterByEndDate(); //
                } else {
                    Utils.popAlert(Alert.AlertType.ERROR, "Search movies error", "must specify search parameters");
                }
            }
        }
    }

    private void filterByBranchOnly() {
        int count = 0;
        for (SertiaMovie moviesSinceLastLaunch : queriedMoviesSinceLastLaunch) {
            for (ClientScreening screening : moviesSinceLastLaunch.getScreenings()) {
                if (screening.getCinemaName().equals(filterByBranch.getSelectionModel().getSelectedItem())) {
                    count++;
                    filteredMoviesLst.getItems().add(clientScreeningToTitledPane(moviesSinceLastLaunch, screening, count));
                }
            }
        }
    }

    private void filterByStartAndEndDate() {
        LocalDate startDate = fromDatePicker.getValue();
        LocalDate endDate = toDatePicker.getValue();

        int count = 0;
        for (SertiaMovie moviesSinceLastLaunch : queriedMoviesSinceLastLaunch) {
            for (ClientScreening screening : moviesSinceLastLaunch.getScreenings()) {
                if (startDate.isBefore(screening.getScreeningTime().toLocalDate()) &&
                        endDate.isAfter(screening.getScreeningTime().toLocalDate())) {
                    count++;
                    filteredMoviesLst.getItems().add(clientScreeningToTitledPane(moviesSinceLastLaunch, screening, count));
                }
            }
        }
    }

    private void filterByBranchAndEndDate() {
        LocalDate endDate = toDatePicker.getValue();

        int count = 0;
        for (SertiaMovie moviesSinceLastLaunch : queriedMoviesSinceLastLaunch) {
            for (ClientScreening screening : moviesSinceLastLaunch.getScreenings()) {
                if (endDate.isAfter(screening.getScreeningTime().toLocalDate()) &&
                        screening.getCinemaName().equals(filterByBranch.getSelectionModel().getSelectedItem())) {
                    count++;
                    filteredMoviesLst.getItems().add(clientScreeningToTitledPane(moviesSinceLastLaunch, screening, count));
                }
            }
        }
    }

    private void filterByEndDate() {
        LocalDate endDate = toDatePicker.getValue();

        int count = 0;
        for (SertiaMovie moviesSinceLastLaunch : queriedMoviesSinceLastLaunch) {
            for (ClientScreening screening : moviesSinceLastLaunch.getScreenings()) {
                if (endDate.isAfter(screening.getScreeningTime().toLocalDate())) {
                    count++;
                    filteredMoviesLst.getItems().add(clientScreeningToTitledPane(moviesSinceLastLaunch, screening, count));
                }
            }
        }
    }

    private void filterByStartDate() {
        LocalDate startDate = fromDatePicker.getValue();

        int count = 0;
        for (SertiaMovie moviesSinceLastLaunch : queriedMoviesSinceLastLaunch) {
            for (ClientScreening screening : moviesSinceLastLaunch.getScreenings()) {
                if (startDate.isBefore(screening.getScreeningTime().toLocalDate())) {
                    count++;
                    filteredMoviesLst.getItems().add(clientScreeningToTitledPane(moviesSinceLastLaunch, screening, count));
                }
            }
        }
    }

    private void filterByBranchStartDateAndEndDate() {
        LocalDate startDate = fromDatePicker.getValue();
        LocalDate endDate = toDatePicker.getValue();

        int count = 0;
        for (SertiaMovie moviesSinceLastLaunch : queriedMoviesSinceLastLaunch) {
            for (ClientScreening screening : moviesSinceLastLaunch.getScreenings()) {
                if (startDate.isBefore(screening.getScreeningTime().toLocalDate()) &&
                        endDate.isAfter(screening.getScreeningTime().toLocalDate()) &&
                        filterByBranch.getSelectionModel().getSelectedItem().equals(screening.getCinemaName())) {
                    count++;
                    filteredMoviesLst.getItems().add(clientScreeningToTitledPane(moviesSinceLastLaunch, screening, count));
                }
            }
        }
    }

    private void filterByBranchAndStartDate() {
        LocalDate startDate = fromDatePicker.getValue();

        int count = 0;
        for (SertiaMovie moviesSinceLastLaunch : queriedMoviesSinceLastLaunch) {
            for (ClientScreening screening : moviesSinceLastLaunch.getScreenings()) {
                if (startDate.isBefore(screening.getScreeningTime().toLocalDate()) &&
                        filterByBranch.getSelectionModel().getSelectedItem().equals(screening.getCinemaName())) {
                    count++;
                    filteredMoviesLst.getItems().add(clientScreeningToTitledPane(moviesSinceLastLaunch, screening, count));
                }
            }
        }
    }

    private HBox toHbox(String key, String value) {
        HBox values = new HBox();
        Label keyLabel = new Label();
        keyLabel.setText(key);
        Label valueLabel = new Label();
        valueLabel.setText(value);
        values.getChildren().addAll(valueLabel, keyLabel);
        return values;
    }

    private TitledPane clientScreeningToTitledPane(SertiaMovie sertiaMovie, ClientScreening clientScreening,
                                                   int count) {
        TitledPane titledPane = new TitledPane();
        VBox vBox = new VBox();

        HBox nameHbox = toHbox(MOVIE_NAME, sertiaMovie.getMovieDetails().getName());
        HBox hebrewNameHbox = toHbox(HEBREW_MOVIE_NAME, sertiaMovie.getMovieDetails().getHebrewName());
        HBox screeningTimeHbox = toHbox(SCREENING_TIME, clientScreening.screeningTime.toString());
        HBox branchNameHbox = toHbox(BRANCH_NAME, clientScreening.getCinemaName());
        vBox.getChildren().addAll(nameHbox, hebrewNameHbox, screeningTimeHbox, branchNameHbox);
        titledPane.setContent(vBox);
        titledPane.setText(RESULT_NUMBER + count);
        return titledPane;
    }

    private void onBranchChanged(Observable observable) {
        renderMoviesList();
    }

    private void fromDateValueChanged(Observable observable) {
        renderMoviesList();
    }

    private void toDateValueChanged(Observable observable) {
        renderMoviesList();
    }

    @FXML
    private void backToClientsView() throws IOException {
        App.setRoot("unauthorized/primary");
    }
}