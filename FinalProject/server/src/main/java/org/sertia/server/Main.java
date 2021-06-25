package org.sertia.server;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.sertia.contracts.screening.ticket.ClientSeatMap;
import org.sertia.contracts.screening.ticket.HallSeat;
import org.sertia.contracts.screening.ticket.PaymentResponse;
import org.sertia.contracts.screening.ticket.ScreeningTicketWithSeatsRequest;
import org.sertia.server.bl.CovidRegulationsController;
import org.sertia.server.bl.ScreeningTicketController;
import org.sertia.server.communication.MessageHandler;
import org.sertia.server.dl.HibernateSessionFactory;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        fillDb();
        CovidRegulationsController covidRegulationsController = new CovidRegulationsController();
        ScreeningTicketController screeningTicketController = new ScreeningTicketController(covidRegulationsController);
        MessageHandler messageHandler = new MessageHandler(1325, screeningTicketController);
        ScreeningTicketWithSeatsRequest request = new ScreeningTicketWithSeatsRequest();
        request.chosenSeats = new ArrayList<>();
        request.chosenSeats.add(new HallSeat(1));
        request.chosenSeats.add(new HallSeat(6));
        request.screeningId = 1;
        request.cardHolderId = "021";
        request.cardHolderName = "banana";
        request.cardHolderEmail = "xxx";
        request.cardHolderPhone = "02321";
        request.creditCardNumber = "2edsa";
        request.expirationDate = DateTime.now();
        request.cvv = "039";

        PaymentResponse paymentResponse = screeningTicketController.buyTicketWithSeatChose(request);

        ClientSeatMap seatMapForScreening = screeningTicketController.getSeatMapForScreening(1);

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
