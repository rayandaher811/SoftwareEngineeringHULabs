package org.sertia.client.boxes;

public class SeatBox {
    public int chooseSeatButton;
    public int rowNumber;
    public int numberInRow;
    public boolean isTaken;
    public boolean isChosen;


    public SeatBox(int chooseSeatButton, int rowNumber, int numberInRow, boolean isTaken, boolean isChosen) {
        this.chooseSeatButton = chooseSeatButton;
        this.rowNumber = rowNumber;
        this.numberInRow = numberInRow;
        this.isTaken = isTaken;
        this.isChosen = isChosen;
    }
}