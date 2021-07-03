package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="refunds")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="refund_id")
    private int id;

    private LocalDateTime refundDate;

    private double price;

    @Enumerated(EnumType.STRING)
    private RefundReason refundReason;

    public Refund() {
    }

    public Refund(LocalDateTime refundDate, double price, RefundReason refundReason) {
        this.refundDate = refundDate;
        this.price = price;
        this.refundReason = refundReason;
    }
}
