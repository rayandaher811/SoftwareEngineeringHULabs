package org.sertia.client.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sertia.contracts.price.change.ClientTicketType;

public class TicketType {
    public final static TicketType SCREENING = new TicketType(ClientTicketType.Screening, "הקרנה");
    public final static TicketType STREAMING = new TicketType(ClientTicketType.Streaming, "חבילת צפייה");
    public final static TicketType VOUCHER = new TicketType(ClientTicketType.Voucher, "כרטיסיה");

    private final static ObservableList<TicketType> types = FXCollections.observableArrayList(
            SCREENING,
            STREAMING,
            VOUCHER
    );

    public ClientTicketType ticketType;
    public String displayName;

    public TicketType(ClientTicketType ticketType, String displayName) {
        this.ticketType = ticketType;
        this.displayName = displayName;
    }

    public static ObservableList<TicketType> getTypes() {
        return FXCollections.observableArrayList(types);
    }

    public static TicketType getFromClientType(ClientTicketType clientTicketType) {
        TicketType ticketType;
        switch (clientTicketType) {
            case Screening:
                ticketType = SCREENING;
                break;
            case Streaming:
                ticketType = STREAMING;
                break;
            case Voucher:
                ticketType = VOUCHER;
                break;
            default:
                ticketType = null;
        };
        return ticketType;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
