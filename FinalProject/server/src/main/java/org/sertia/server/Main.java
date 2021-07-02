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
//        // Recipient's email ID needs to be mentioned.
//        String to = "rayan.daher811@gmail.com";
//
//        // Sender's email ID needs to be mentioned
//        String from = "softwareengineeringteam5@gmail.com";
//
//        // Assuming you are sending email from through gmails smtp
//        String host = "smtp.gmail.com";
//
//        // Get system properties
//        Properties properties = System.getProperties();
//
//        // Setup mail server
//        properties.put("mail.smtp.host", host);
//        properties.put("mail.smtp.port", "465");
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.auth", "true");
//
//        // Get the Session object.// and pass username and password
//        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
//
//            protected PasswordAuthentication getPasswordAuthentication() {
//
//                return new PasswordAuthentication(from, "1qaz@WSX1qaz@WSX");
//
//            }
//
//        });
//
//        // Used to debug SMTP issues
//
//        try {
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));
//
//            // Set To: header field of the header.
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//            // Set Subject: header field
//            message.setSubject("This is the Subject Line!");
//
//            // Now set the actual message
//            message.setText("This is actual message");
//
//            System.out.println("sending...");
//            // Send message
//            Transport.send(message);
//            System.out.println("Sent message successfully....");
//        } catch (MessagingException mex) {
//            mex.printStackTrace();
//        }
//    }
//}
