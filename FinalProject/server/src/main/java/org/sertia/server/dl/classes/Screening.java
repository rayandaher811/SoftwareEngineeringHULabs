package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name = "screenings")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screening_id")
    private int id;

    private String screeningTimeStampAsString;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private ScreenableMovie movie;

    public Screening() {
    }

    public Screening(String screeningTimeAsString, Hall hall, ScreenableMovie movie) {
        this.screeningTimeStampAsString = screeningTimeAsString;
        this.hall = hall;
        this.movie = movie;
    }

    public String getScreeningTimeStampAsString() {
        return screeningTimeStampAsString;
    }

    public Hall getHall() {
        return hall;
    }

    public ScreenableMovie getScreenableMovie() {
        return movie;
    }

    public int getId() {
        return id;
    }

    public void setScreeningTimeStampAsString(String screeningTimeStampAsString) {
        this.screeningTimeStampAsString = screeningTimeStampAsString;
    }
}
