package org.sertia.contracts.user.login;

import java.io.Serializable;

public class LoginCredentials implements Serializable {
	public String username;
	public String password;

	public LoginCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
}