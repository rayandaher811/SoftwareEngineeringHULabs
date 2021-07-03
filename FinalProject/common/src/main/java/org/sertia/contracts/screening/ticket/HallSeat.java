package org.sertia.contracts.screening.ticket;

import java.io.Serializable;

public class HallSeat  implements Serializable {
	public int getId() {
		return id;
	}

	public int id;
	public int row;
	public int numberInRow;
	public boolean isTaken;

	public HallSeat(int id) {
		this.id = id;
	}

	public int getRow() {
		return row;
	}

	public int getNumberInRow() {
		return numberInRow;
	}
}