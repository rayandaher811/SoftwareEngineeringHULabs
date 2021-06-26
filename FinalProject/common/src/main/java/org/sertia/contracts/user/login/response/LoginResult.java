package org.sertia.contracts.user.login.response;

import org.sertia.contracts.user.login.UserRole;

public class LoginResult {

	public Integer sessionId;
	public UserRole userRole;

	public LoginResult() {
		sessionId = -1;
		userRole = UserRole.None;
	}
}