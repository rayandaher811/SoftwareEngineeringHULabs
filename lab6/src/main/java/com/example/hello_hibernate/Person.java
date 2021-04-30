package com.example.hello_hibernate;

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

    public Person(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public Person() {
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
}
