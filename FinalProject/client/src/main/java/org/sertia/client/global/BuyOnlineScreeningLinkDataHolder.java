package org.sertia.client.global;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BuyOnlineScreeningLinkDataHolder {
    private static final BuyOnlineScreeningLinkDataHolder instance = new BuyOnlineScreeningLinkDataHolder();
    private String clientName;
    private String email;
    private String phone;
    private boolean isInitialized;
    private int numberOfDaysForRental;
    LocalDateTime startDateTime;

    public static BuyOnlineScreeningLinkDataHolder getInstance() {
        return instance;
    }

    public void clear(){
        this.clientName = null;
        this.email = null;
        this.phone = null;
        this.isInitialized = false;
        this.numberOfDaysForRental = -1;
        this.startDateTime = null;
    }

    public void setClientData(String clientName, String email, String phone, int numberOfDaysForRental, LocalDateTime startDateTime) {
        this.clientName = clientName;
        this.email = email;
        this.phone = phone;
        this.isInitialized = true;
        this.numberOfDaysForRental = numberOfDaysForRental;
        this.startDateTime = startDateTime;
    }

    public String getClientName() {
        return clientName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public int getNumberOfDaysForRental() {
        return numberOfDaysForRental;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
}
