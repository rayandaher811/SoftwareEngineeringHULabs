package org.sertia.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import org.sertia.client.App;
import org.sertia.client.communication.ServerCommunicationHandler;
import org.sertia.client.communication.messages.CinemaScreeningMovie;
import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.client.global.LoggedInUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AvailableMoviesForEdit implements Initializable {

    @FXML
    private Accordion moviesAccordion;

    private TitledPane screeningMovieToTilePane(CinemaScreeningMovie screeningMovie) {
        TitledPane tiledPane = new TitledPane("BGBG", new Label("show all BGGBG"));
        tiledPane.setAnimated(false);
        tiledPane.setText(screeningMovie.getName());
        Button btn = new Button();
        btn.setText(String.valueOf(screeningMovie.getDescription()));
        btn.setOnMouseClicked(mouseEvent -> {
            try {
                LoggedInUser.getInstance().setChosenMovieForUpdateTimeOperation(screeningMovie);
                App.setRoot("editMovieScreeningTimePresenter");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tiledPane.setContent(btn);

        return tiledPane;
    }

    @FXML
    private void back() throws IOException {
        LoggedInUser.onDisconnection();
        App.setRoot("employeesForm");
    }

    @FXML
    private void updateMovie() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<TitledPane> list = FXCollections.observableArrayList();
        // Need to get movies by start time
        MoviesCatalog catalog = ServerCommunicationHandler.getInstance().getMoviesCatalog();
        catalog.getMoviesCatalog().forEach(screeningMovie -> list.add(screeningMovieToTilePane(screeningMovie)));
//
//        list.addAll(movies);
//
//        lstView.setItems(list);

        moviesAccordion.getPanes().addAll(list);

    }
}
