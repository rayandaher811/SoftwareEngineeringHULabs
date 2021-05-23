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

    @ManyToOne
    private Cinema cinema;

    public Hall(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
        this.cinema = cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
}
