package org.sertia.server.pojos;

import java.util.Date;
import java.util.Objects;

public class ScreeningMovie {
    private Movie movie;
    private Date playingTime;
    private double price;

    public ScreeningMovie(Movie movie, long playingTime, double price) {
        this.movie = movie;
        this.playingTime = playingTime;
        this.price = price;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setPlayingTime(long playingTime) {
        this.playingTime = playingTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Movie getMovie() {
        return movie;
    }

    public long getPlayingTime() {
        return playingTime;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreeningMovie that = (ScreeningMovie) o;
        return playingTime == that.playingTime &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(movie, that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, playingTime, price);
    }
}
