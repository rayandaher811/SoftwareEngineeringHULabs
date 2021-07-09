package org.sertia.server;

import org.sertia.server.dl.classes.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class    DBFiller {
    private ArrayList<Actor> actors;
    private ArrayList<Producer> producers;
    private ArrayList<Movie> movies;
    private ArrayList<ScreenableMovie> screenableMovies;
    private ArrayList<HallSeat> hallSeats;
    private ArrayList<Hall> halls;
    private ArrayList<Cinema> cinemas;
    private ArrayList<Screening> screenings;
    private ArrayList<Streaming> streamings;
    private ArrayList<StreamingLink> streamingLinks;
    private ArrayList<TicketsVoucher> ticketsVouchers;
    private ArrayList<CustomerPaymentDetails> customerPaymentDetails;
    private ArrayList<CostumerComplaint> costumerComplaints;
    private ArrayList<Refund> refunds;
    private ArrayList<PriceChangeRequest> priceChangeRequests;
    private ArrayList<User> users;

    public void initialize() {
        fillActors();
        fillProducers();
        fillMovies();
        fillScreenableMovies();
        fillUsers();
        fillCinema();
        fillHalls();
        fillHallSeats();
        fillScreenings();
        fillStreamings();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ArrayList<Producer> getProducers() {
        return producers;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<ScreenableMovie> getScreenableMovies() {
        return screenableMovies;
    }

    public ArrayList<HallSeat> getHallSeats() {
        return hallSeats;
    }

    public ArrayList<Hall> getHalls() {
        return halls;
    }

    public ArrayList<Cinema> getCinemas() {
        return cinemas;
    }

    public ArrayList<Screening> getScreenings() {
        return screenings;
    }

    public ArrayList<Streaming> getStreamings() {
        return streamings;
    }

    private void fillActors() {
        actors = new ArrayList<Actor>();
        actors.add(new Actor("Roberto Carlos"));
        actors.add(new Actor("Roberto baba"));
        actors.add(new Actor("Roberto bebe"));
        actors.add(new Actor("Roberto huanito"));
    }

    private void fillProducers() {
        producers = new ArrayList<Producer>();
        producers.add(new Producer("producer A"));
        producers.add(new Producer("producer B"));
        producers.add(new Producer("producer C"));
        producers.add(new Producer("producer D"));
    }

    private void fillMovies() {
        movies = new ArrayList<Movie>();
        movies.add(new Movie(producers.get(0), actors.get(0), "בלתי נשכחים", "The expendables", false, "Action movie with many players", "walla.com", Duration.ofHours(1)));
        movies.add(new Movie(producers.get(1), actors.get(1), "אנטמםן", "Antman", false, "Action movie with many ants", "walla.com", Duration.ofHours(1)));
        movies.add(new Movie(producers.get(2), actors.get(2), "קפטן אמריקה", "captin America", false, "Action movie with many americans", "walla.com", Duration.ofHours(1)));
        movies.add(new Movie(producers.get(3), actors.get(3), "עלי באבא", "Ali baba", true, "Action movie with many thieves", "walla.com", Duration.ofHours(1)));
    }

    private void fillScreenableMovies() {
        screenableMovies = new ArrayList<ScreenableMovie>();
        screenableMovies.add(new ScreenableMovie(30, movies.get(0)));
        screenableMovies.add(new ScreenableMovie(40, movies.get(1)));
        screenableMovies.add(new ScreenableMovie(50, movies.get(2)));
        screenableMovies.add(new ScreenableMovie(60, movies.get(3)));
    }

    private void fillUsers() {
        users = new ArrayList<User>();
        users.add(new User("Admin", "123123", Role.CinemaManager));
        users.add(new User("cinemaManagerA", "123123", Role.BranchManager));
        users.add(new User("cinemaManagerB", "123123", Role.BranchManager));
        users.add(new User("cinemaManagerC", "123123", Role.BranchManager));
        users.add(new User("SupporterA", "123123", Role.CostumerSupport));
        users.add(new User("SupporterB", "123123", Role.CostumerSupport));
        users.add(new User("MediaManager", "123123", Role.MediaManager));
    }

    private void fillCinema() {
        cinemas = new ArrayList<Cinema>();
        cinemas.add(new Cinema("Hakerion", users.get(1)));
        cinemas.add(new Cinema("GrandKenion", users.get(2)));
        cinemas.add(new Cinema("HotsotHamifrats", users.get(3)));
    }

    private void fillHalls() {
        halls = new ArrayList<Hall>();
        halls.add(new Hall(50, 80, cinemas.get(0), 1));
        halls.add(new Hall(50, 80, cinemas.get(0), 2));
        halls.add(new Hall(50, 80, cinemas.get(1),1));
        halls.add(new Hall(50, 80, cinemas.get(2),1 ));
        halls.add(new Hall(50, 80, cinemas.get(2), 2));
    }

    private void fillHallSeats() {
        hallSeats = new ArrayList<HallSeat>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                hallSeats.add(new HallSeat(i, j, halls.get(0)));
                hallSeats.add(new HallSeat(i, j, halls.get(1)));
                hallSeats.add(new HallSeat(i, j, halls.get(2)));
                hallSeats.add(new HallSeat(i, j, halls.get(3)));
                hallSeats.add(new HallSeat(i, j, halls.get(4)));
            }
        }
    }

    private void fillScreenings() {
        screenings = new ArrayList<>();

        for (int i = 1; i < 29; i++) {
            Screening e = new Screening(dateToTimeStamp(2021, Calendar.NOVEMBER, i, 1, 30), halls.get(0), screenableMovies.get(0));
            screenings.add(e);
            screenings.add(new Screening(dateToTimeStamp(2021, Calendar.NOVEMBER, i, 2, 10), halls.get(1), screenableMovies.get(1)));
            screenings.add(new Screening(dateToTimeStamp(2021, Calendar.NOVEMBER, i, 4, 40), halls.get(2), screenableMovies.get(2)));
            screenings.add(new Screening(dateToTimeStamp(2021, Calendar.NOVEMBER, i, 1, 25), halls.get(3), screenableMovies.get(3)));
            screenings.add(new Screening(dateToTimeStamp(2021, Calendar.NOVEMBER, i, 11, 30), halls.get(4), screenableMovies.get(0)));
        }
    }


    private java.time.LocalDateTime dateToTimeStamp(int year, int month, int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, min);
        return LocalDateTime.of(year, month, day, hour, min);
    }

    private void fillStreamings() {
        streamings = new ArrayList<Streaming>();
        streamings.add(new Streaming(movies.get(0), 10));
        streamings.add(new Streaming(movies.get(1), 10));
    }
}
