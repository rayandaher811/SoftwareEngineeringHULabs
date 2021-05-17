package org.sertia.users;

public class NetworkManager extends BaseUser{
    public NetworkManager(String userName, String password) {
        super(userName, password, Role.NETWORK_MANAGER);
    }
}
