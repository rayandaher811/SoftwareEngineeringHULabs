package org.sertia.server.bl.Services;

import org.sertia.server.dl.classes.CustomerPaymentDetails;
import org.sertia.server.dl.classes.RefundReason;

public interface ICreditCardService {
    void chargeCredit(CustomerPaymentDetails customerPaymentDetails, int amount);

    void refund(CustomerPaymentDetails customerPaymentDetails, double amount, RefundReason refundReason);
}
