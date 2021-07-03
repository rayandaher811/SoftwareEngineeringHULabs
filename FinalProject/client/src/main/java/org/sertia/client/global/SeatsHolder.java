package org.sertia.client.global;

import java.util.List;

public class SeatsHolder {
    List<Integer> seatsIdsList;
    private static SeatsHolder instance = new SeatsHolder();
    public static SeatsHolder getInstance() {
        return instance;
    }

    public void setSeatsIdsList(List<Integer> userSelection) {
        seatsIdsList = userSelection;
    }

    public List<Integer> getUserSelection() {
        return seatsIdsList;
    }
}
