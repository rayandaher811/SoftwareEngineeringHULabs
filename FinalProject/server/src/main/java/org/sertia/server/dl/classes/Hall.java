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

    @ManyToOne
    private Cinema cinema;
}
