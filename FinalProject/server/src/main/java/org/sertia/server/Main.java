package org.sertia.server;

import DataLayer.SessionSupplier;
import org.hibernate.Session;
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
