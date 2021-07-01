package org.sertia.server.bl;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.screening.ticket.request.BasicPaymentRequest;
import org.sertia.server.dl.classes.CustomerPaymentDetails;
import org.sertia.server.dl.classes.PaymentMethod;

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
}
