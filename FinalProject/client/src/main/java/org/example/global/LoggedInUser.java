package org.example.global;

public class LoggedInUser {
    // User
    private String userName;

    private static LoggedInUser loggedInUser = null;

    private LoggedInUser(String userName){
        this.userName = userName;
    }

    public static LoggedInUser getInstance(){
        if (loggedInUser == null) {
            System.out.println("Error.. user wasn't created yet");
        }

        return loggedInUser;
    }

    public static void setConnectionStatus(String userName) {
        if (loggedInUser == null)
            loggedInUser = new LoggedInUser(userName);
    }

    public static void onDisconnection(){
        loggedInUser = null;
    }
}
