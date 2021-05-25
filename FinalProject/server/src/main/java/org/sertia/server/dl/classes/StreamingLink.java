package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="streaming_links")
public class StreamingLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ticket_id")
    private int id;

    private Date activationStart;

    private Date activationEnd;

    private double paidPrice;

    private String link;

    @ManyToOne
    private Streaming movie;

    @OneToOne
    private PaymentInfo paymentInfo;

    @ManyToOne
    private CostumerInfo costumer;

    public StreamingLink() {
    }
}

