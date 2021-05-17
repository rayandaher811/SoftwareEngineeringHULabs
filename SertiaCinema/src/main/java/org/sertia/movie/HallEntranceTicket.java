package org.sertia.movie;

/**
 * Entrance to hall, it has properties to connect ticket and hall, without the movie
 */
public class HallEntranceTicket {
    private int row;
    private int col;
    private int hallNumber;

    public HallEntranceTicket(int row, int col, int hallNumber) {
        this.row = row;
        this.col = col;
        this.hallNumber = hallNumber;
    }

    public int getHallNumber() {
        return hallNumber;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
