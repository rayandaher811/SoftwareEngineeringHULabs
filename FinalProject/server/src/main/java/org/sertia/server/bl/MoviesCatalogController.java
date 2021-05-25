package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.server.communication.messages.CinemaScreeningMovie;
import org.sertia.server.communication.messages.MoviesCatalog;
import org.sertia.server.communication.messages.UpdateMovieScreeningTime;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.Movie;
import org.sertia.server.dl.classes.Screening;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MoviesCatalogController {

    public static MoviesCatalog getAllMoviesCatalog() {
        Collection<Screening> screeningMovies = queryScreenings();
        MoviesCatalog catalog = new MoviesCatalog();
        screeningMovies.forEach(screening -> catalog.addMovie(screeningToCinemaScreeningMovie(screening)));
        return catalog;
    }

    public static void updateScreeningMovie(UpdateMovieScreeningTime updateMovieRequest) {
        Session session = HibernateSessionFactory.getInstance().openSession();

        // TODO validations
        CinemaScreeningMovie movieFromClient = updateMovieRequest.getCurrentMovie();
        movieFromClient.setScreeningTime(updateMovieRequest.getNewDateTime());

        Collection<Screening> screenings = queryScreenings();
        AtomicReference<Screening> expected = new AtomicReference<>();

        screenings.forEach(screening -> {
            if (screening.getId() == movieFromClient.getScreeningId()){
                screening.setScreeningTime(updateMovieRequest.getNewDateTime());
                expected.set(screening);
                return;
            }
        });

        try {
            session.beginTransaction();
            session.update(expected.get());
            session.getTransaction().commit();
        } catch (Exception e) {
        } finally {
            session.close();
        }
    }

    private static Collection<Screening> queryScreenings() {
        Session session = HibernateSessionFactory.getInstance().openSession();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Screening> query = builder.createQuery(Screening.class);
            query.from(Screening.class);
            List<Screening> screeningList = session.createQuery(query).getResultList();
            return screeningList;
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    private static CinemaScreeningMovie screeningToCinemaScreeningMovie(Screening screening) {
        final Movie movie = screening.getMovie();
        return new CinemaScreeningMovie(screening.getId(),
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
