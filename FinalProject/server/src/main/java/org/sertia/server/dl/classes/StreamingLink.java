package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="streaming_links")
public class StreamingLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ticket_id")
    private int id;

    private LocalDateTime activationStart;

    private LocalDateTime activationEnd;

    private double paidPrice;

    private String link;

    private LocalDateTime purchaseDate;

    @ManyToOne
    @JoinColumn(name="streaming_streaming_id", nullable=false)
    private Streaming movie;

    @ManyToOne
    private CustomerPaymentDetails customerPaymentDetails;

    public StreamingLink() {
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getActivationStart() {
        return activationStart;
    }

    public void setActivationStart(LocalDateTime activationStart) {
        this.activationStart = activationStart;
    }

    public LocalDateTime getActivationEnd() {
        return activationEnd;
    }

    public void setActivationEnd(LocalDateTime activationEnd) {
        this.activationEnd = activationEnd;
    }

    public double getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(double paidPrice) {
        this.paidPrice = paidPrice;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Streaming getMovie() {
        return movie;
    }

    public void setMovie(Streaming movie) {
        this.movie = movie;
    }

    public CustomerPaymentDetails getCustomerPaymentDetails() {
        return customerPaymentDetails;
    }

    public void setCustomerPaymentDetails(CustomerPaymentDetails customerPaymentDetails) {
        this.customerPaymentDetails = customerPaymentDetails;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}

