package com.example.hello_hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;

public class App {

    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Picture.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void generateCars() {
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Car car = new Car("MOO-" + random.nextInt(), 100000, 2000 + random.nextInt(19));
            session.save(car);
            session.flush();
        }
    }

    private static List<Car> getAllCars() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> query = builder.createQuery(Car.class);
        query.from(Car.class);
        List<Car> data = session.createQuery(query).getResultList();
        return data;
    }

    private static void printAllCars() throws Exception {
        List<Car> cars = getAllCars();
        for (Car car : cars) {
            System.out.print("Id: ");
            System.out.print(car.getId());
            System.out.print(", License plate: ");
            System.out.print(car.getLicensePlate());
            System.out.print(", Price: ");
            System.out.print(car.getPrice());
            System.out.print(", Year: ");
            System.out.print(car.getYear());
            System.out.print('\n');
        }
    }

    private static void generatePeopleWithCars(){
        Person p1 = new Person("AAAAA", "aaaaa", "aaaaAAAAA", "aaaaa@gmail.com");
        Person p2 = new Person("BBBBB", "bbbbb", "bbbbBBBBB", "bbbbb@gmail.com");
        Person p3 = new Person("CCCCC", "ccccc", "CCCCCcccc", "ccccc@gmail.com");
        Person p4 = new Person("DDDDD", "ddddd", "ddddDDDDD", "ddddd@gmail.com");
        Person p5 = new Person("EEEEE", "eeeee", "eeEEeeEEe", "eeee@gmail.com");

        Picture one = new Picture();
        Picture two = new Picture();
        Picture three = new Picture();
        Picture four = new Picture();
        Picture five = new Picture();


        Car a = new Car("A1234a", 32.2, 33333, p1, one);
        Car b = new Car("B1234b", 32, 33333, p2, two);
        Car c = new Car("C1234c", 32, 33333, p2, three);
        Car d = new Car("D1234d", 32, 33333, p3, four);
        Car e = new Car("E1234e", 32, 33333, p4, five);

        // Assigning car to picture
        one.setCar(a);
        two.setCar(b);
        three.setCar(c);
        four.setCar(d);
        five.setCar(e);

        // Adding cars to p1, and saving
        HashSet<Car> p1Cars = new HashSet<>();
        p1Cars.add(a);
        p1.setCars(p1Cars);
        session.save(p1);
        session.save(a);

        // Adding cars to p2, and saving
        HashSet<Car> p2Cars = new HashSet<>();
        p2Cars.add(b);
        p2Cars.add(c);
        p2.setCars(p2Cars);
        session.save(p2);
        session.save(b);
        session.save(c);

        // Adding cars to p3, and saving
        HashSet<Car> p3Cars = new HashSet<>();
        p3Cars.add(d);
        p3Cars.add(e);
        p3.setCars(p3Cars);
        session.save(p3);
        session.save(d);
        session.save(e);
        // p5 without cars
        session.save(p4);
        session.save(p5);

        // saving pictures
        session.save(one);
        session.save(two);
        session.save(three);
        session.flush();
    }


    public static void main(String[] args) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            generatePeopleWithCars();

            session.getTransaction().commit(); // Save everything.


        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
        }
    }
}