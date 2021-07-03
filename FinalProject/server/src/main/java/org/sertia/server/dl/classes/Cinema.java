package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Set;

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

    public Set<Hall> getHalls() {
        return halls;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="cinema_cinema_id")
    Set<Hall> halls;

    public Cinema() {
    }

    public Cinema(String name, User manager) {
        this.name = name;
        Manager = manager;
    }

    public int getId() {
        return id;
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
