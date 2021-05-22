package org.sertia.server.controllers;

import org.sertia.server.pojos.Movie;
import org.sertia.server.pojos.ScreeningMovie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * This class should query DB
 * now it just acting as a mock
 */
public class MoviesController {

    public static Collection<ScreeningMovie> getScreeningMovies() {
        // TODO: this function should request server for changes, now it's just a mock, so it has hard coded values
        return new HashSet<>(queryScreeningMoviesFromServer());
    }

    private static Collection<ScreeningMovie> queryScreeningMoviesFromServer() {
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
