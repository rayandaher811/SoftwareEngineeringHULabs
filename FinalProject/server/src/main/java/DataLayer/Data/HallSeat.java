package DataLayer.Data;

import javax.persistence.*;

@Entity
@Table(name="hall_seats")
public class HallSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="seat_id")
    private int id;

    private int rowNumber;
    private int numberInRow;
    private boolean disabled;

    @ManyToOne
    private Hall hall;

    public HallSeat(int rowNumber, int numberInRow, boolean disabled) {
        this.rowNumber = rowNumber;
        this.numberInRow = numberInRow;
        this.disabled = disabled;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }
}
