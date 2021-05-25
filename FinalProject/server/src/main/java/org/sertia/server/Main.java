package org.sertia.server;

import org.joda.time.DateTime;
import org.sertia.server.dl.HibernateSessionFactory;
import org.hibernate.Session;
import org.sertia.server.communication.MessageHandler;
import org.sertia.server.dl.classes.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        MessageHandler messageHandler = new MessageHandler(1325);

        initDb();
        Session session = HibernateSessionFactory.getInstance().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Screening> query = builder.createQuery(Screening.class);
        query.from(Screening.class);

        List<Screening> s =  session.createQuery(query).getResultList();
        try {
            messageHandler.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.close();;
    }

    private static void initDb() {
        Session session = HibernateSessionFactory.getInstance().openSession();
        Producer pOne = new Producer("ONE TWO");
        Actor aOne = new Actor(" ATHREE");
        Movie mOne = new Movie(pOne, aOne, "a", "AAA", false, "bla bla", "walla.co.il");
        Hall hOne = new Hall();
        Screening s = new Screening(1, DateTime.now().getMillis(), hOne, mOne);

        session.beginTransaction();

        session.save(aOne);
        session.save(pOne);
        session.save(mOne);
        session.save(hOne);
        session.save(s);
        session.getTransaction().commit();
    }
}
