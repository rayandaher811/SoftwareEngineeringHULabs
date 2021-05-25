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

    public Cinema() {
    }

    public Cinema(String name, User manager) {
        this.name = name;
        Manager = manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getManager() {
        return Manager;
    }

    public void setManager(User manager) {
        Manager = manager;
    }

    public Cinema(String name) {
        this.name = name;
    }
}
