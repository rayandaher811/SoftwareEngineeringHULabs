package org.sertia.server.bl;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.sertia.contracts.movies.catalog.controller.*;
import org.sertia.contracts.reports.controller.ClientReport;
import org.sertia.server.bl.Services.CreditCardService;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MoviesCatalogController implements Reportable {

    private CreditCardService creditCardService;

    public MoviesCatalogController(){
        creditCardService = new CreditCardService();
    }

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
                    sertiaMovie.ticketPrice = screenableMovie.getTicketPrice();
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

//    public void updateScreeningMovie(UpdateMovieScreeningTime updateMovieRequest) {
//        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
//            // Getting the real screening to avoid redundant changes
//            Screening screeningToUpdate = session.get(Screening.class,
//                    updateMovieRequest.getCurrentMovie().getScreeningId());
//
//            // Updating
//            screeningToUpdate.setScreeningTimeStampAsString(updateMovieRequest.getNewDateTimeAsString());
//            session.beginTransaction();
//            session.update(screeningToUpdate);
//            session.getTransaction().commit();
//        } catch (Exception e) {
//        }
//    }

//    public MoviesCatalog getScreeningsByCinemaId(String cinemaId) {
//        // TODO - implement MoviesCatalogController.getScreeningsByCinemaId
//        throw new UnsupportedOperationException();
//    }

    public void addMovie(SertiaMovie movieData) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            session.beginTransaction();

            Producer producer = new Producer(movieData.movieDetails.producerName);
            session.save(producer);
            
            Actor actor = new Actor(movieData.movieDetails.mainActorName);
            session.save(actor);
            
            Movie newMovie = new Movie(producer,
                    actor,
                    movieData.movieDetails.hebrewName,
                    movieData.movieDetails.name,
                    movieData.isComingSoon,
                    movieData.movieDetails.description,
                    movieData.movieDetails.posterImageUrl);
            session.save(newMovie);

            // Every added movie could be screenable
            ScreenableMovie screenableMovie = new ScreenableMovie(movieData.ticketPrice,
                                                                    newMovie);
            session.save(screenableMovie);

            // In case the movie is streamable
            if(movieData.isStreamable){
                Streaming streaming = new Streaming(newMovie, movieData.pricePerStream);
                session.save(streaming);
            }
            session.flush();
            session.getTransaction().commit();
        } finally {
        }
    }

    /**
     * @param screenings
     */
    public void updateMovieScreenings(ClientScreening screenings) {
        // TODO - implement MoviesCatalogController.updateMovieScreenings
        throw new UnsupportedOperationException();
    }

    public void addMovieScreenings(CinemaScreeningMovie newScreenings) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {

            ScreenableMovie movie = session.get(ScreenableMovie.class, newScreenings.movieDetails.id);

            session.beginTransaction();

            // Adding all requested movies
            for (ClientScreening screening : newScreenings.screenings) {
                Hall screeningHall = session.get(Hall.class, screening.hallId);
                session.save(new Screening(screening.screeningTime,
                                            screeningHall,
                                            movie));
            }

            session.flush();
            session.getTransaction().commit();
        } finally {
        }
    }

    public void removeMovieScreening(int screeningId) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            List<ScreeningTicket> screeningTickets = getAllScreeningTickets(session);

            // Refunding all costumers and deleting the tickets
            for (ScreeningTicket ticket:screeningTickets) {
                if(ticket.getScreening().getId() == screeningId){
                    creditCardService.refund(ticket.getPaymentInfo(), ticket.getPaidPrice());
                    session.remove(ticket);
                }
            }

            session.remove(session.get(Screening.class, screeningId));
        } finally {
        }
    }

    public void removeMovie(int movieId) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            List<Screening> screenings = getAllScreenings(session);
            Streaming streaming = getMovieStreaming(session, movieId);
            LocalDateTime currentTime = LocalDateTime.now();

            // Refunding all canceled screenings customers
            for (Screening screening:screenings) {
                if(screening.getScreenableMovie().getId() == movieId && screening.getScreeningTime().isAfter(currentTime)){
                    RefundAndRemoveAllScreeningTickets(session, screening);
                }

                // Deleting the canceled screening
                session.remove(session.get(Screening.class, screening.getId()));
            }

            session.remove(session.get(ScreenableMovie.class, movieId));
            session.remove(session.get(Movie.class, movieId));
        } finally {
        }
    }

    private Streaming getMovieStreaming(Session session, int movieId) {
        return session.get(Streaming.class, movieId);
    }

    private void RefundAndRemoveAllScreeningTickets(Session session, Screening screening) {
        for (ScreeningTicket ticket:screening.getTickets()) {
            creditCardService.refund(ticket.getPaymentInfo(), ticket.getPaidPrice());
            session.remove(ticket);
        }
    }

    private List<ScreeningTicket> getAllScreeningTickets(Session session) {
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<ScreeningTicket> query = builder.createQuery(ScreeningTicket.class);
        query.from(ScreeningTicket.class);
         return session.createQuery(query).getResultList();
    }

    private List<Screening> getAllScreenings(Session session) {
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Screening> query = builder.createQuery(Screening.class);
        query.from(Screening.class);
        return session.createQuery(query).getResultList();
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
        List<Streaming> streamings = DbUtils.getAll(Streaming.class);
        Map<Integer, Streaming> movieToStreaming = new HashMap<>();
        streamings.forEach(streaming -> movieToStreaming.put(streaming.movie.getId(), streaming));

        return movieToStreaming;
    }

    private Map<ScreenableMovie, List<Screening>> getScreenings() {
        List<Screening> screenings = DbUtils.getAll(Screening.class);
        return screenings.stream().collect(Collectors.groupingBy(Screening::getScreenableMovie));
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
        clientScreening.screeningTime = screening.getScreeningTime();
        clientScreening.cinemaName = screening.getHall().getCinema().getName();
        clientScreening.hallId = screening.getHall().getId();
        List<ScreeningTicket> tickets = screening.getTickets();

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
