package org.sertia.client.global;

import org.sertia.contracts.user.login.UserRole;

public class LoggedInUser {
    private UserRole userRole;
    private String managedCinema;

    private static LoggedInUser loggedInUser = null;

    private LoggedInUser(UserRole userRole, String managedCinema) {
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

    public String getManagedCinema() {
        return managedCinema;
    }

    public static void setConnectionStatus(UserRole role, String managedCinema) {
        if (loggedInUser == null)
            loggedInUser = new LoggedInUser(role, managedCinema);
    }

    public static void onDisconnection() {
        loggedInUser = null;
    }
}
