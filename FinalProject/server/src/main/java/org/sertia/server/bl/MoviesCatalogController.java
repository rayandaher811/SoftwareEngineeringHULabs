package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.server.communication.messages.CinemaScreeningMovie;
import org.sertia.server.communication.messages.MoviesCatalog;
import org.sertia.server.dl.DbSessionSupplier;
import org.sertia.server.dl.classes.Movie;
import org.sertia.server.dl.classes.Screening;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;

public class MoviesCatalogController {

    private static final Session session = DbSessionSupplier.getInstance();

    public static MoviesCatalog getAllMoviesCatalog() {
        Collection<Screening> screeningMovies = queryScreenings();
        MoviesCatalog catalog = new MoviesCatalog();
        screeningMovies.forEach(screening -> catalog.addMovie(screeningToCinemaScreeningMovie(screening)));
        return catalog;
    }

    private static Collection<Screening> queryScreenings() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Screening> query = builder.createQuery(Screening.class);
        query.from(Screening.class);
        return session.createQuery(query).getResultList();
    }

    private static CinemaScreeningMovie screeningToCinemaScreeningMovie(Screening screening){
        final Movie movie = screening.getMovie();
        return new CinemaScreeningMovie(movie.getId(),
                                        movie.getProducer().getFullName(),
                                        movie.getMainActor().getFullName(),
                                        movie.getHebrewName(),
                                        movie.getName(),
                                        movie.isComingSoon(),
                                        movie.getDescription(),
                                        movie.getImageUrl());
    }

}
