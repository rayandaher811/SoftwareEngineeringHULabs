package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.contracts.movies.catalog.controller.ClientMovie;
import org.sertia.contracts.movies.catalog.controller.ClientScreening;
import org.sertia.contracts.movies.catalog.controller.SertiaMovie;
import org.sertia.contracts.reports.controller.ClientReport;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.communication.messages.CinemaScreeningMovie;
import org.sertia.server.communication.messages.MoviesCatalog;
import org.sertia.server.communication.messages.UpdateMovieScreeningTime;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.Movie;
import org.sertia.server.dl.classes.Screening;
import org.sertia.server.dl.classes.TicketType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MoviesCatalogController implements Reportable {

    public static MoviesCatalog getAllMoviesCatalog() {
        Collection<Screening> screeningMovies = queryScreenings();
        MoviesCatalog catalog = new MoviesCatalog();
        screeningMovies.forEach(screening -> catalog.addMovie(screeningToCinemaScreeningMovie(screening)));
        return catalog;
    }

    public static void updateScreeningMovie(UpdateMovieScreeningTime updateMovieRequest) {
        Session session = HibernateSessionFactory.getInstance().openSession();

        try {
            // Getting the real screening to avoid redundant changes
            Screening screeningToUpdate = session.get(Screening.class,
                    updateMovieRequest.getCurrentMovie().getScreeningId());

            // Updating
            screeningToUpdate.setScreeningTimeStampAsString(updateMovieRequest.getNewDateTimeAsString());
            session.beginTransaction();
            session.update(screeningToUpdate);
            session.getTransaction().commit();
        } catch (Exception e) {
        } finally {
            session.close();
        }
    }

//    public MoviesCatalog getAllMoviesCatalog() {
//        // TODO - implement MoviesCatalogController.getAllMoviesCatalog
//        throw new UnsupportedOperationException();
//    }

    /**
     *
     * @param cinemaId
     */
    public MoviesCatalog getScreeningsByCinemaId(String cinemaId) {
        // TODO - implement MoviesCatalogController.getScreeningsByCinemaId
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param movieData
     */
    public void addMovie(SertiaMovie movieData) {
        // TODO - implement MoviesCatalogController.addMovie
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param movieId
     */
    public void removeMovie(String movieId) {
        // TODO - implement MoviesCatalogController.removeMovie
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param screenings
     */
    public void updateMovieScreenings(ClientScreening screenings) {
        // TODO - implement MoviesCatalogController.updateMovieScreenings
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param newScreening
     */
    public void addMovieScreening(ClientScreening newScreening) {
        // TODO - implement MoviesCatalogController.addMovieScreening
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param screeningId
     */
    public void removeMovieScreening(String screeningId) {
        // TODO - implement MoviesCatalogController.removeMovieScreening
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param movieId
     * @param pricePerStream
     */
    public void addStreaming(String movieId, int pricePerStream) {
        // TODO - implement MoviesCatalogController.addStreaming
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param movieId
     */
    public void removeStreaming(String movieId) {
        // TODO - implement MoviesCatalogController.removeStreaming
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param movieId
     * @param ticketType
     * @param newPrice
     */
    public void requestPriceChange(String movieId, TicketType ticketType, int newPrice) {
        // TODO - implement MoviesCatalogController.requestPriceChange
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param movie
     */
    private boolean isMovieValid(SertiaMovie movie) {
        // TODO - implement MoviesCatalogController.isMovieValid
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param screening
     */
    private boolean isScreeningValid(ClientScreening screening) {
        // TODO - implement MoviesCatalogController.isScreeningValid
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param newMovie
     */
    private void notifyVoucherOwners(ClientMovie newMovie) {
        // TODO - implement MoviesCatalogController.notifyVoucherOwners
        throw new UnsupportedOperationException();
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
        final Movie movie = screening.getScreenableMovie().getMovie();
        return new CinemaScreeningMovie(screening.getId(),
                movie.getProducer().getFullName(),
                movie.getMainActor().getFullName(),
                movie.getHebrewName(),
                movie.getName(),
                movie.isComingSoon(),
                movie.getDescription(),
                movie.getImageUrl(),
                screening.getScreeningTimeStampAsString(),
                screening.getHall().getCinema().getName(),
                screening.getHall().getId());
    }

    @Override
    public ClientReport[] createSertiaReports() {
        return new ClientReport[0];
    }

    @Override
    public ClientReport[] createCinemaReports(String cinemaId) {
        return new ClientReport[0];
    }
}
