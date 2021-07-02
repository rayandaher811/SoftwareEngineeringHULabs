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

    public double extraDayPrice;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="streaming_streaming_id")
    private List<StreamingLink> links;

    public Streaming() {
    }

    public Streaming(Movie movie, double extraDayPrice) {
        this.movie = movie;
        this.extraDayPrice = extraDayPrice;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public double getExtraDayPrice() {
        return extraDayPrice;
    }

    public void setExtraDayPrice(double extraDayPrice) {
        this.extraDayPrice = extraDayPrice;
    }

    public List<StreamingLink> getLinks() {
        return links;
    }

    public void setLinks(List<StreamingLink> links) {
        this.links = links;
    }
}
