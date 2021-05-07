package com.example.hello_hibernate;

import edu.emory.mathcs.backport.java.util.Collections;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="GARAGES")
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="garage_id")
    private int id;
    private String address;

    @ManyToMany(mappedBy = "garages")
    private Set<Person> owners;

    @ManyToMany(mappedBy = "garages")
    private Set<Car> cars;

    public Garage() {
    }

    public Garage(String address, Set<Person> owners, Set<Car> cars) {
        this.address = address;
        this.owners = owners;
        this.cars = cars;
    }

    public Garage(String address) {
        this(address, Collections.emptySet(), Collections.emptySet());
    }

    public void setOwners(Set<Person> owners) {
        this.owners = owners;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        String allowedCarsIds = cars.stream().map(Car::getLicensePlate).collect(Collectors.toSet()).toString();
        return "Garage Id: " + id + ", Address: " + address + ", allowed cars ids: (license plate num): " + allowedCarsIds;
    }

    public String getAddress() {
        return address;
    }
}
