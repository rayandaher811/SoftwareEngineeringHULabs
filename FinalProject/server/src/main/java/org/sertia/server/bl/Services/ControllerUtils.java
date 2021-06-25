package org.sertia.server.bl.Services;

import org.sertia.contracts.price.change.ClientTicketType;
import org.sertia.server.dl.classes.TicketType;

import javax.transaction.NotSupportedException;

public class ControllerUtils {

    public static ClientTicketType dlTicketTypeToClient(TicketType ticketType) throws NotSupportedException {
        switch (ticketType) {
            case Streaming: return ClientTicketType.Streaming;
            case Screening: return ClientTicketType.Screening;
            case Voucher: return ClientTicketType.Voucher;
            default: throw new NotSupportedException("There are no such DL ticket type");
        }
    }

    public static TicketType clientTicketTypeToDL(ClientTicketType ticketType) throws NotSupportedException{
        switch (ticketType) {
            case Streaming: return TicketType.Streaming;
            case Screening: return TicketType.Screening;
            case Voucher: return TicketType.Voucher;
            default: throw new NotSupportedException("There are no such client ticket type");
        }
    }
}
