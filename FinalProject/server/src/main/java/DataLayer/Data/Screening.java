package DataLayer.Data;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="screenings")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="screening_id")
    private int id;

    private double price;

    private DateTime screeningTime;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private Movie movie;

    public double getPrice() {
        return price;
    }

    public DateTime getScreeningTime() {
        return screeningTime;
    }

    public Hall getHall() {
        return hall;
    }

    public Movie getMovie() {
        return movie;
    }
}
