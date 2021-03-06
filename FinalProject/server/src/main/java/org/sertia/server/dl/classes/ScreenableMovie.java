package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "screenable_movies")
public class ScreenableMovie {
    @Id
    @Column(name = "movie_id")
    private int id;

    public ScreenableMovie setId(int id) {
        this.id = id;
        return this;
    }

    @OneToOne
    @MapsId
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private double ticketPrice;

    public ScreenableMovie() {
    }

    public ScreenableMovie(double ticketPrice, Movie movie) {
        this.ticketPrice = ticketPrice;
        this.movie = movie;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenableMovie that = (ScreenableMovie) o;
        return id == that.id && Double.compare(that.ticketPrice, ticketPrice) == 0 && Objects.equals(movie, that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movie, ticketPrice);
    }
}

