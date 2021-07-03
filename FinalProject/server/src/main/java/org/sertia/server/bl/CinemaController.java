package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.server.SertiaException;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.Cinema;

import java.util.List;

public class CinemaController {
    public int getCinemaIdByManagerUsername(String managerUsername) throws SertiaException {
        try (Session session = HibernateSessionFactory.getInstance().openSession()){
            List<Cinema> cinemas = DbUtils.getAll(Cinema.class);

            for (Cinema cinema :
                    cinemas) {
                if(cinema.getManager().getUsername().equals(managerUsername))
                    return cinema.getId();
            }
        }

        throw new SertiaException("There are no cinemas under the management of the user " + managerUsername);
    }
}
