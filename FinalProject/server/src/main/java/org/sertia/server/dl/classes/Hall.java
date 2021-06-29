package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hall_id")
    private int id;

    public int getHallNumber() {
        return hallNumber;
    }

    private int hallNumber;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "hall_hall_id")
    private Set<HallSeat> seats;

    public Set<Screening> getScreenings() {
        return screenings;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_hall_id")
    private Set<Screening> screenings;

    private int maximumCapacity;
    private int numberOfSeats;

    @ManyToOne
    @JoinColumn(name = "cinema_cinema_id", nullable = false)
    private Cinema cinema;

    public Hall() {
    }

    public Hall(int maximumCapacity, int numberOfSeats, Cinema cinema, int hallNumber) {
        this.maximumCapacity = maximumCapacity;
        this.numberOfSeats = numberOfSeats;
        this.cinema = cinema;
        this.hallNumber = hallNumber;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public int getId() {
        return id;
    }

    public Set<HallSeat> getSeats() {
        return seats;
    }
}
