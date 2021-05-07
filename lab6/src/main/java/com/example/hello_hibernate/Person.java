package com.example.hello_hibernate;

import edu.emory.mathcs.backport.java.util.Collections;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="PERSON")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="person_id")
    private int id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    @OneToMany(mappedBy = "person")
    private Set<Car> cars;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Person_Garage",
            joinColumns = { @JoinColumn(name = "person_id") },
            inverseJoinColumns = { @JoinColumn(name = "garage_id") }
    )
    private Set<Garage> garages;

    public Person(String firstName, String lastName, String password, String email, Set<Garage> garages) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.garages = garages;
    }

    public Person(String firstName, String lastName, String password, String email) {
        this(firstName, lastName, password, email, Collections.emptySet());
    }

    public Person() {
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public Person setGarages(Set<Garage> garages) {
        this.garages = garages;
        return this;
    }

    @Override
    public String toString() {
        return "First name: " + firstName + ", lastName: " + lastName + ", password: " + password + ", email: " + email + ", owned garages: " + garages;
    }
}
