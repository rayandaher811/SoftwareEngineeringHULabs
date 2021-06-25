package org.sertia.contracts.screening.ticket;

public class HallSeat {
	public int id;
	public int row;
	public int numberInRow;
	public boolean isTaken;

	public HallSeat(int id) {
		this.id = id;
	}
}