package org.sertia.client;

/**
 * Will be replaced by user after DB design is over
 */
public class ActiveUserData {
    private String userName;
    private String role;

    public ActiveUserData(String userName, String role) {
        this.userName = userName;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }
}
