package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.contracts.movies.catalog.controller.*;
import org.sertia.contracts.reports.controller.ClientReport;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.communication.messages.MoviesCatalog;
import org.sertia.server.communication.messages.UpdateMovieScreeningTime;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;
import java.util.stream.Collectors;

public class MoviesCatalogController implements Reportable {

    public SertiaCatalog getSertiaCatalog() {
        Map<ScreenableMovie, List<Screening>> screeningMovies = getScreenings();
        Map<Integer, Streaming> streamings = getStreamings();
        List<SertiaMovie> sertiaMovieList = new ArrayList<>();

        screeningMovies.keySet()
                .forEach(screenableMovie -> {
                    Movie movie = screenableMovie.getMovie();
                    SertiaMovie sertiaMovie = new SertiaMovie();
                    sertiaMovie.movieDetails = movieToClientMovie(movie);
                    sertiaMovie.isComingSoon = movie.isComingSoon();
                    sertiaMovie.screenings = screeningMovies.get(screenableMovie).stream().map(MoviesCatalogController::screeningToClientScreening)
                            .collect(Collectors.toList());
                    Optional.ofNullable(streamings.get(movie.getId()))
                            .ifPresent(streaming -> {
                                sertiaMovie.isStreamable = true;
                                sertiaMovie.pricePerStream = streaming.pricePerStream;
                            });
                    sertiaMovieList.add(sertiaMovie);
                });

        return new SertiaCatalog(sertiaMovieList);
    }

    public CinemaCatalog getCatalogByCinema(String cinemaId) {
        return new CinemaCatalog();
    }

    public void updateScreeningMovie(UpdateMovieScreeningTime updateMovieRequest) {

        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            // Getting the real screening to avoid redundant changes
            Screening screeningToUpdate = session.get(Screening.class,
                    updateMovieRequest.getCurrentMovie().getScreeningId());

            // Updating
            screeningToUpdate.setScreeningTimeStampAsString(updateMovieRequest.getNewDateTimeAsString());
            session.beginTransaction();
            session.update(screeningToUpdate);
            session.getTransaction().commit();
        } catch (Exception e) {
        }
    }

//    public MoviesCatalog getAllMoviesCatalog() {
//        // TODO - implement MoviesCatalogController.getAllMoviesCatalog
//        throw new UnsupportedOperationException();
//    }

    /**
     * @param cinemaId
     */
    public MoviesCatalog getScreeningsByCinemaId(String cinemaId) {
        // TODO - implement MoviesCatalogController.getScreeningsByCinemaId
        throw new UnsupportedOperationException();
    }

    /**
     * @param movieData
     */
    public void addMovie(SertiaMovie movieData) {
        // TODO - implement MoviesCatalogController.addMovie
        throw new UnsupportedOperationException();
    }

    /**
     * @param movieId
     */
    public void removeMovie(String movieId) {
        // TODO - implement MoviesCatalogController.removeMovie
        throw new UnsupportedOperationException();
    }

    /**
     * @param screenings
     */
    public void updateMovieScreenings(ClientScreening screenings) {
        // TODO - implement MoviesCatalogController.updateMovieScreenings
        throw new UnsupportedOperationException();
    }

    /**
     * @param newScreening
     */
    public void addMovieScreening(ClientScreening newScreening) {
        // TODO - implement MoviesCatalogController.addMovieScreening
        throw new UnsupportedOperationException();
    }

    /**
     * @param screeningId
     */
    public void removeMovieScreening(String screeningId) {
        // TODO - implement MoviesCatalogController.removeMovieScreening
        throw new UnsupportedOperationException();
    }

    /**
     * @param movieId
     * @param pricePerStream
     */
    public void addStreaming(String movieId, int pricePerStream) {
        // TODO - implement MoviesCatalogController.addStreaming
        throw new UnsupportedOperationException();
    }

    /**
     * @param movieId
     */
    public void removeStreaming(String movieId) {
        // TODO - implement MoviesCatalogController.removeStreaming
        throw new UnsupportedOperationException();
    }

    /**
     * @param movieId
     * @param ticketType
     * @param newPrice
     */
    public void requestPriceChange(String movieId, TicketType ticketType, int newPrice) {
        // TODO - implement MoviesCatalogController.requestPriceChange
        throw new UnsupportedOperationException();
    }

    /**
     * @param movie
     */
    private boolean isMovieValid(SertiaMovie movie) {
        // TODO - implement MoviesCatalogController.isMovieValid
        throw new UnsupportedOperationException();
    }

    /**
     * @param screening
     */
    private boolean isScreeningValid(ClientScreening screening) {
        // TODO - implement MoviesCatalogController.isScreeningValid
        throw new UnsupportedOperationException();
    }

    /**
     * @param newMovie
     */
    private void notifyVoucherOwners(ClientMovie newMovie) {
        // TODO - implement MoviesCatalogController.notifyVoucherOwners
        throw new UnsupportedOperationException();
    }

    private Map<Integer, Streaming> getStreamings() {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Streaming> query = builder.createQuery(Streaming.class);
            query.from(Streaming.class);
            List<Streaming> streamings = session.createQuery(query).getResultList();
            Map<Integer, Streaming> movieToStreaming = new HashMap<>();
            streamings.forEach(streaming -> movieToStreaming.put(streaming.movie.getId(), streaming));

            return movieToStreaming;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private Map<ScreenableMovie, List<Screening>> getScreenings() {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Screening> query = builder.createQuery(Screening.class);
            query.from(Screening.class);
            List<Screening> screenings = session.createQuery(query).getResultList();
            return screenings.stream().collect(Collectors.groupingBy(Screening::getScreenableMovie));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private static ClientMovie movieToClientMovie(Movie movie) {
        ClientMovie clientMovie = new ClientMovie();
        clientMovie.name = movie.getName();
        clientMovie.hebrewName = movie.getHebrewName();
        clientMovie.mainActorName = movie.getMainActor().getFullName();
        clientMovie.producerName = movie.getProducer().getFullName();

        return clientMovie;
    }

    private static ClientScreening screeningToClientScreening(Screening screening) {
        ClientScreening clientScreening = new ClientScreening();
        clientScreening.screeningId = screening.getId();
        clientScreening.screeningTime = screening.getScreeningTimeStampAsString();
        clientScreening.cinemaName = screening.getHall().getCinema().getName();
        clientScreening.hallId = screening.getHall().getId();

        return clientScreening;
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
