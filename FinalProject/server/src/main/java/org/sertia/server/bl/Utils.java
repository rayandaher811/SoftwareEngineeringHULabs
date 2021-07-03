package org.sertia.server.bl;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.price.change.ClientTicketType;
import org.sertia.contracts.screening.ticket.request.BasicPaymentRequest;
import org.sertia.server.SertiaException;
import org.sertia.server.dl.classes.CustomerPaymentDetails;
import org.sertia.server.dl.classes.PaymentMethod;
import org.sertia.server.dl.classes.TicketType;

import javax.transaction.NotSupportedException;

public final class Utils {
    private Utils() {
    }

    public static CustomerPaymentDetails getPaymentDetails(BasicPaymentRequest paymentRequest) {
        CustomerPaymentDetails paymentDetails = new CustomerPaymentDetails();
        paymentDetails.setPayerId(paymentRequest.cardHolderId);
        paymentDetails.setFullName(paymentRequest.cardHolderName);
        paymentDetails.setExperationDate(paymentRequest.expirationDate);
        paymentDetails.setPaymentMethod(PaymentMethod.Credit);
        paymentDetails.setCreditNumber(paymentRequest.creditCardNumber);
        paymentDetails.setCvv(paymentRequest.cvv);
        paymentDetails.setEmail(paymentRequest.cardHolderEmail);
        paymentDetails.setPhoneNumber(paymentRequest.cardHolderPhone);

        return paymentDetails;
    }

    public static <T extends SertiaBasicResponse> T createFailureResponse(T baseResponse, String failReason) {
        baseResponse.isSuccessful = false;
        baseResponse.failReason = failReason;
        return baseResponse;
    }

    public static ClientTicketType dlTicketTypeToClient(TicketType ticketType) throws SertiaException {
        switch (ticketType) {
            case Streaming: return ClientTicketType.Streaming;
            case Screening: return ClientTicketType.Screening;
            case Voucher: return ClientTicketType.Voucher;
            default: throw new SertiaException("There are no such ticket type");
        }
    }

    public static TicketType clientTicketTypeToDL(ClientTicketType ticketType) throws SertiaException {
        switch (ticketType) {
            case Streaming: return TicketType.Streaming;
            case Screening: return TicketType.Screening;
            case Voucher: return TicketType.Voucher;
            default: throw new SertiaException("There are no such ticket type");
        }
    }
}
