package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="screening_tickets")
public class ScreeningTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ticket_id")
    private int id;

    private double paidPrice;
    private boolean isVoucher;
    @ManyToOne
    private CustomerPaymentDetails customerPaymentDetails;
    @ManyToOne
    private HallSeat seat;
    @ManyToOne
    @JoinColumn(name="screening_screening_id", nullable=false)
    private Screening screening;

    public ScreeningTicket() {
    }

    public double getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(double paidPrice) {
        this.paidPrice = paidPrice;
    }

    public boolean isVoucher() {
        return isVoucher;
    }

    public void setVoucher(boolean voucher) {
        isVoucher = voucher;
    }

    public HallSeat getSeat() {
        return seat;
    }

    public void setSeat(HallSeat seat) {
        this.seat = seat;
    }

    public CustomerPaymentDetails getPaymentInfo() {
        return customerPaymentDetails;
    }

    public void setPaymentInfo(CustomerPaymentDetails customerPaymentDetails) {
        this.customerPaymentDetails = customerPaymentDetails;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }
}
