package com.example.hello_hibernate;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "picture_id")
    private int id;

    @OneToOne(mappedBy = "picture", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Car car;

    public Picture(){

    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Picture id: " + id;
    }
}
