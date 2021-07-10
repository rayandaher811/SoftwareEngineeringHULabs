package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.contracts.covidRegulations.responses.ClientCovidRegulationsStatus;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.server.SertiaException;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.CovidRegulationsInfo;
import org.sertia.server.dl.classes.Refund;
import org.sertia.server.dl.classes.RefundReason;
import org.sertia.server.dl.classes.Screening;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CovidRegulationsController extends Reportable {
    private final MoviesCatalogController moviesCatalogController;

    public CovidRegulationsController(MoviesCatalogController moviesCatalogController) {
        this.moviesCatalogController = moviesCatalogController;
        List<CovidRegulationsInfo> covidRegulationsInfos = DbUtils.getAll(CovidRegulationsInfo.class);

        // Initializing the Covid Regulations Info table if its not initialized
        if (covidRegulationsInfos.size() == 0) {
            try (Session session = HibernateSessionFactory.getInstance().openSession()) {
                CovidRegulationsInfo covidRegulationsInfo = new CovidRegulationsInfo();
                covidRegulationsInfo.setId(CovidRegulationsInfo.singleRecordId);

                // Dummy value that represents that there are no limitation
                covidRegulationsInfo.setMaxNumberOfPeople(500000);
                covidRegulationsInfo.setActive(false);

                session.beginTransaction();
                session.save(covidRegulationsInfo);
                session.flush();
                session.clear();
                session.getTransaction().commit();
            } finally {

            }
        }
    }

    public void updateCovidCrowdingRegulations(int maxNumberOfPeople) throws SertiaException {
        if (maxNumberOfPeople <= 0)
            throw new SertiaException("מספר אנשים מקסימלי חייב להיות יותר מ-0");
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();

            CovidRegulationsInfo covidRegulationsInfo = getCovidRegulationsInfo(session);

            // Updating
            covidRegulationsInfo.setMaxNumberOfPeople(maxNumberOfPeople);

            session.beginTransaction();
            session.update(covidRegulationsInfo);
            session.flush();
            session.clear();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void cancelCovidRegulations() {
        changeCovidRegulationActivation(false);
    }

    public void activeCovidRegulations() {
        changeCovidRegulationActivation(true);
    }

    public ClientCovidRegulationsStatus getCovidRegulationsStatus() {
        try (Session session = HibernateSessionFactory.getInstance().openSession()) {
            CovidRegulationsInfo covidRegulationsInfo = getCovidRegulationsInfo(session);
            return new ClientCovidRegulationsStatus(covidRegulationsInfo.getMaxNumberOfPeople(),
                    covidRegulationsInfo.isActive());
        } finally {
        }
    }

    public void cancelAllScreeningsDueCovid(LocalDateTime startDate, LocalDateTime endDate) throws SertiaException {
        if(startDate.isBefore(LocalDateTime.now()))
            throw new SertiaException("לא ניתן לבטל הקרנות שכבר הסתיימו");
        if (startDate.isAfter(endDate))
            throw new SertiaException("טווח הזמנים שנבחר אינו תקין");

        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();

            List<Screening> screenings = DbUtils.getAll(Screening.class);

            session.beginTransaction();

            for (Screening screening : screenings) {
                // Canceling all screenings in the inserted DateTime range
                if (screening.getScreeningTime().isBefore(endDate) &&
                        screening.getScreeningTime().isAfter(startDate))
                    moviesCatalogController.removeMovieScreeningDueCovid(screening.getId());
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    private CovidRegulationsInfo getCovidRegulationsInfo(Session session) {
        return session.get(CovidRegulationsInfo.class, CovidRegulationsInfo.singleRecordId);
    }

    private void changeCovidRegulationActivation(boolean regulationsIsActive) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getInstance().openSession();

            CovidRegulationsInfo covidRegulationsInfo = getCovidRegulationsInfo(session);

            // Updating
            covidRegulationsInfo.setActive(regulationsIsActive);

            session.beginTransaction();
            session.update(covidRegulationsInfo);
            session.flush();
            session.clear();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<ClientReport> createSertiaReports() {
        ClientReport report = new ClientReport();
        report.title = "החזרים כספיים כתוצאה משינוי הנחיות קורונה";
        List<Refund> refunds = getDataOfThisMonth(Refund.class, "refundDate").stream()
                .filter(refund -> refund.getRefundReason() == RefundReason.RegulationChange)
                .collect(Collectors.toList());

        report.addEntry("כמות החזרים כספיים", refunds.size());
        refunds.stream().map(Refund::getPrice)
                .reduce(Double::sum)
                .ifPresent(totalRefund -> report.addEntry("סך הכל סכום שהוחזר", totalRefund));

        return Collections.singletonList(report);
    }

    @Override
    public List<ClientReport> createCinemaReports(int cinemaId) {
        return new ArrayList<>();
    }
}