package org.sertia.server.dl.classes;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "screenings")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screening_id")
    private int id;

    private LocalDateTime screeningTime;

    @ManyToOne
    @JoinColumn(name="hall_hall_id", nullable=false)
    private Hall hall;

    @ManyToOne
    @JoinColumn(nullable=false)
    private ScreenableMovie movie;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "screening_screening_id")
    private Set<ScreeningTicket> tickets;

    public Screening() {
    }

    public Screening(LocalDateTime screeningTime, Hall hall, ScreenableMovie movie) {
        this.hall = hall;
        this.movie = movie;
        this.screeningTime = screeningTime;
    }

    public Screening setHall(Hall hall) {
        this.hall = hall;
        return this;
    }

    public Screening setMovie(ScreenableMovie movie) {
        this.movie = movie;
        return this;
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

    public LocalDateTime getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(LocalDateTime screeningTime) {
        this.screeningTime = screeningTime;
    }

    public Set<ScreeningTicket> getTickets() {
        return tickets;
    }
}
