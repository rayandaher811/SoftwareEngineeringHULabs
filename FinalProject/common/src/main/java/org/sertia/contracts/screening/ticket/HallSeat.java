package org.sertia.contracts.screening.ticket;

import java.io.Serializable;

public class HallSeat  implements Serializable {
	public int id;
	public int row;
	public int numberInRow;
	public boolean isTaken;

	public HallSeat(int id) {
		this.id = id;
	}
}