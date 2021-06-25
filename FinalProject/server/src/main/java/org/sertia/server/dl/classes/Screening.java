package org.sertia.server.dl.classes;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "screenings")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screening_id")
    private int id;

    private LocalDateTime screeningTime;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private ScreenableMovie movie;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "screening_screening_id")
    private List<ScreeningTicket> tickets;

    public Screening() {
    }

    public Screening(LocalDateTime screeningTime, Hall hall, ScreenableMovie movie) {
        this.hall = hall;
        this.movie = movie;
        this.screeningTime = screeningTime;
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

    public List<ScreeningTicket> getTickets() {
        return tickets;
    }
}
