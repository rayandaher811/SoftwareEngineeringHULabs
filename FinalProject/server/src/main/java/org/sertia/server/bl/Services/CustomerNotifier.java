package org.sertia.server.bl.Services;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.sertia.server.dl.classes.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.PersistenceContext;
import java.util.Properties;

public class CustomerNotifier implements ICustomerNotifier {
    private static CustomerNotifier notifier;
    private static final String serverEmail = "softwareengineeringteam5@gmail.com";
    private static final String serverEmailPass = "1qaz@WSX1qaz@WSX";
    private CustomerNotifier() {
    }

    public static CustomerNotifier getInstance() {
        if (notifier == null)
            notifier = new CustomerNotifier();

        return notifier;
    }

    @Override
    public void notify(String email, String messageToSend) {
        String receiverEmail = email;
        String message = messageToSend;

        new Thread(() -> {
            // Setting up the authenticator
            Session session = Session.getInstance(getGmailServerProperties(), new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(serverEmail, serverEmailPass);
                }

            });

            try {
                // Create The message.
                MimeMessage sentMessage = new MimeMessage(session);

                // Setting the from header field
                sentMessage.setFrom(new InternetAddress(serverEmail));

                // Defining the Recipient
                sentMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));

                // Setting the email content
                sentMessage.setSubject("Sertia Cinema Notification");
                sentMessage.setText(message, "UTF-8");

                Transport.send(sentMessage);
                System.out.println("An email sent to " + receiverEmail + " with the message - " + message);
            } catch (Exception e) {
                System.out.println("The server couldn't send the email to " + receiverEmail);
                e.printStackTrace();
            }
        }).start();
    }

    private Properties getGmailServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        return properties;
    }
}
