package DataLayer.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="screenings")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="screening_id")
    private int id;

    private double price;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private Movie movie;
}
