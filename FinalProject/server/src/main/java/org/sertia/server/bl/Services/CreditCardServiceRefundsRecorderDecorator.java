package org.sertia.server.bl.Services;

import org.hibernate.Session;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.CustomerPaymentDetails;
import org.sertia.server.dl.classes.Refund;
import org.sertia.server.dl.classes.RefundReason;

import java.time.LocalDateTime;

public class CreditCardServiceRefundsRecorderDecorator implements ICreditCardService{
    private ICreditCardService creditCardService;

    public CreditCardServiceRefundsRecorderDecorator(ICreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Override
    public void chargeCredit(CustomerPaymentDetails customerPaymentDetails, int amount) {
        creditCardService.chargeCredit(customerPaymentDetails, amount);
    }

    @Override
    public void refund(CustomerPaymentDetails customerPaymentDetails, double amount, RefundReason refundReason) {
        if(amount > 0) {
            creditCardService.refund(customerPaymentDetails, amount, refundReason);

            // Recording the refund in our DB in order to export reports properly
            try (Session session = HibernateSessionFactory.getInstance().openSession()) {
                session.save(new Refund(LocalDateTime.now(), amount, refundReason));
            }
        }
    }
}
