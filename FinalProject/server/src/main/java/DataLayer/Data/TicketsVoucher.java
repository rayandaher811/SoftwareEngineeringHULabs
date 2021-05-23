package DataLayer.Data;

import javax.persistence.*;

@Entity
@Table(name="tickets_vouchers")
public class TicketsVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="voucher_id")
    private int id;

    private int ticketsBalance;

    @OneToOne
    private PaymentInfo paymentInfo;

    @ManyToOne
    private CostumerInfo costumerInfo;
}
