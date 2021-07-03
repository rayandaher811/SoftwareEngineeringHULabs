package org.sertia.server.bl.Services;

import org.hibernate.Session;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.server.dl.HibernateSessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Reportable {

    public abstract List<ClientReport> createSertiaReports();

    public abstract List<ClientReport> createCinemaReports(int cinemaId);

    protected <T> List<T> getDataOfThisMonth(Class<T> dataClass, String dateField) {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = builder.createQuery(dataClass);
            Root<T> complaintRoot = criteriaQuery.from(dataClass);
            criteriaQuery.select(complaintRoot);
            criteriaQuery.where(builder.between(complaintRoot.get(dateField), firstDayOfMonth(), lastDayOfMonth()));
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    protected LocalDateTime firstDayOfMonth() {
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(
                now.getYear(),
                now.getMonth(),
                1,
                0,
                0);
    }

    protected LocalDateTime lastDayOfMonth() {
        LocalDateTime firstDayOfMonth = firstDayOfMonth();
        return firstDayOfMonth.withDayOfMonth(firstDayOfMonth.toLocalDate().lengthOfMonth());
    }
}