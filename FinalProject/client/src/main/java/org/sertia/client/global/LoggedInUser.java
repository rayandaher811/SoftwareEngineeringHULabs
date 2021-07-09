package org.sertia.client.global;

import org.sertia.contracts.user.login.UserRole;

import java.util.UUID;

public class LoggedInUser {
    // User
    private String userName;
    private String uuid;
    private UserRole userRole;

    public String getManagedCinema() {
        return managedCinema;
    }

    private String managedCinema;

    private static LoggedInUser loggedInUser = null;

    private LoggedInUser(String userName, UserRole userRole, String managedCinema) {
        this.userName = userName;
        this.uuid = UUID.randomUUID().toString();
        this.userRole = userRole;
        this.managedCinema = managedCinema;
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

    public static void setConnectionStatus(String userName, UserRole role, String managedCinema) {
        if (loggedInUser == null)
            loggedInUser = new LoggedInUser(userName, role, managedCinema);
    }

    public static void onDisconnection() {
        loggedInUser = null;
    }
}
