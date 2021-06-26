package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.*;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.server.bl.Services.CreditCardService;
import org.sertia.server.bl.Services.CustomerNotifier;
import org.sertia.server.bl.Services.ICustomerNotifier;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MoviesCatalogController implements Reportable {

    private CreditCardService creditCardService;
    private ICustomerNotifier notifier;

    public MoviesCatalogController(){
        creditCardService = new CreditCardService();
        notifier = CustomerNotifier.getInstance();
    }

    public SertiaBasicResponse getSertiaCatalog() {
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

        return new SertiaCatalogResponse(true, sertiaMovieList);
    }

    public void addMovie(SertiaMovie movieData) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
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
            session.clear();
            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        }
        finally {
            session.close();
        }
    }

    public void updateScreeningTime(ClientScreening screening) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
            // Getting the real screening to avoid redundant changes
            Screening screeningToUpdate = session.get(Screening.class, screening.screeningId);

            // Updating
            screeningToUpdate.setScreeningTime(screening.screeningTime);
            session.beginTransaction();
            session.update(screeningToUpdate);

            // Notifing ll relevant customers for the update
            for (ScreeningTicket ticket : screeningToUpdate.getTickets()) {
                notifier.notify(ticket.getPaymentInfo().getPhoneNumber(),
                                "Your screening at " + screeningToUpdate.getScreeningTime() + " in sertia cinema had been postponed to " + screening.screeningTime);
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void addMovieScreenings(CinemaScreeningMovie newScreenings) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
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
        } catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    public void removeMovieScreening(int screeningId) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
            Screening screening = session.get(Screening.class, screeningId);
            List<ScreeningTicket> screeningTickets = screening.getTickets();

            session.beginTransaction();

            // Refunding all relevant costumers and deleting the tickets
            for (ScreeningTicket ticket:screeningTickets) {
                creditCardService.refund(ticket.getPaymentInfo(), ticket.getPaidPrice());
                session.remove(ticket);
            }

            session.remove(screening);

            session.flush();
            session.clear();
            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    public void removeMovie(int movieId) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
            List<Screening> screenings = getAllScreenings(session);
            Streaming streaming = getMovieStreaming(session, movieId);
            LocalDateTime currentTime = LocalDateTime.now();

            session.beginTransaction();

            // Refunding all canceled screenings customers
            for (Screening screening:screenings) {
                if(screening.getScreenableMovie().getId() == movieId && screening.getScreeningTime().isAfter(currentTime)){
                    RefundAndRemoveAllScreeningTickets(session, screening);
                }

                // Deleting the canceled screening
                session.remove(session.get(Screening.class, screening.getId()));
            }

            removeStreamingViaFoundSession(movieId, session);

            session.remove(session.get(ScreenableMovie.class, movieId));
            session.remove(session.get(Movie.class, movieId));
            session.flush();
            session.clear();
            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    public void addStreaming(int movieId, double pricePerStream) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
            Movie movie = session.get(Movie.class, movieId);
            Streaming newStreaming = new Streaming(movie, pricePerStream);

            // Saving the streaming movie
            session.beginTransaction();
            session.save(newStreaming);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void removeStreaming(int movieId) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
            session.beginTransaction();
            removeStreamingViaFoundSession(movieId, session);
            session.flush();
            session.clear();
            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    private void removeStreamingViaFoundSession(int movieId, Session session) {
        Streaming streaming = session.get(Streaming.class, movieId);
        LocalDateTime currentTime = LocalDateTime.now();

        // Refunding all relevant canceled link + deleting them
        for (StreamingLink link:streaming.getLinks()) {
            if(link.getActivationEnd().isAfter(currentTime)){
                creditCardService.refund(link.getCustomerPaymentDetails(), link.getPaidPrice());
            }

            // Deleting the canceled screening
            session.remove(link);
        }

        session.remove(streaming);
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

    private List<Screening> getAllScreenings(Session session) {
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Screening> query = builder.createQuery(Screening.class);
        query.from(Screening.class);
        return session.createQuery(query).getResultList();
    }

    private boolean isMovieValid(SertiaMovie movie) {
        // TODO - implement MoviesCatalogController.isMovieValid
        throw new UnsupportedOperationException();
    }

    private boolean isScreeningValid(ClientScreening screening) {
        // TODO - implement MoviesCatalogController.isScreeningValid
        throw new UnsupportedOperationException();
    }

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
