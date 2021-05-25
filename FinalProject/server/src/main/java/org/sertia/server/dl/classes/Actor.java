package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="actors")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="actor_id")
    private int id;
    private String fullName;

    public Actor(String fullName) {
        this.fullName = fullName;
    }

    public Actor() {
    }

    public String getFullName() {
        return fullName;
    }
}
