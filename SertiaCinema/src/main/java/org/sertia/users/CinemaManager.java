package org.sertia.users;

public class CinemaManager extends BaseUser{
    public CinemaManager(String userName, String password) {
        super(userName, password, Role.CINEMA_MANAGER);
    }
}
