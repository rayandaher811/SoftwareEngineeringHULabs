package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="screening_tickets")
public class ScreeningTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ticket_id")
    private int id;

    private double paidPrice;
    private LocalDateTime purchaseDate;

    private boolean isVoucher;

    @ManyToOne
    @JoinColumn(name="voucher_voucher_id", nullable=true)
    private TicketsVoucher voucher;

    @ManyToOne
    private CustomerPaymentDetails customerPaymentDetails;
    @ManyToOne
    private HallSeat seat;
    @ManyToOne
    @JoinColumn(name="screening_screening_id", nullable=true)
    private Screening screening;

    public ScreeningTicket() {
    }

    public int getId() {
        return id;
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

    public void setIsVoucher(boolean voucher) {
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

    public void setIsVoucher(TicketsVoucher voucher) {
        this.voucher = voucher;
    }

    public void setVoucher(TicketsVoucher voucher) {
        this.voucher = voucher;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }
    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public ScreeningTicket setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }
}
