package org.sertia.client.controllers;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.request.LoginRequest;
import org.sertia.contracts.user.login.request.LogoutRequest;
import org.sertia.contracts.user.login.response.LoginResult;

public class ClientUserLoginController extends ClientControl {

    private static final ClientUserLoginController instance = new ClientUserLoginController();

    public static ClientUserLoginController getInstance() {
        return instance;
    }

    public LoginResult login(LoginCredentials credentials) {
        return client.request(new LoginRequest(credentials), LoginResult.class);
    }

    public SertiaBasicResponse logout() {
        return client.request(new LogoutRequest(), SertiaBasicResponse.class);
    }
}