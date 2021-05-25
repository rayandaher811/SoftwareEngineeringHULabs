package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="payment_infos")
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="payment_id")
    private int id;
    private String fullName;
    private int creditNumber;
    private Date experationDate;
    private int cvv;
    private int payerId;

    public PaymentInfo() {
    }

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}
