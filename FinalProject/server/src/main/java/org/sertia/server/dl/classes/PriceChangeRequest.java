package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="price_change_requests")
public class PriceChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="request_id")
    private int id;

    @Enumerated(EnumType.STRING)
    private StreamingType streamingType;

    @ManyToOne
    private User requester;

    @ManyToOne
    private User handler;

    @ManyToOne
    private Movie movie;

    private boolean accepted;

    public PriceChangeRequest() {
    }

    public PriceChangeRequest(StreamingType streamingType, User requester, User handler, Movie movie, boolean accepted) {
        this.streamingType = streamingType;
        this.requester = requester;
        this.handler = handler;
        this.movie = movie;
        this.accepted = accepted;
    }
}

