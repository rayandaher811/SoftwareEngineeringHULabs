package org.sertia.cinema;

public class CinemaSeat {
    private int row;
    private int col;
    private boolean isEmpty;

    public CinemaSeat(int row, int col) {
        this.row = row;
        this.col = col;
        this.isEmpty = true;
    }

    public void useSeat(){
        this.isEmpty = false;
    }
}
