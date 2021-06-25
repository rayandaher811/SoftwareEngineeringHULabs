package org.sertia.server.bl.Services;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.sertia.server.dl.classes.*;

import javax.persistence.PersistenceContext;

public class CustomerNotifier implements ICustomerNotifier {
    private static CustomerNotifier notifier;

    private CustomerNotifier() {
    }

    public static CustomerNotifier getInstance() {
        if (notifier == null)
            notifier = new CustomerNotifier();

        return notifier;
    }

    @Override
    public void notify(String phoneNumber, String message) {

    }
}
