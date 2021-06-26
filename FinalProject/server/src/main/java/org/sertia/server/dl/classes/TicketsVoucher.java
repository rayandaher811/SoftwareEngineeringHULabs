package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="tickets_vouchers")
public class TicketsVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="voucher_id")
    private int id;

    private int ticketsBalance;

    @ManyToOne
    private CustomerPaymentDetails customerPaymentDetails;

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
}
