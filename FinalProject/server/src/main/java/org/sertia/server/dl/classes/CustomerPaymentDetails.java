package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@IdClass(PaymentDetailsId.class)
@Table(name = "customers_payment_details")
public class CustomerPaymentDetails {
    @Id
    @Column(name = "payer_id")
    private String payerId;

    @Id
    private String creditNumber;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String fullName;
    private LocalDateTime experationDate;
    private String cvv;
    private String email;
    private String phoneNumber;

    public CustomerPaymentDetails() {
    }

    public CustomerPaymentDetails(String payerId, String fullName, String creditNumber, LocalDateTime experationDate, String cvv, String email, String phoneNumber, PaymentMethod paymentMethod) {
        this.payerId = payerId;
        this.fullName = fullName;
        this.creditNumber = creditNumber;
        this.experationDate = experationDate;
        this.cvv = cvv;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.paymentMethod = paymentMethod;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }

    public LocalDateTime getExperationDate() {
        return experationDate;
    }

    public void setExperationDate(LocalDateTime experationDate) {
        this.experationDate = experationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
