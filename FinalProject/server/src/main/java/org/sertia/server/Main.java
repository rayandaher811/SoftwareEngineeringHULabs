package org.sertia.server;

<<<<<<< HEAD
=======
import DataLayer.SessionSupplier;
import org.hibernate.Session;
>>>>>>> de7a8b1... project compiles, added docker-compose.yml with DB presenter
import org.sertia.server.communication.MessageHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        MessageHandler messageHandler = new MessageHandler(1325);
        Session session = SessionSupplier.getInstance();
        session.beginTransaction();

        try {
            messageHandler.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.close();;
    }
}
