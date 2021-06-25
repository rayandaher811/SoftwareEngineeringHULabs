package org.sertia.server.communication;

import org.sertia.contracts.user.login.UserRole;
import org.sertia.server.dl.classes.Role;

public class RoleValidator {

	public boolean isClientAllowed(UserRole role, String requestType) {
		switch (requestType){
			case RequestType.ALL_MOVIES_REQ:
			case RequestType.LOGIN_REQ:
				return true;
			case RequestType.UPDATE_SCREENING_TIME_REQ:
			case RequestType.ADD_MOVIE:
			case RequestType.REMOVE_MOVIE:
			case RequestType.ADD_SCREENINGS:
			case RequestType.REMOVE_SCREENINGS:
			case RequestType.ADD_STREAMING:
			case RequestType.REMOVE_STREAMING:
			case RequestType.REQUEST_PRICE_CHANGE:
				return role == UserRole.MediaManager;
			case RequestType.ALL_UNAPPROVED_REQUESTS:
			case RequestType.APPROVE_PRICE_CHANGE:
			case RequestType.DISAPPROVE_PRICE_CHANGE:
				return role == UserRole.BranchManager;
			default:
				return false;
		}
	}

}