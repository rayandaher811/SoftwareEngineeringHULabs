package org.sertia.contracts.movies.catalog;

import java.io.Serializable;
import java.time.Duration;

public class ClientMovie implements Serializable {
    public String hebrewName;
    public String name;
    public String description;
    public String mainActorName;
    public String producerName;
    public String posterImageUrl;
    public Duration duration;
    public int movieId;

    public ClientMovie(String hebrewName, String name, String description,
                       String mainActorName, String producerName, String posterImageUrl, Duration duration, int movieId) {
        this.hebrewName = hebrewName;
        this.name = name;
        this.description = description;
        this.mainActorName = mainActorName;
        this.producerName = producerName;
        this.posterImageUrl = posterImageUrl;
        this.duration = duration;
        this.movieId = movieId;
    }

    public ClientMovie() {
    }

    public String getName() {
        return name;
    }

    public String getHebrewName() {
        return hebrewName;
    }

    public String getDescription() {
        return description;
    }

    public String getMainActorName() {
        return mainActorName;
    }

    public String getProducerName() {
        return producerName;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }
}