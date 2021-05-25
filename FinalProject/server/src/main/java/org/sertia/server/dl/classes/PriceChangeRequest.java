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

}

