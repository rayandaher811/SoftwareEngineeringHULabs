package org.sertia.client.global;

public class NumberOfTicketsHolder {
    private int numberOfTickets = 0;

    private final static NumberOfTicketsHolder instance = new NumberOfTicketsHolder();

    public static NumberOfTicketsHolder getInstance() {
        return instance;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }
}
