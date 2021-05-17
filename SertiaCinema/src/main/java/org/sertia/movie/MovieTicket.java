package org.sertia.movie;

public class MovieTicket {
    private String movieName;
    private HallEntranceTicket entranceTicket;

    public MovieTicket(String movieName, HallEntranceTicket entranceTicket) {
        this.movieName = movieName;
        this.entranceTicket = entranceTicket;
    }

    public String getMovieName(){
        return movieName;
    }

    public HallEntranceTicket getEntranceTicket(){
        return entranceTicket;
    }
}
