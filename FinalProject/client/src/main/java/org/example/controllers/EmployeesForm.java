package org.example.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import org.example.App;
import org.example.dialogs.UpdateMoviesTimeDialog;
import org.example.global.LoggedInUser;
import org.example.pojos.Movie;
import org.example.pojos.ScreeningMovie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class EmployeesForm {
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void updateMovie() throws IOException {
        UpdateMoviesTimeDialog.loginAndGetUserData(getScreeningMovies());
    }

    private Collection<ScreeningMovie> getScreeningMovies() {
        // TODO: this function should request server for changes, now it's just a mock, so it has hard coded values
        return new HashSet<>(queryScreeningMoviesFromServer());
    }

    private Collection<ScreeningMovie> queryScreeningMoviesFromServer() {
        ScreeningMovie one = new ScreeningMovie(new Movie("a", "b"), 3, 2);
        ScreeningMovie two = new ScreeningMovie(new Movie("ab", "b"), 3, 2);
        ScreeningMovie three = new ScreeningMovie(new Movie("abc", "b"), 3, 2);
        ScreeningMovie four = new ScreeningMovie(new Movie("a", "b"), 3, 2);
        ScreeningMovie five = new ScreeningMovie(new Movie("abw", "b"), 3, 2);

        Collection<ScreeningMovie> screenningMovies = new ArrayList<>();
        Collections.addAll(screenningMovies, one, two, three, four, five);
        return screenningMovies;
    }
}