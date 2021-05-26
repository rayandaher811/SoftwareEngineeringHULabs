package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="hall_id")
    private int id;

    private int maximumCapacity;
    private int numberOfSeats;

    @ManyToOne
    private Cinema cinema;

    public Hall() {
    }

    public Hall(int maximumCapacity, int numberOfSeats, Cinema cinema) {
        this.maximumCapacity = maximumCapacity;
        this.numberOfSeats = numberOfSeats;
        this.cinema = cinema;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public int getId() {
        return id;
    }
}
