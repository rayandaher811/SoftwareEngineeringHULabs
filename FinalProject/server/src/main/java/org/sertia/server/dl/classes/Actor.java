package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return id == actor.id && Objects.equals(fullName, actor.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName);
    }
}
