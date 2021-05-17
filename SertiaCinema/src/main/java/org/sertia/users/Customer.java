package org.sertia.users;

public class Customer extends BaseUser {
    protected Customer(String userName, String password) {
        super(userName, password, Role.CUSTOMER);
    }
}
