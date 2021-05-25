package org.sertia.server.dl.classes;

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

    public HallSeat() {
    }

    @ManyToOne
    private Hall hall;

    public HallSeat(int rowNumber, int numberInRow, Hall hall) {
        this.rowNumber = rowNumber;
        this.numberInRow = numberInRow;
        this.hall = hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }
}
