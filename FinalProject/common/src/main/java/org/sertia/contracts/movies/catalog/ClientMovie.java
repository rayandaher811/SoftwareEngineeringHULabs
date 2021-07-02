package org.sertia.contracts.movies.catalog;

import java.io.Serializable;

public class ClientMovie implements Serializable {
    public String hebrewName;
    public String name;
    public String description;
    public String mainActorName;
    public String producerName;
	public String posterImageUrl;

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