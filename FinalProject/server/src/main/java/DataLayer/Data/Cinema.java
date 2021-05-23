package DataLayer.Data;

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

    public Cinema(String name) {
        this.name = name;
    }
}
