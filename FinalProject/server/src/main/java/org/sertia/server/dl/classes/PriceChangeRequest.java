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
    private TicketType ticketType;

    @ManyToOne
    private User requester;

    @ManyToOne
    private User handler;

    @ManyToOne
    private Movie movie;

    private boolean accepted;

    private double newPrice;

    public PriceChangeRequest() {
    }

    public int getId() {
        return id;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getHandler() {
        return handler;
    }

    public void setHandler(User handler) {
        this.handler = handler;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }
}

