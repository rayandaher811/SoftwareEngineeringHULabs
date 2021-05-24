package DataLayer.Data;

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

    private Date screeningTime;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private Movie movie;

    public double getPrice() {
        return price;
    }

    public Date getScreeningTime() {
        return screeningTime;
    }

    public Hall getHall() {
        return hall;
    }

    public Movie getMovie() {
        return movie;
    }
}
