package org.sertia.contracts.screening.ticket.request;

public enum PaymentMethod {
    CREDIT_CARD("כרטיס אשראי"),
    PREPAID_TICKET("כרטיסיה");

    public final String label;
    PaymentMethod(String label) {
        this.label = label;
    }
}
