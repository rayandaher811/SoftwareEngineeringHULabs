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

    @ManyToOne
    private User handler;

}
