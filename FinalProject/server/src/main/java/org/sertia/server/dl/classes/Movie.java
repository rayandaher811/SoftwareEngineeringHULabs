package org.sertia.server.dl.classes;

import javax.persistence.*;
import java.time.Duration;
import java.util.Objects;

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

    @Column(length = Integer.MAX_VALUE)
    private String imageUrl;
    private Duration duration;

    public Movie() {
    }

    public Movie(Producer producer, Actor mainActor, String hebrewName, String name, boolean isComingSoon, String description, String imageUrl, Duration duration) {
        this.producer = producer;
        this.mainActor = mainActor;
        this.hebrewName = hebrewName;
        this.name = name;
        this.isComingSoon = isComingSoon;
        this.description = description;
        this.imageUrl = imageUrl;
        this.duration = duration;
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

    public void setComingSoon(boolean comingSoon) {
        isComingSoon = comingSoon;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id && isComingSoon == movie.isComingSoon && Objects.equals(producer, movie.producer) && Objects.equals(mainActor, movie.mainActor) && Objects.equals(hebrewName, movie.hebrewName) && Objects.equals(name, movie.name) && Objects.equals(description, movie.description) && Objects.equals(imageUrl, movie.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, producer, mainActor, hebrewName, name, isComingSoon, description, imageUrl);
    }
}
