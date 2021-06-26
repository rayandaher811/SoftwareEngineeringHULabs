package org.sertia.client.controllers;

import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.UserRole;
import org.sertia.contracts.user.login.request.LoginRequest;
import org.sertia.contracts.user.login.response.LoginResult;

public class ClientUserLoginController extends ClientControl {

    public UserRole login(LoginCredentials credentials) {
        LoginResult response = client.request(new LoginRequest(credentials), LoginResult.class);
        return response.userRole;
    }
}