package org.sertia.client.global;

import java.util.UUID;

public class LoggedInUser {
    // User
    private String userName;
    private String uuid;

    private static LoggedInUser loggedInUser = null;

    private LoggedInUser(String userName){
        this.userName = userName;
        this.uuid = UUID.randomUUID().toString();
    }

    public static LoggedInUser getInstance(){
        if (loggedInUser == null) {
            System.out.println("Error.. user wasn't created yet");
        }

        return loggedInUser;
    }

    public String getUuid() {
        return uuid;
    }

    public static void setConnectionStatus(String userName) {
        if (loggedInUser == null)
            loggedInUser = new LoggedInUser(userName);
    }

    public static void onDisconnection(){
        loggedInUser = null;
    }
}
