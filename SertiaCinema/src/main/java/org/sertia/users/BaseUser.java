package org.sertia.users;

public abstract class BaseUser {
    private String userName;
    private String password;
    private Role role;
    private boolean isLoggedIn;

    protected BaseUser(String userName, String password, Role role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.isLoggedIn = true;
    }
}
