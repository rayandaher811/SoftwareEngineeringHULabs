package DataLayer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import DataLayer.Data.*;
import org.sertia.server.pojos.ScreeningMovie;

import java.util.Date;

public class SessionSupplier {
    private static Session session;

    private SessionSupplier(){}

    public static Session getInstance(){
        if(session == null)
            try{
                Configuration configuration = new Configuration();

                // Adding the data objects package.
                configuration.addAnnotatedClass(DataLayer.Data.Actor.class);
                configuration.addAnnotatedClass(Cinema.class);
                configuration.addAnnotatedClass(CostumerComplaint.class);
                configuration.addAnnotatedClass(CostumerInfo.class);
                configuration.addAnnotatedClass(Hall.class);
                configuration.addAnnotatedClass(HallSeat.class);
                configuration.addAnnotatedClass(Movie.class);
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

                SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                session = sessionFactory.openSession();
            }
            catch (Exception e) {
                throw e;
            }
        return session;
    }
}
