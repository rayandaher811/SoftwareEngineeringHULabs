package org.sertia.contracts.user.login;

public class LoginResult {

	public Integer sessionId;
	public UserRole userRole;

	public LoginResult() {
		sessionId = -1;
		userRole = UserRole.None;
	}
}