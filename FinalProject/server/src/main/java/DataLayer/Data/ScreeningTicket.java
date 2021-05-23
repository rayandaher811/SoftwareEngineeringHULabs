package DataLayer.Data;

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
    private CostumerInfo costumerInfo;
    @ManyToOne
    private HallSeat seat;
    @OneToOne
    private PaymentInfo paymentInfo;
    @ManyToOne
    private Screening screening;
}
