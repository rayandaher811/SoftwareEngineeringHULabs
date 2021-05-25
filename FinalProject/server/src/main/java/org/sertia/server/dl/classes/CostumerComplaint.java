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

    @ManyToOne
    private CostumerInfo costumerInfo;

    public CostumerComplaint() {
    }

    public CostumerComplaint(Date openedDate, Date closedDate, CostumerInfo costumerInfo, User handler) {
        this.openedDate = openedDate;
        this.closedDate = closedDate;
        this.costumerInfo = costumerInfo;
        this.handler = handler;
    }

    @ManyToOne
    private User handler;

}
