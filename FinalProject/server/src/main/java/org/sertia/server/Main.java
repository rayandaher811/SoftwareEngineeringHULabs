package org.sertia.server;

import org.sertia.server.communication.MessageHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        MessageHandler messageHandler = new MessageHandler(1325);

        try {
            messageHandler.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
