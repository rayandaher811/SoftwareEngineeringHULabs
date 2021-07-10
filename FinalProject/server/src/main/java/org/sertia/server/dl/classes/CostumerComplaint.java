package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="costumer_complaints")
public class CostumerComplaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="complaint_id")
    private int id;
    private LocalDateTime openedDate;
    private LocalDateTime closedDate;
    private String description;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @OneToOne
    @JoinColumn(nullable=true)
    private ScreeningTicket screeningTicket;

    @OneToOne
    @JoinColumn(nullable=true)
    private StreamingLink streamingLink;

    @ManyToOne
    private User handler;

    public CostumerComplaint() {
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDateTime openedDate) {
        this.openedDate = openedDate;
    }

    public LocalDateTime getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDateTime closedDate) {
        this.closedDate = closedDate;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public ScreeningTicket getScreeningTicket() {
        return screeningTicket;
    }

    public void setScreeningTicket(ScreeningTicket screeningTicket) {
        this.screeningTicket = screeningTicket;
    }

    public StreamingLink getStreamingLink() {
        return streamingLink;
    }

    public void setStreamingLink(StreamingLink streamingLink) {
        this.streamingLink = streamingLink;
    }

    public User getHandler() {
        return handler;
    }

    public void setHandler(User handler) {
        this.handler = handler;
    }
}
