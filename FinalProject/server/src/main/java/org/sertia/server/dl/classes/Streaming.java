package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="streaming_streaming_id")
    private List<StreamingLink> links;

    public Streaming() {
    }

    public Streaming(Movie movie, double pricePerStream) {
        this.movie = movie;
        this.pricePerStream = pricePerStream;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public double getPricePerStream() {
        return pricePerStream;
    }

    public void setPricePerStream(double pricePerStream) {
        this.pricePerStream = pricePerStream;
    }

    public List<StreamingLink> getLinks() {
        return links;
    }

    public void setLinks(List<StreamingLink> links) {
        this.links = links;
    }
}
