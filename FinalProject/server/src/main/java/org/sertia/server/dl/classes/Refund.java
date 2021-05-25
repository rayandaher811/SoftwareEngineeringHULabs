package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="refunds")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="refund_id")
    private int id;

    private double price;

    @OneToOne
    private CostumerComplaint complaint;

    @Enumerated(EnumType.STRING)
    private RefundReason reason;

    public Refund() {
    }
}
