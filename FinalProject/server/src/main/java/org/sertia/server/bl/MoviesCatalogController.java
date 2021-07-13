package org.sertia.server.bl;

import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.query.Query;
import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.movies.catalog.*;
import org.sertia.contracts.movies.catalog.request.AddScreeningRequest;
import org.sertia.contracts.movies.catalog.request.CinemaCatalogRequest;
import org.sertia.contracts.movies.catalog.response.CinemaAndHallsResponse;
import org.sertia.contracts.movies.catalog.response.CinemaCatalogResponse;
import org.sertia.contracts.movies.catalog.response.SertiaCatalogResponse;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.server.SertiaException;
import org.sertia.server.bl.Services.CustomerNotifier;
import org.sertia.server.bl.Services.ICreditCardService;
import org.sertia.server.bl.Services.ICustomerNotifier;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MoviesCatalogController extends Reportable {

    private final ICreditCardService creditCardService;
    private final ICustomerNotifier notifier;
    private final ScheduledExecutorService firstScreeningsExecutor;

    public MoviesCatalogController(ICreditCardService creditCardService) {
        this.creditCardService = creditCardService;
        notifier = CustomerNotifier.getInstance();
        firstScreeningsExecutor = Executors.newSingleThreadScheduledExecutor();
        firstScreeningsExecutor.scheduleAtFixedRate(
                this::checkForFirstScreenings,
                0,
                1,
                TimeUnit.DAYS);
    }

    public CinemaCatalogResponse getCinemaCatalog(CinemaCatalogRequest request) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            Cinema cinema = session.get(Cinema.class, request.cinemaId);
            List<Screening> screenings = cinema.getHalls().stream()
                    .flatMap(hall -> hall.getScreenings().stream())
                    .collect(Collectors.toList());
            Map<ScreenableMovie, List<Screening>> screenableMoviesMap = getScreenableMovieMap(screenings);
            CinemaCatalogResponse cinemaCatalogResponse = new CinemaCatalogResponse(true);
            screenableMoviesMap.keySet()
                    .forEach(screenableMovie -> {
                        Movie movie = screenableMovie.getMovie();
                        CinemaScreeningMovie cinemaScreeningMovie = new SertiaMovie();
                        cinemaScreeningMovie.movieId = movie.getId();
                        cinemaScreeningMovie.movieDetails = movieToClientMovie(movie);
                        cinemaScreeningMovie.ticketPrice = screenableMovie.getTicketPrice();
                        cinemaScreeningMovie.screenings = screenableMoviesMap.get(screenableMovie).stream().map(MoviesCatalogController::screeningToClientScreening)
                                .collect(Collectors.toList());
                        cinemaCatalogResponse.movies.add(cinemaScreeningMovie);
                    });

            return cinemaCatalogResponse;
        } catch (RuntimeException exception) {
            return new CinemaCatalogResponse(false);
        }
    }

    public SertiaCatalogResponse getSertiaCatalog() throws SertiaException {
        try {
            Map<ScreenableMovie, List<Screening>> screeningMovies = getScreenings();
            Map<Integer, Streaming> streamings = getStreamings();
            List<SertiaMovie> sertiaMovieList = new ArrayList<>();

            screeningMovies.keySet()
                    .forEach(screenableMovie -> {
                        Movie movie = screenableMovie.getMovie();
                        SertiaMovie sertiaMovie = new SertiaMovie();
                        sertiaMovie.movieId = movie.getId();
                        sertiaMovie.movieDetails = movieToClientMovie(movie);
                        sertiaMovie.isComingSoon = movie.isComingSoon();
                        sertiaMovie.ticketPrice = screenableMovie.getTicketPrice();
                        sertiaMovie.screenings = screeningMovies.get(screenableMovie).stream().map(MoviesCatalogController::screeningToClientScreening)
                                .collect(Collectors.toList());
                        Optional.ofNullable(streamings.get(movie.getId()))
                                .ifPresent(streaming -> {
                                    sertiaMovie.isStreamable = true;
                                    sertiaMovie.extraDayPrice = streaming.extraHourPrice;
                                });
                        sertiaMovieList.add(sertiaMovie);
                    });

            return new SertiaCatalogResponse(true, sertiaMovieList);
        } catch (Exception e) {
            throw new SertiaException("ארעה שגיאה בעת קבלת קטלוג הסרטיה");
        }
    }

    public ClientMovie getMovieById(int movieId) throws SertiaException {
        try {
            Optional<Movie> movie = DbUtils.getById(Movie.class, movieId);

            if (!movie.isPresent())
                throw new SertiaException("הסרט המתבקש אינו קיים");

            return movieToClientMovie(movie.get());
        } catch (JDBCConnectionException e) {
            throw new SertiaException("נתלקנו בתקלה בעת ביצוע בקשתך לנתוני סרט");
        }
    }

    public SertiaBasicResponse addMovie(SertiaMovie movieData) {
        Session session = null;
        if (!isMovieValid(movieData)) {
            return new SertiaBasicResponse(false).setFailReason("נתוני הסרט אינם תקינים");
        }

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
                    true,
                    movieData.movieDetails.description,
                    movieData.movieDetails.posterImageUrl,
                    movieData.movieDetails.duration);
            session.save(newMovie);

            // Every added movie could be screenable
            ScreenableMovie screenableMovie = new ScreenableMovie(movieData.ticketPrice,
                    newMovie);
            session.save(screenableMovie);

            // In case the movie is streamable
            if (movieData.isStreamable) {
                Streaming streaming = new Streaming(newMovie, movieData.extraDayPrice);
                session.save(streaming);
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

        return new SertiaBasicResponse(true);
    }

    public void updateScreeningTime(ClientScreening screening) throws SertiaException {
        if (screening.screeningTime.isBefore(LocalDateTime.now()))
            throw new SertiaException("שעת ההקרנה החדשה היא בעבר");

        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
            // Getting the real screening to avoid redundant changes
            Screening screeningToUpdate = session.get(Screening.class, screening.screeningId);
            validateScreeningTime(screening.screeningId, screening.screeningTime, screeningToUpdate.getScreenableMovie().getMovie(), screeningToUpdate.getHall());
            // Updating
            LocalDateTime oldScreeningTime = screeningToUpdate.getScreeningTime();
            screeningToUpdate.setScreeningTime(screening.screeningTime);
            session.beginTransaction();
            session.update(screeningToUpdate);

            // Notifing ll relevant customers for the update
            screeningToUpdate.getTickets().stream()
                    .collect(Collectors.groupingBy(ScreeningTicket::getPaymentInfo))
                    .forEach((paymentDetails, screeningTickets) ->
                            notifier.notify(paymentDetails.getEmail(),
                                    "Your screening at " + oldScreeningTime + " in sertia cinema had been rescheduled to " + screening.screeningTime));

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

    public void addMovieScreenings(AddScreeningRequest addScreeningRequest) throws SertiaException {
        if (addScreeningRequest.screeningTime.isBefore(LocalDateTime.now()))
            throw new SertiaException("זמן ההקרנה שהוזן הוא בעבר");


        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            Movie movie = getMovieById(addScreeningRequest.movieId, session);

            Hall selectedHall = getHall(addScreeningRequest.hallId, session);

            validateScreeningTime(addScreeningRequest.screeningTime, movie, selectedHall);

            ScreenableMovie screenableMovie = DbUtils.getById(ScreenableMovie.class, addScreeningRequest.movieId).get();

            Screening screening = new Screening();
            screening.setMovie(screenableMovie);
            screening.setScreeningTime(addScreeningRequest.screeningTime);

            screening.setHall(selectedHall);
            session.save(screening);
        }
    }

    private Hall getHall(int hallId, Session session) throws SertiaException {
        Optional<Hall> hall = DbUtils.getById(Hall.class, hallId, session);
        if (!hall.isPresent())
            throw new SertiaException("האולם עם המזהה : " + hallId + " לא נמצא במערכת.");

        return hall.get();
    }

    private void validateScreeningTime(LocalDateTime screeningDateTime, Movie movie, Hall hall) throws SertiaException {
        validateScreeningTime(null, screeningDateTime, movie, hall);
    }

    private void validateScreeningTime(Integer screeningId, LocalDateTime screeningDateTime, Movie movie, Hall hall) throws SertiaException {
        LocalDateTime movieStartTime = screeningDateTime;
        LocalDateTime movieEndTime = screeningDateTime.plus(movie.getDuration());

        for (Screening screening : hall.getScreenings()) {
            LocalDateTime hallScreeningStartTime = screening.getScreeningTime();
            LocalDateTime hallScreeningEndTime = screening.getScreeningTime().plus(movie.getDuration());

            // Making sure all hall screenings does not conflict the current screening
            if (!((movieStartTime.isAfter(hallScreeningEndTime) && movieStartTime.isAfter(hallScreeningStartTime) &&
                    movieEndTime.isAfter(hallScreeningEndTime) && movieEndTime.isAfter(hallScreeningStartTime)) ||
                    (movieStartTime.isBefore(hallScreeningEndTime) && movieStartTime.isBefore(hallScreeningStartTime) &&
                            movieEndTime.isBefore(hallScreeningEndTime) && movieEndTime.isBefore(hallScreeningStartTime)))) {
                if (screeningId != null && screeningId == screening.getId()) {
                    continue;
                }

                throw new SertiaException("הזמן שהזנת מתנגש עם הקרנה אחרת באותו האולם");
            }
        }
    }

    public void removeMovieScreening(int screeningId) throws SertiaException {
        removeMovieScreening(screeningId, RefundReason.ScreeningService);
    }

    public void removeMovieScreeningDueCovid(int screeningId) throws SertiaException {
        removeMovieScreening(screeningId, RefundReason.RegulationChange);
    }

    public void removeMovieScreening(int screeningId, RefundReason refundReason) throws SertiaException {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
            Screening screening = session.get(Screening.class, screeningId);
            if (screening == null)
                throw new SertiaException("הקרנה אינה קיימת");

            session.beginTransaction();

            // Refunding all relevant costumers and deleting the tickets
            RefundAndRemoveAllScreeningTickets(session, screening, refundReason);

            session.flush();
            session.remove(screening);
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

    public void removeMovie(int movieId) throws SertiaException {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
            Movie movie = getMovieById(movieId, session);
            ScreenableMovie screenableMovie = session.get(ScreenableMovie.class, movieId);

            if (movie == null)
                throw new SertiaException("אין סרט במערכת עם המזהה " + movieId + ".");

            if (screenableMovie != null) {
                List<Screening> screenings = DbUtils.getAll(Screening.class);
                LocalDateTime currentTime = LocalDateTime.now();

                session.beginTransaction();

                // Refunding all canceled screenings customers
                for (Screening screening : screenings) {
                    if (screening.getScreenableMovie().getId() == movieId && screening.getScreeningTime().isAfter(currentTime)) {
                        RefundAndRemoveAllScreeningTickets(session, screening, RefundReason.ScreeningService);

                        // Deleting the canceled screening
                        session.remove(session.get(Screening.class, screening.getId()));
                    }
                }
                session.flush();

                // Deleting all movie price changes request
                for (PriceChangeRequest request : DbUtils.getAll(PriceChangeRequest.class)) {
                    if(request.getMovie().getId() == movieId)
                        session.remove(request);
                }

                session.flush();

                session.remove(session.get(ScreenableMovie.class, movieId));
            }

            removeStreamingViaFoundSession(movieId, session);
            session.flush();
            session.remove(session.get(Movie.class, movieId));
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

    public void addStreaming(int movieId, double pricePerStream) throws SertiaException {
        if (pricePerStream < 0)
            throw new SertiaException("מחר אינו יכול להיות שלילי");
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();
            if (getMovieStreaming(session, movieId) != null)
                throw new SertiaException("לסרט יש חבילת צפייה קיימת");

            Movie movie = getMovieById(movieId, session);
            Streaming newStreaming = new Streaming(movie, pricePerStream);

            // Saving the streaming movie
            session.beginTransaction();
            session.save(newStreaming);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
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
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public SertiaBasicResponse getCinemaAndHalls() {
        Map<String, List<ClientHall>> cinemaToHalls = new HashMap<>();
        DbUtils.getAll(Cinema.class).forEach(cinema -> cinemaToHalls.put(cinema.getName(),
                cinema.getHalls().stream()
                        .map(hall -> new ClientHall(hall.getId(), hall.getHallNumber()))
                        .collect(Collectors.toList())));
        return new CinemaAndHallsResponse(true, cinemaToHalls);
    }

    private void removeStreamingViaFoundSession(int movieId, Session session) {
        Streaming streaming = session.get(Streaming.class, movieId);

        // The movie have no streaming
        if (streaming == null)
            return;

        deleteAllRelatedLinks(movieId, session);

        session.flush();
        session.remove(streaming);
    }

    private void deleteAllRelatedLinks(int movieId, Session session) {
        LocalDateTime now = LocalDateTime.now();

        // Deleting and refund the relevant ones all movie related links
        for (StreamingLink link : DbUtils.getAll(StreamingLink.class)) {
            if(link.getMovie().id == movieId) {

                // Refunding relevants
                if(link.getActivationStart().isAfter(now) ||
                        (now.isAfter(link.getActivationStart()) && now.isBefore(link.getActivationEnd())))
                {
                    creditCardService.refund(link.getCustomerPaymentDetails(), link.getPaidPrice(), RefundReason.StreamingService);
                    notifier.notify(link.getCustomerPaymentDetails().getEmail(), "לינק הסטרים שרכשת " + link.getLink() + " התבטל בעקבות החלטת אדמינטרציה.");
                }

                // Deleting all link related complaints (The link has been already refunded)
                for (CostumerComplaint complaint : DbUtils.getAll(CostumerComplaint.class)) {
                    if(complaint.getTicketType() == TicketType.Streaming &&
                            complaint.getStreamingLink().getId() == link.getId())
                        session.remove(complaint);
                }

                session.flush();

                // Deleting
                session.remove(link);
            }
        }

        session.flush();
    }

    private Streaming getMovieStreaming(Session session, int movieId) {
        return session.get(Streaming.class, movieId);
    }

    private void RefundAndRemoveAllScreeningTickets(Session session, Screening screening, RefundReason refundReason) {
        screening.getTickets().stream()
                .collect(Collectors.groupingBy(ScreeningTicket::getPaymentInfo))
                .forEach((paymentDetails, screeningTickets) -> {
                    Map<TicketsVoucher, Integer> voucherToUsedAmount = new HashMap<>();
                    int fromVouchers = 0;
                    for (ScreeningTicket ticket : screeningTickets) {
                        if (ticket.isVoucher()) {
                            voucherToUsedAmount.putIfAbsent(ticket.getVoucher(), 0);
                            voucherToUsedAmount.put(ticket.getVoucher(), voucherToUsedAmount.get(ticket.getVoucher()) + 1);
                            fromVouchers++;
                        }
                    }
                    voucherToUsedAmount.forEach((ticketsVoucher, addToBalance) -> {
                        ticketsVoucher.setTicketsBalance(ticketsVoucher.getTicketsBalance() + addToBalance);
                        session.saveOrUpdate(ticketsVoucher);
                        notifier.notify(paymentDetails.getEmail(), "Your voucher " + ticketsVoucher.getId() + " has been refunded with " + addToBalance + ".\n now the balance is " + ticketsVoucher.getTicketsBalance());
                    });

                    double refundAmount = screening.getScreenableMovie().getTicketPrice() * (screeningTickets.size() - fromVouchers);
                    notifier.notify(paymentDetails.getEmail(), "Your screening at " + screening.getScreeningTime() + " in sertia cinema has been canceled.");
                    creditCardService.refund(paymentDetails, refundAmount, refundReason);

                    // Deleting all relevant complaints (They have been refunded)
                    for (CostumerComplaint complaint : DbUtils.getAll(CostumerComplaint.class)) {
                        if((complaint.getTicketType() == TicketType.Screening || complaint.getTicketType() == TicketType.Voucher)){
                            for (ScreeningTicket ticket:screeningTickets) {
                                if(complaint.getScreeningTicket().getId() == ticket.getId())
                                    session.remove(complaint);
                            }
                        }
                    }

                    session.flush();
                    screeningTickets.forEach(session::remove);
                });
    }

    private boolean isMovieValid(SertiaMovie movie) {
        return isMovieDetailsValid(movie.movieDetails);
    }

    private boolean isMovieDetailsValid(ClientMovie movie) {
        return movie.getDescription() != null &&
                movie.getHebrewName() != null &&
                movie.getName() != null &&
                movie.getMainActorName() != null &&
                movie.getProducerName() != null;
    }

    private void notifyVoucherOwners(ClientMovie newMovie, List<String> emails) {
        emails.forEach(email -> {
            notifier.notify(email, "movie " + newMovie.name + " " +
                    "is screening today for the first time!");
        });
    }

    private void checkForFirstScreenings() {
        List<TicketsVoucher> vouchers = DbUtils.getAll(TicketsVoucher.class);
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            Query comingSoonQuery = session
                    .createQuery("from movies where isComingSoon = :isComingSoon");
            comingSoonQuery.setParameter("isComingSoon", true);
            List<Movie> movies = comingSoonQuery.list();

            movies.forEach(movie -> {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Screening> criteriaQuery = builder.createQuery(Screening.class);
                Root<Screening> screeningRoot = criteriaQuery.from(Screening.class);
                criteriaQuery.select(screeningRoot);
                criteriaQuery.orderBy(builder.asc(screeningRoot.get("screeningTime")));
                Screening earliestScreening = session.createQuery(criteriaQuery).getResultList().get(0);

                if (isDateToday(earliestScreening.getScreeningTime())) {
                    Movie comingSoonMovie = earliestScreening.getScreenableMovie().getMovie();
                    comingSoonMovie.setComingSoon(false);
                    session.saveOrUpdate(comingSoonMovie);

                    notifyVoucherOwners(
                            movieToClientMovie(earliestScreening.getScreenableMovie().getMovie()),
                            vouchers.stream()
                                    .map(ticketsVoucher -> ticketsVoucher.getCustomerPaymentDetails().getEmail())
                                    .collect(Collectors.toList()));
                }
            });
        }
    }

    private boolean isDateToday(LocalDateTime dateTime) {
        return dateTime.toLocalDate().equals(LocalDate.now());
    }

    private Map<Integer, Streaming> getStreamings() {
        List<Streaming> streamings = DbUtils.getAll(Streaming.class);
        Map<Integer, Streaming> movieToStreaming = new HashMap<>();
        streamings.forEach(streaming -> movieToStreaming.put(streaming.movie.getId(), streaming));

        return movieToStreaming;
    }

    private Map<ScreenableMovie, List<Screening>> getScreenings() {
        List<Screening> screenings = DbUtils.getAll(Screening.class);
        List<ScreenableMovie> movies = DbUtils.getAll(ScreenableMovie.class);
        Map<ScreenableMovie, List<Screening>> moviesWithScreeningsMap = getScreenableMovieMap(screenings);

        for (ScreenableMovie screenableMovie : movies) {
            moviesWithScreeningsMap.putIfAbsent(screenableMovie, Collections.emptyList());
        }

        return moviesWithScreeningsMap;
    }

    private Map<ScreenableMovie, List<Screening>> getScreenableMovieMap(List<Screening> screenings) {
        return screenings.stream().collect(Collectors.groupingBy(Screening::getScreenableMovie));
    }

    private static ClientMovie movieToClientMovie(Movie movie) {
        ClientMovie clientMovie = new ClientMovie();
        clientMovie.name = movie.getName();
        clientMovie.hebrewName = movie.getHebrewName();
        clientMovie.mainActorName = movie.getMainActor().getFullName();
        clientMovie.producerName = movie.getProducer().getFullName();
        clientMovie.description = movie.getDescription();
        clientMovie.posterImageUrl = movie.getImageUrl();
        clientMovie.duration = movie.getDuration();

        return clientMovie;
    }

    private static ClientScreening screeningToClientScreening(Screening screening) {
        ClientScreening clientScreening = new ClientScreening();
        clientScreening.screeningId = screening.getId();
        clientScreening.screeningTime = screening.getScreeningTime();
        clientScreening.cinemaName = screening.getHall().getCinema().getName();
        clientScreening.hallId = screening.getHall().getId();

        return clientScreening;
    }

    public static Movie getMovieById(int movieId, Session session) throws SertiaException {
        Movie movie = session.get(Movie.class, movieId);

        if (movie == null)
            throw new SertiaException("לא קיים סרט שכזה");

        return movie;
    }

    @Override
    public List<ClientReport> createSertiaReports() {
        return Collections.emptyList();
    }

    @Override
    public List<ClientReport> createCinemaReports(int cinemaId) {
        return Collections.emptyList();
    }
}
