package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="streamings")
public class Streaming {
    @Id
    @Column(name = "movie_id")
    public int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "movie_id")
    public Movie movie;

    public double pricePerStream;

    public Streaming() {
    }

    public Streaming(Movie movie, double pricePerStream) {
        this.movie = movie;
        this.pricePerStream = pricePerStream;
    }
}
