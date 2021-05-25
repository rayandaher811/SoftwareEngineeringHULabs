package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="streamings")
public class Streaming {
    @Id
    @Column(name = "movie_id")
    private int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String fullName;
    private String link;
    private double pricePerStream;
}
