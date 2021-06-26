package org.sertia.contracts.user.login.request;

import org.sertia.contracts.SertiaClientRequest;
import org.sertia.contracts.user.login.LoginCredentials;

public class LoginRequest extends SertiaClientRequest {
    public LoginCredentials loginCredentials;
}
