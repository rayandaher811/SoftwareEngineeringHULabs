package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.server.communication.messages.CinemaScreeningMovie;
import org.sertia.server.communication.messages.MoviesCatalog;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.Movie;
import org.sertia.server.dl.classes.Screening;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MoviesCatalogController {

    public static MoviesCatalog getAllMoviesCatalog() {
        Collection<Screening> screeningMovies = queryScreenings();
        MoviesCatalog catalog = new MoviesCatalog();
        screeningMovies.forEach(screening -> catalog.addMovie(screeningToCinemaScreeningMovie(screening)));
        return catalog;
    }

    private static Collection<Screening> queryScreenings() {
        try {
            Session session = HibernateSessionFactory.getInstance().openSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Screening> query = builder.createQuery(Screening.class);
            query.from(Screening.class);
            List<Screening> screeningList = session.createQuery(query).getResultList();
            session.close();
            return screeningList;
        } catch (Exception e) {
            return Collections.emptyList();
        }

    }

    private static CinemaScreeningMovie screeningToCinemaScreeningMovie(Screening screening) {
        final Movie movie = screening.getMovie();
        return new CinemaScreeningMovie(movie.getId(),
                movie.getProducer().getFullName(),
                movie.getMainActor().getFullName(),
                movie.getHebrewName(),
                movie.getName(),
                movie.isComingSoon(),
                movie.getDescription(),
                movie.getImageUrl(),
                screening.getScreeningTime());
    }

}
