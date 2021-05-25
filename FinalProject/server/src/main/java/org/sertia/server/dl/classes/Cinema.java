package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="cinemas")
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="cinema_id")
    private int id;

    private String name;

    @ManyToOne
    private User Manager;

    public Cinema(String name) {
        this.name = name;
    }
}
