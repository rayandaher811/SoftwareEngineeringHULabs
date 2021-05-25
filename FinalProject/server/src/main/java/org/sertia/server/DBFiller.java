package org.sertia.server;

import org.sertia.server.dl.classes.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class DBFiller {
     private ArrayList<Actor> actors;
     private ArrayList<Producer> producers;
     private ArrayList<Movie> movies;
     private ArrayList<HallSeat> hallSeats;
     private ArrayList<Hall> halls;
     private ArrayList<Cinema> cinemas;
     private ArrayList<Screening> screenings;
     private ArrayList<Streaming> streamings;
     private ArrayList<StreamingLink> streamingLinks;
     private ArrayList<CostumerInfo> costumerInfos;
     private ArrayList<TicketsVoucher> ticketsVouchers;
     private ArrayList<PaymentInfo> paymentInfos;
     private ArrayList<CostumerComplaint> costumerComplaints;
     private ArrayList<Refund> refunds;
     private ArrayList<PriceChangeRequest> priceChangeRequests;
     private ArrayList<User> users;

     public void initialize(){
          fillActors();
          fillProducers();
          fillMovies();
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

     private void fillActors(){
         actors = new ArrayList<Actor>();
         actors.add(new Actor("Roberto Carlos"));
         actors.add(new Actor("Roberto baba"));
         actors.add(new Actor("Roberto bebe"));
         actors.add(new Actor("Roberto huanito"));
    }
     private void fillProducers(){
         producers = new ArrayList<Producer>();
         producers.add(new Producer("producer A"));
         producers.add(new Producer("producer B"));
         producers.add(new Producer("producer C"));
         producers.add(new Producer("producer D"));
    }
     private void fillMovies(){
         movies = new ArrayList<Movie>();
         movies.add(new Movie(producers.get(0),actors.get(0),"בלתי נשכחים", "The expendables", false, "Action movie with many players", "walla.com"));
         movies.add(new Movie(producers.get(1),actors.get(1),"אנטמםן", "Antman", false, "Action movie with many ants", "walla.com"));
         movies.add(new Movie(producers.get(2),actors.get(2),"קפטן אמריקה", "captin America", false, "Action movie with many americans", "walla.com"));
         movies.add(new Movie(producers.get(3),actors.get(3),"עלי באבא", "Ali baba", true, "Action movie with many thieves", "walla.com"));
    }
     private void fillUsers(){
          users = new ArrayList<User>();
          users.add(new User("Admin", "123123",Role.BranchManager));
          users.add(new User("cinemaManagerA", "123123",Role.CinemaManager));
          users.add(new User("cinemaManagerB", "123123",Role.CinemaManager));
          users.add(new User("cinemaManagerC", "123123",Role.CinemaManager));
          users.add(new User("SupporterA", "123123",Role.CostumerSupport));
          users.add(new User("SupporterB", "123123",Role.CostumerSupport));
          users.add(new User("MediaManager", "123123",Role.MediaManager));
     }
     private void fillCinema(){
          cinemas = new ArrayList<Cinema>();
          cinemas.add(new Cinema("Hakerion", users.get(1)));
          cinemas.add(new Cinema("GrandKenion", users.get(2)));
          cinemas.add(new Cinema("HotsotHamifrats", users.get(3)));
     }
     private void fillHalls(){
          halls = new ArrayList<Hall>();
          halls.add(new Hall(50,80,cinemas.get(0)));
          halls.add(new Hall(50,80,cinemas.get(0)));
          halls.add(new Hall(50,80,cinemas.get(1)));
          halls.add(new Hall(50,80,cinemas.get(2)));
          halls.add(new Hall(50,80,cinemas.get(2)));
     }
     private void fillHallSeats(){
          hallSeats = new ArrayList<HallSeat>();

          for (int i = 0; i < 10; i++) {
               for (int j = 0; j < 8; j++) {
                    hallSeats.add(new HallSeat(i,j,halls.get(0)));
                    hallSeats.add(new HallSeat(i,j,halls.get(1)));
                    hallSeats.add(new HallSeat(i,j,halls.get(2)));
                    hallSeats.add(new HallSeat(i,j,halls.get(3)));
                    hallSeats.add(new HallSeat(i,j,halls.get(4)));
               }
          }
     }
     private void fillScreenings(){
          screenings = new ArrayList<Screening>();

          for (int i = 1; i < 29; i++) {
               screenings.add(new Screening(50,new Date(2020,10,i,1,1,1),halls.get(0),movies.get(0)));
               screenings.add(new Screening(50,new Date(2020,10,i,1,1,1),halls.get(1),movies.get(1)));
               screenings.add(new Screening(50,new Date(2020,10,i,1,1,1),halls.get(2),movies.get(2)));
               screenings.add(new Screening(50,new Date(2020,10,i,1,1,1),halls.get(3),movies.get(3)));
               screenings.add(new Screening(50,new Date(2020,10,i,1,1,1),halls.get(4),movies.get(0)));
          }
     }
     private void fillStreamings(){
          streamings = new ArrayList<Streaming>();
          streamings.add(new Streaming(movies.get(0),10));
          streamings.add(new Streaming(movies.get(1),10));
     }
}
