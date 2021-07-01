package org.sertia.server.dl;

import org.hibernate.Session;
import org.sertia.server.dl.classes.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class DbUtils {
    public static <T> List<T> getAll(Class<T> clazz) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(clazz);
            query.from(clazz);
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static <T> Optional<T> getById(Class<T> clazz, int id) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            return Optional.ofNullable(session.get(clazz, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public static User getUserByUsername(String username) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            return session.byNaturalId(User.class).using(User.usernameFieldName, username).load();
        } catch (Exception e) {
            return null;
        }
    }
}
