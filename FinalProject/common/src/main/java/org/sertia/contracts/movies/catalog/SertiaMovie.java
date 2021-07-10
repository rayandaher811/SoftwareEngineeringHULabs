package org.sertia.contracts.movies.catalog;

import java.util.List;

public class SertiaMovie extends CinemaScreeningMovie  {
	public boolean isStreamable;
	public double extraDayPrice = 0;
	public boolean isComingSoon;

	public SertiaMovie() {
	}

	public SertiaMovie(ClientMovie movieDetails, List<ClientScreening> screenings,
					   double ticketPrice,
					   boolean isStreamable,
					   boolean isComingSoon) {
		super(movieDetails, screenings, ticketPrice);
		this.isStreamable = isStreamable;
		this.isComingSoon = isComingSoon;
	}

	@Override
	public String toString() {
		return movieDetails.name;
	}
}