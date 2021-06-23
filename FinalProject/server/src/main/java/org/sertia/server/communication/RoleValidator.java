package org.sertia.server.communication;

import org.sertia.contracts.user.login.UserRole;
import org.sertia.server.dl.classes.Role;

public class RoleValidator {

	public boolean isClientAllowed(UserRole role, String requestType) {
		switch (requestType){
			case RequestType.ALL_MOVIES_REQ:
				return true;
			case RequestType.UPDATE_SCREENING_REQ:
				return role == UserRole.MediaManager;
			case RequestType.LOGIN_REQ:
				return true;
			case RequestType.ADD_MOVIE:
				return role == UserRole.MediaManager;
			case RequestType.REMOVE_MOVIE:
				return role == UserRole.MediaManager;
		}
	}

}