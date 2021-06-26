package org.sertia.contracts.user.login.response;

import org.sertia.contracts.user.login.UserRole;

import java.io.Serializable;

public class LoginResult  implements Serializable {

	public Integer sessionId;
	public UserRole userRole;

	public LoginResult() {
		sessionId = -1;
		userRole = UserRole.None;
	}
}