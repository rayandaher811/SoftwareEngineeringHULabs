package org.sertia.server.bl;

import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.server.dl.classes.Role;
import org.sertia.server.dl.classes.User;

import java.util.Map;

public class UserLoginController {

	// session ID and user
	public Map<Integer, User> connectedUsers;
	public boolean isLoggedIn = false;
	public Role role;

	/**
	 * 
	 * @param credentials
	 */
	public void login(LoginCredentials credentials) {
		// TODO - implement UserLoginController.login
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param sessionId
	 */
	public void disconnect(String sessionId) {
		// TODO - implement UserLoginController.disconnect
		throw new UnsupportedOperationException();
	}

}