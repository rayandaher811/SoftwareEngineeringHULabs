package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.contracts.user.login.LoginCredentials;
import org.sertia.contracts.user.login.UserRole;
import org.sertia.contracts.user.login.response.LoginResult;
import org.sertia.server.SertiaException;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.Cinema;
import org.sertia.server.dl.classes.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserLoginController {
    private int sessionId;
    private final CinemaController cinemaController;
    // session ID and user
    public Map<Integer, User> connectedUsers;
    public boolean isLoggedIn = false;

    public UserLoginController(CinemaController cinemaController) {
        sessionId = 0;
        connectedUsers = new HashMap<Integer, User>();
        this.cinemaController = cinemaController;
    }

    public LoginResult login(LoginCredentials credentials) throws SertiaException {
        if (credentials.username == null || credentials.username.length() == 0 ||
                credentials.password == null || credentials.password.length() == 0)
            throw new SertiaException("חובה להזין שם משתמש וסיסמא");

        List<User> users = getAllUsers();
        LoginResult result = new LoginResult();

        for (User user : users) {
            if (user.getUsername().equals(credentials.username) &&
                    user.getPassword().equals(credentials.password)) {

                synchronized (this) {
                    result.sessionId = sessionId;
                    connectedUsers.put(sessionId++, user);
                }

                // Parsing the DB Role to the contract role
                switch (user.getRole()) {
                    case MediaManager:
                        result.userRole = UserRole.MediaManager;
                        break;
                    case BranchManager:
                        result.userRole = UserRole.BranchManager;
                        getManagedCinema(credentials.username)
                                .ifPresent(managedCinema -> result.managerCinema = managedCinema);
                        break;
                    case CostumerSupport:
                        result.userRole = UserRole.CostumerSupport;
                        break;
                    case CinemaManager:
                        result.userRole = UserRole.CinemaManager;
                        break;
                    default:
                        result.userRole = UserRole.None;
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

    public Optional<String> getManagedCinema(String username) {
        try {
            return DbUtils.getById(Cinema.class, cinemaController.getCinemaIdByManagerUsername(username))
                    .map(Cinema::getName);
        } catch (SertiaException e) {
            return Optional.empty();
        }
    }
}