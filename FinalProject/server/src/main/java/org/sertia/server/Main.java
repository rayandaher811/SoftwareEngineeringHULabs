package org.sertia.server;

import org.hibernate.Session;
import org.sertia.server.communication.MessageHandler;
import org.sertia.server.dl.HibernateSessionFactory;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        fillDb();
        MessageHandler messageHandler = new MessageHandler(1325);
        try {
            messageHandler.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fillDb() {
        Session session = HibernateSessionFactory.getInstance().openSession();

        try {
            DBFiller dbFiller = new DBFiller();
            dbFiller.initialize();
            session.beginTransaction();
            dbFiller.getActors().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getProducers().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getMovies().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getScreenableMovies().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getUsers().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getCinemas().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getHalls().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getHallSeats().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getScreenings().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getStreamings().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getHalls().forEach(obj -> session.save(obj));
            session.flush();
            dbFiller.getHallSeats().forEach(obj -> session.save(obj));
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
        } finally {
            session.close();
        }
    }
}
