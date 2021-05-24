package org.sertia.server.pojos;

import DataLayer.Data.Actor;
import DataLayer.Data.Producer;
import DataLayer.Data.Streaming;

import javax.persistence.ManyToOne;
import java.util.Objects;

public class Movie {

    private String name;
    private String hebrewName;
    private String producerName;
    private String mainActorName;
    private boolean isComingSoon;
    private String description;

    public Movie(String name, String hebrewName, String producerName, String mainActorName, boolean isComingSoon, String description) {
        this.name = name;
        this.hebrewName = hebrewName;
        this.producerName = producerName;
        this.mainActorName = mainActorName;
        this.isComingSoon = isComingSoon;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getHebrewName() {
        return hebrewName;
    }

    public String getProducerName() {
        return producerName;
    }

    public String getMainActorName() {
        return mainActorName;
    }

    public boolean isComingSoon() {
        return isComingSoon;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(name, movie.name); /*&&*/
//                Objects.equals(hebrewName, movie.hebrewName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, hebrewName);
    }
}
