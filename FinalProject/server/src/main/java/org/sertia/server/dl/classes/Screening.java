package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name = "screenings")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screening_id")
    private int id;

    private double price;

    private String screeningTimeStampAsString;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private Movie movie;

    public Screening() {
    }

    public Screening(double price, String screeningTimeAsString, Hall hall, Movie movie) {
        this.price = price;
        this.screeningTimeStampAsString = screeningTimeAsString;
        this.hall = hall;
        this.movie = movie;
    }

    public double getPrice() {
        return price;
    }

    public String getScreeningTimeStampAsString() {
        return screeningTimeStampAsString;
    }

    public Hall getHall() {
        return hall;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getId() {
        return id;
    }

    public void setScreeningTimeStampAsString(String screeningTimeStampAsString) {
        this.screeningTimeStampAsString = screeningTimeStampAsString;
    }
}
