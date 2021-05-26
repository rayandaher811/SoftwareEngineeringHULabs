package org.sertia.server.dl;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.sertia.server.dl.classes.*;

import javax.persistence.PersistenceContext;

public class HibernateSessionFactory {
    @PersistenceContext
    private static SessionFactory sessionFactory;

    private HibernateSessionFactory() {
    }

    public static SessionFactory getInstance() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Adding the data objects package.
                configuration.addAnnotatedClass(Actor.class);
                configuration.addAnnotatedClass(Cinema.class);
                configuration.addAnnotatedClass(CostumerComplaint.class);
                configuration.addAnnotatedClass(CostumerInfo.class);
                configuration.addAnnotatedClass(Hall.class);
                configuration.addAnnotatedClass(HallSeat.class);
                configuration.addAnnotatedClass(Movie.class);
                configuration.addAnnotatedClass(ScreenableMovie.class);
                configuration.addAnnotatedClass(PaymentInfo.class);
                configuration.addAnnotatedClass(PriceChangeRequest.class);
                configuration.addAnnotatedClass(Producer.class);
                configuration.addAnnotatedClass(Refund.class);
                configuration.addAnnotatedClass(Screening.class);
                configuration.addAnnotatedClass(ScreeningTicket.class);
                configuration.addAnnotatedClass(Streaming.class);
                configuration.addAnnotatedClass(StreamingLink.class);
                configuration.addAnnotatedClass(TicketsVoucher.class);
                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                throw e;
            }
        }

        return sessionFactory;
    }
}
