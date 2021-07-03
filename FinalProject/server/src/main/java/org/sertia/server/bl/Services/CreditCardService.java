package org.sertia.server.bl.Services;


import org.sertia.server.dl.classes.CustomerPaymentDetails;

public class CreditCardService {

    ICustomerNotifier notifier;

    public CreditCardService() {
        this.notifier = CustomerNotifier.getInstance();
    }

    public void chargeCredit(CustomerPaymentDetails customerPaymentDetails, int amount) {
        notifier.notify(customerPaymentDetails.getEmail(), "You have been charged by sertia cinema with " + amount + " shekels.");
    }

    public void refund(CustomerPaymentDetails customerPaymentDetails, double amount) {
        notifier.notify(customerPaymentDetails.getEmail(), "You have been refunded by sertia cinema in " + amount + " shekels.");
    }

}