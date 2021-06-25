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

    public int getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(int creditNumber) {
        this.creditNumber = creditNumber;
    }

    public Date getExperationDate() {
        return experationDate;
    }

    public void setExperationDate(Date experationDate) {
        this.experationDate = experationDate;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
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
