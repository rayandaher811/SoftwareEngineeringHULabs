package org.sertia.server.dl.classes;

import java.io.Serializable;
import java.util.Objects;

public class PaymentDetailsId implements Serializable {

    private String payerId;
    private String creditNumber;

    public PaymentDetailsId() {
    }

    public PaymentDetailsId(String payerId, String creditNumber) {
        this.payerId = payerId;
        this.creditNumber = creditNumber;
    }

    public String getPayerId() {
        return payerId;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDetailsId that = (PaymentDetailsId) o;
        return Objects.equals(payerId, that.payerId) && Objects.equals(creditNumber, that.creditNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payerId, creditNumber);
    }
}
