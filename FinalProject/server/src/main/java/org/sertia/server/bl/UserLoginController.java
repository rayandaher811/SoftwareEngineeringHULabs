package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.LoginResult;
import org.sertia.contracts.user.login.UserRole;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserLoginController {
	private int sessionId;
	// session ID and user
	public Map<Integer, User> connectedUsers;
	public boolean isLoggedIn = false;

	public UserLoginController(){
		sessionId = 0;
		connectedUsers = new HashMap<Integer, User>();
	}

	public LoginResult login(LoginCredentials credentials){
		List<User> users = getAllUsers();
		LoginResult result = new LoginResult();

		for (User user : users) {
			if (user.getUsername().equals(credentials.username) &&
				user.getPassword().equals(credentials.password)){

				synchronized (this){
					result.sessionId = sessionId;
					connectedUsers.put(sessionId++, user);
				}

				// Parsing the DB Role to the contract role
				switch (user.getRole()){
					case MediaManager: result.userRole = UserRole.MediaManager;
					case BranchManager: result.userRole = UserRole.BranchManager;
					case CostumerSupport: result.userRole = UserRole.CostumerSupport;
					case CinemaManager: result.userRole = UserRole.CinemaManager;
					default: result.userRole = UserRole.None;
				}

				break;
			}
		}

		return result;
	}

	public void disconnect(int sessionId) {
		connectedUsers.remove(sessionId);
	}

	private List<User> getAllUsers() {
		List<User> users;
		Session session = HibernateSessionFactory.getInstance().openSession();

		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<User> query = builder.createQuery(User.class);
			query.from(User.class);
			users = session.createQuery(query).getResultList();
		} finally {
			session.close();
		}

		return users;
	}

}