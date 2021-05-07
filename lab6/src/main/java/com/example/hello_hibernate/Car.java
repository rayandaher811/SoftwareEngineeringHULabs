package com.example.hello_hibernate;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "CARS")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String licensePlate;
    private double price;
    @Column(name = "manufacturing_year")
    private int year;
    @ManyToOne
    @JoinColumn(name="person_id", nullable = true)
    private Person person;

    @OneToOne
    @MapsId
    @JoinColumn(name = "picture_id", nullable = true)
    private Picture picture;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Car_Garage",
            joinColumns = { @JoinColumn(name = "id") },
            inverseJoinColumns = { @JoinColumn(name = "garage_id") }
    )
    private Set<Garage> garages;

    public Car(String licensePlate, double price, int year, Picture pic) {
        super();
        this.licensePlate = licensePlate;
        this.price = price;
        this.year = year;
        this.picture = pic;
    }

    public Car() {
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setGarages(Set<Garage> garages) {
        this.garages = garages;
    }
}