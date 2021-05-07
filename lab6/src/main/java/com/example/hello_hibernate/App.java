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
    private static ArrayList<Car> cars = new ArrayList<>();
    private static ArrayList<Picture> pictures = new ArrayList<>();
    private static ArrayList<Person> people = new ArrayList<>();
    private static ArrayList<Garage> garages = new ArrayList<>();

    private static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Initialize applications cars
     *
     * @return
     */
    private static boolean initializeCars(int numberOfCars) {
        for (int i = 0; i < numberOfCars; i++) {
            String licensePlate = String.valueOf((char) ('A' + i))
                    + "1234"
                    + String.valueOf((char) ('A' + i)).toLowerCase();
            double price = getRandomNumber(15, 90) * 1000;
            int year = getRandomNumber(2010, 2021);
            cars.add(new Car(licensePlate, price, year, pictures.get(i)));
        }
        return true;
    }

    private static boolean initializePeople(int numberOfPeople) {
        for (int i = 0; i < numberOfPeople; i++) {
            String firstName = String.valueOf((char) ('A' + i)).repeat(5);
            String lastName = String.valueOf((char) ('a' + i)).repeat(5);
            String password = lastName + firstName;
            String email = lastName + "@gmail.com";
            people.add(new Person(firstName, lastName, password, email));
        }
        return true;
    }

    private static boolean initializePictures(int numberOfPicutres) {
        for (int i = 0; i < numberOfPicutres; i++) {
            pictures.add(new Picture());
        }
        return true;
    }

    private static boolean initializeGarages(int numberOfGarages) {
        for (int i = 0; i < numberOfGarages; i++)
            garages.add(new Garage("Histadrut " + i + ", Haifa"));
        return true;
    }

    private static boolean initializeDbData(int numberOfCars, int numberOfPeople, int numberOfGarages) {
        return initializePictures(numberOfCars)
                && initializeCars(numberOfCars)
                && initializePeople(numberOfPeople)
                && initializeGarages(numberOfGarages);
    }

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Picture.class);
        configuration.addAnnotatedClass(Garage.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static List<Car> getAllCars() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> query = builder.createQuery(Car.class);
        query.from(Car.class);
        List<Car> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<Person> getAllPersons() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        query.from(Person.class);
        List<Person> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<Garage> getAllGarages() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Garage> query = builder.createQuery(Garage.class);
        query.from(Garage.class);
        List<Garage> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<Picture> getAllPictures() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Picture> query = builder.createQuery(Picture.class);
        query.from(Picture.class);
        List<Picture> data = session.createQuery(query).getResultList();
        return data;
    }

    private static void initializeDbSpecificRelations() {
        // Setting first person to be the owner of both garages
        people.get(0).setGarages(new HashSet<>(garages));

        // Setting second person to be the owner of second garage
        people.get(1).setGarages(Collections.singleton(garages.get(1)));

        // Setting third person to be the owner of first garage
        people.get(2).setGarages(Collections.singleton(garages.get(0)));

        // Setting fifth person to be the owner of both garages
        people.get(4).setGarages(new HashSet<>(garages));

        // Setting garages to be owned by persons
        Set<Person> firstGarageOwners = new HashSet<>();
        firstGarageOwners.add(people.get(0));
        firstGarageOwners.add(people.get(2));
        firstGarageOwners.add(people.get(4));
        garages.get(0).setOwners(firstGarageOwners);

        Set<Person> secondGarageOwners = new HashSet<>();
        secondGarageOwners.add(people.get(0));
        secondGarageOwners.add(people.get(4));
        garages.get(1).setOwners(secondGarageOwners);

        // Assigning car to picture
        for (int i = 0; i < pictures.size() && i < cars.size(); i++) {
            pictures.get(i).setCar(cars.get(i));
        }

        // Adding cars to p1, and saving
        HashSet<Car> p1Cars = new HashSet<>();
        p1Cars.add(cars.get(0));
        people.get(0).setCars(p1Cars);
        cars.get(0).setPerson(people.get(0));

        // Adding cars to p2, and saving
        HashSet<Car> p2Cars = new HashSet<>();
        p2Cars.add(cars.get(1));
        p2Cars.add(cars.get(2));
        people.get(1).setCars(p2Cars);
        cars.get(1).setPerson(people.get(1));
        cars.get(2).setPerson(people.get(1));

        // Adding cars to p3, and saving
        HashSet<Car> p3Cars = new HashSet<>();
        p3Cars.add(cars.get(3));
        p3Cars.add(cars.get(4));
        people.get(2).setCars(p3Cars);
        cars.get(3).setPerson(people.get(2));
        cars.get(4).setPerson(people.get(2));

        // Assigning cars 1 2 3 4 to garage 1
        Set<Car> carsForGarageOne = new HashSet<>();
        carsForGarageOne.add(cars.get(0));
        carsForGarageOne.add(cars.get(1));
        carsForGarageOne.add(cars.get(2));
        carsForGarageOne.add(cars.get(3));
        garages.get(0).setCars(carsForGarageOne);

        // Assigning cars 3 4 5 to garage 2
        Set<Car> carsForGarageTwo = new HashSet<>();
        carsForGarageTwo.add(cars.get(2));
        carsForGarageTwo.add(cars.get(3));
        carsForGarageTwo.add(cars.get(4));
        garages.get(1).setCars(carsForGarageTwo);

        // Assigning garage 1 to car 1
        cars.get(0).setGarages(Collections.singleton(garages.get(0)));

        // Assigning garage 1 to car 2
        cars.get(1).setGarages(Collections.singleton(garages.get(0)));

        // Assigning garages 1,2 to cars 3, 4
        cars.get(2).setGarages(new HashSet<>(garages));
        cars.get(3).setGarages(new HashSet<>(garages));

        // Assigning garage 2 to car 5
        cars.get(4).setGarages(Collections.singleton(garages.get(1)));
    }

    public static boolean isDataStoredAsExpected() {
        List<Car> actualCars = getAllCars();
        List<Garage> actualGarages = getAllGarages();
        List<Person> actualPeople = getAllPersons();
        List<Picture> actualPictures = getAllPictures();

        return actualCars.equals(cars) && actualGarages.equals(garages) && actualPeople.equals(people) && actualPictures.equals(pictures);
    }

    private static boolean writeToDB() {
        cars.forEach(car -> session.save(car));
        garages.forEach(garage -> session.save(garage));
        people.forEach(person -> session.save(person));
        pictures.forEach(picture -> session.save(picture));
        session.flush();
        return true;
    }

    public static void main(String[] args) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            initializeDbData(5, 5, 2);
            initializeDbSpecificRelations();

            if (writeToDB()) {
                session.getTransaction().commit(); // Save everything.
                if (!isDataStoredAsExpected()) {
                    System.err.println("Data not stored as expected! :(");
                }
            }
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