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

    public Hall getHall() {
        return hall;
    }

    @ManyToOne
    @JoinColumn(name="hall_hall_id", nullable=false)
    private Hall hall;

    public HallSeat(int rowNumber, int numberInRow, Hall hall) {
        this.rowNumber = rowNumber;
        this.numberInRow = numberInRow;
        this.hall = hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getNumberInRow() {
        return numberInRow;
    }

    public int getId() {
        return id;
    }
}
