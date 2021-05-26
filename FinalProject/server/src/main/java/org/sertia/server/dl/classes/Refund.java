package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="refunds")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="refund_id")
    private int id;

    private Date refundDate;

    private double price;

    @Enumerated(EnumType.STRING)
    private RefundReason refundReason;

    public Refund() {
    }
}
