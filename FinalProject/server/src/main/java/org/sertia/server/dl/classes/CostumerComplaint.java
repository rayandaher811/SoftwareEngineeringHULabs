package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="costumer_complaints")
public class CostumerComplaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="complaint_id")
    private int id;
    private Date openedDate;
    private Date closedDate;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @OneToOne
    private ScreeningTicket screeningTicket;

    @OneToOne
    private StreamingLink streamingLink;

    @OneToOne
    private TicketsVoucher ticketsVoucher;

    public CostumerComplaint() {
    }

    public CostumerComplaint(Date openedDate, Date closedDate, User handler) {
        this.openedDate = openedDate;
        this.closedDate = closedDate;
        this.handler = handler;
    }

    @ManyToOne
    private User handler;

}
