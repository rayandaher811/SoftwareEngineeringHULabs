package org.sertia.server.controllers;

import DataLayer.Data.Screening;
import DataLayer.SessionSupplier;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.sertia.server.pojos.Movie;
import org.sertia.server.pojos.ScreeningMovie;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;

/**
 * This class should query DB
 * now it just acting as a mock
 */
public class MoviesController {

    private static Session session = SessionSupplier.getInstance();

    public static Collection<ScreeningMovie> getScreeningMovies() {
        // TODO: this function should request server for changes, now it's just a mock, so it has hard coded values
        return new HashSet<>(queryScreeningMoviesFromServer());
    }

    private static Collection<ScreeningMovie> queryScreeningMoviesFromServer() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Screening> query = builder.createQuery(Screening.class);
        query.from(Screening.class);
        List<Screening> data = session.createQuery(query).getResultList();

        LinkedList<ScreeningMovie> screeningMovies = new LinkedList<ScreeningMovie>();

        for (Screening screening: data) {
            screeningMovies.add(new ScreeningMovie(new Movie(screening.getMovie().getName(),
                                                            screening.getMovie().getHebrewName(),
                                                            screening.getMovie().getProducer().getFullName(),
                                                            screening.getMovie().getMainActor().getFullName(),
                                                            screening.getMovie().isComingSoon(),
                                                            screening.getMovie().getDescription()),
                                                    screening.getScreeningTime(),
                                                    screening.getPrice()));
        }
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
