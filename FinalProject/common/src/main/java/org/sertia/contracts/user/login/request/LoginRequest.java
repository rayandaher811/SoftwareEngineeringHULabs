package org.sertia.contracts.user.login.request;

import org.sertia.contracts.SertiaBasicRequest;
import org.sertia.contracts.user.login.LoginCredentials;

public class LoginRequest extends SertiaBasicRequest {
    public LoginCredentials loginCredentials;
}
