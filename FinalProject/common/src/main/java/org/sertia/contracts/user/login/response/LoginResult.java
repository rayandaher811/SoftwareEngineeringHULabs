package org.sertia.contracts.user.login.response;

import org.sertia.contracts.SertiaBasicResponse;
import org.sertia.contracts.user.login.UserRole;

import java.io.Serializable;

public class LoginResult  extends SertiaBasicResponse implements Serializable {

	public Integer sessionId;
	public UserRole userRole;
	public String managerCinema;

	public LoginResult() {
		super(false);
		sessionId = -1;
		userRole = UserRole.None;
	}
}