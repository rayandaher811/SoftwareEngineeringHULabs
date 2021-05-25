package org.sertia.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sertia.client.ActiveUserData;
import org.sertia.client.App;
import org.sertia.client.communication.ServerCommunicationHandler;
import org.sertia.client.communication.messages.CinemaScreeningMovie;
import org.sertia.client.communication.messages.MoviesCatalog;
import org.sertia.client.dialogs.UpdateMoviesTimeDialog;
import org.sertia.client.global.LoggedInUser;
import org.sertia.client.pojos.ScreeningMovie;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

public class AvailableMoviesForEdit implements Initializable {

//    @FXML
//    private ListView<ScreeningMovie> lstView;
    @FXML
    private Accordion moviesAccordion;

    private TitledPane screeningMovieToTilePane(CinemaScreeningMovie screeningMovie) {
        TitledPane tiledPane = new TitledPane("BGBG", new Label("show all BGGBG"));
        tiledPane.setAnimated(false);
        tiledPane.setText(screeningMovie.getName());
        Button btn = new Button();
        btn.setText(String.valueOf(screeningMovie.getDescription()));
        btn.setOnMouseClicked(mouseEvent -> {
            ActiveUserData userData = UpdateMoviesTimeDialog.loginAndGetUserData(screeningMovie);
            if (userData.getUserName().equals("bbbb")){
                try {
                    App.setRoot("employeesForm");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
