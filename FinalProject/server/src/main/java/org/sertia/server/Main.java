package org.sertia.server;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.sertia.server.communication.MessageHandler;
import org.sertia.server.dl.HibernateSessionFactory;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String dbHost = args[0];
        String dbPort = args[1];
        String dbName = args[2];
        String dbUser = args[3];
        String dbPassword = args[4];

        HibernateSessionFactory.initConfig(dbHost, dbPort, dbName, dbUser, dbPassword);

        fillDb();
        MessageHandler messageHandler = new MessageHandler(1235);
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
