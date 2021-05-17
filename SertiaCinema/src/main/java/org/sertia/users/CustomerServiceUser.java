package org.sertia.users;

public class CustomerServiceUser extends BaseUser {
    public CustomerServiceUser(String userName, String password) {
        super(userName, password, Role.CUSTOMER_SERVICE);
    }
}
