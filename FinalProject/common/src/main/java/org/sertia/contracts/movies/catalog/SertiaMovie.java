package org.sertia.contracts.movies.catalog;

import java.util.List;
import java.util.Objects;

public class SertiaMovie extends CinemaScreeningMovie  {
	public boolean isStreamable;
	public double extraDayPrice = 0;
	public boolean isComingSoon;

	public SertiaMovie() {
	}

	public SertiaMovie(ClientMovie movieDetails, List<ClientScreening> screenings,
					   double ticketPrice,
					   boolean isStreamable,
					   double streamingPrice) {
		super(movieDetails, screenings, ticketPrice);
		this.isStreamable = isStreamable;
		this.extraDayPrice = streamingPrice;
	}

	@Override
	public int hashCode() {
		return Objects.hash(movieId);
	}

	@Override
	public String toString() {
		return movieDetails.name;
	}
}