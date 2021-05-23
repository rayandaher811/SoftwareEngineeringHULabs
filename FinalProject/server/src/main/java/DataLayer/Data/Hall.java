package DataLayer.Data;

import javax.persistence.*;
import java.util.Set;

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
}
