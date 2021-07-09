package org.sertia.client.global;

import org.sertia.contracts.user.login.UserRole;

import java.util.UUID;

public class LoggedInUser {
    // User
    private String userName;
    private String uuid;
    private UserRole userRole;

    private static LoggedInUser loggedInUser = null;

    private LoggedInUser(String userName, UserRole userRole) {
        this.userName = userName;
        this.uuid = UUID.randomUUID().toString();
        this.userRole = userRole;
    }

    public static LoggedInUser getInstance() {
        if (loggedInUser == null) {
            System.out.println("Error.. user wasn't created yet");
        }

        return loggedInUser;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getUuid() {
        return uuid;
    }

    public static void setConnectionStatus(String userName, UserRole role) {
        if (loggedInUser == null)
            loggedInUser = new LoggedInUser(userName, role);
    }

    public static void onDisconnection() {
        loggedInUser = null;
    }
}
