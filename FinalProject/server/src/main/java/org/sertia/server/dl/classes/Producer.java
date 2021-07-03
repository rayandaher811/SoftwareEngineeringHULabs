package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="producers")
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="producer_id")
    private int id;
    private String fullName;

    public Producer() {
    }

    public Producer(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producer producer = (Producer) o;
        return id == producer.id && Objects.equals(fullName, producer.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName);
    }
}
