package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="tickets_vouchers")
public class TicketsVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="voucher_id")
    private int id;

    private int ticketsBalance;
    private LocalDateTime purchaseDate;

    @ManyToOne
    private CustomerPaymentDetails customerPaymentDetails;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_voucher_id")
    private Set<ScreeningTicket> tickets;

    public TicketsVoucher() {
    }

    public int getId() {
        return id;
    }

    public int getTicketsBalance() {
        return ticketsBalance;
    }

    public void setTicketsBalance(int ticketsBalance) {
        this.ticketsBalance = ticketsBalance;
    }

    public CustomerPaymentDetails getCustomerPaymentDetails() {
        return customerPaymentDetails;
    }

    public void setCustomerPaymentDetails(CustomerPaymentDetails customerPaymentDetails) {
        this.customerPaymentDetails = customerPaymentDetails;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
