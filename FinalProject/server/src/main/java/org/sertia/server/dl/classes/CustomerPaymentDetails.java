package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers_payment_details")
public class CustomerPaymentDetails {
    @Id
    @Column(name = "payer_id")
    private String payerId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String fullName;
    private int creditNumber;
    private Date experationDate;
    private int cvv;
    private String email;
    private String phoneNumber;

    public CustomerPaymentDetails(String payerId, String fullName, int creditNumber, Date experationDate, int cvv, String email, String phoneNumber, PaymentMethod paymentMethod) {
        this.payerId = payerId;
        this.fullName = fullName;
        this.creditNumber = creditNumber;
        this.experationDate = experationDate;
        this.cvv = cvv;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.paymentMethod = paymentMethod;
    }
}
