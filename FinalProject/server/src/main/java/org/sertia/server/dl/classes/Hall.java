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

    public Hall() {
    }

    public Hall(int maximumCapacity, int numberOfSeats, Cinema cinema) {
        this.maximumCapacity = maximumCapacity;
        this.numberOfSeats = numberOfSeats;
        this.cinema = cinema;
    }

    @ManyToOne
    private Cinema cinema;
}
