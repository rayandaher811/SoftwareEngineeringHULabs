package org.sertia.server.dl.classes;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="screenings")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="screening_id")
    private int id;

    private double price;

    private Date screeningTime;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private Movie movie;

    public Screening() {
    }

    public Screening(double price, Date screeningTime, Hall hall, Movie movie) {
        this.price = price;
        this.screeningTime = screeningTime;
        this.hall = hall;
        this.movie = movie;
    }

    public double getPrice() {
        return price;
    }

    public Date getScreeningTime() {
        return screeningTime;
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

    public void setScreeningTime(Date screeningTime) {
        this.screeningTime = screeningTime;
    }
}
