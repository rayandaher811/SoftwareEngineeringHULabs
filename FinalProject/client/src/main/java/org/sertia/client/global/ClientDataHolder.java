package org.sertia.client.global;

public class ClientDataHolder {
    private static final ClientDataHolder instance = new ClientDataHolder();
    private String clientName;
    private String email;
    private String phone;
    private boolean isInitialized;

    public static ClientDataHolder getInstance() {
        return instance;
    }

    public void setClientData(String clientName, String email, String phone) {
        this.clientName = clientName;
        this.email = email;
        this.phone = phone;
        this.isInitialized = true;
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
}
