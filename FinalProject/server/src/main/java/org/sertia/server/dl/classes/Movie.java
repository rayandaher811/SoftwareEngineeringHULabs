package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity(name = "movies")
@Inheritance(strategy = InheritanceType.JOINED)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="movie_id")
    private int id;

    @ManyToOne
    private Producer producer;

    @ManyToOne
    private Actor mainActor;

    private String hebrewName;
    private String name;
    private boolean isComingSoon;
    private String description;
    private String imageUrl;

    public Movie(Producer producer, Actor mainActor, String hebrewName, String name, boolean isComingSoon, String description, String imageUrl) {
        this.producer = producer;
        this.mainActor = mainActor;
        this.hebrewName = hebrewName;
        this.name = name;
        this.isComingSoon = isComingSoon;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Producer getProducer() {
        return producer;
    }

    public Actor getMainActor() {
        return mainActor;
    }

    public String getHebrewName() {
        return hebrewName;
    }

    public String getName() {
        return name;
    }

    public boolean isComingSoon() {
        return isComingSoon;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }
}
