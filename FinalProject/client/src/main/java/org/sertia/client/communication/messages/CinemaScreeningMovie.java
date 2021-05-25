package org.sertia.client.communication.messages;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CinemaScreeningMovie {
    private int id;
    private String producerName;
    private String mainActorName;
    private String hebrewName;
    private String name;
    private boolean isComingSoon;
    private String description;
    private String imageUrl;
    private Date screeningTime;

    public CinemaScreeningMovie(int id, String producerName, String mainActorName, String hebrewName, String name,
                                boolean isComingSoon, String description, String imageUrl, long screeningTime) {
        this.id = id;
        this.producerName = producerName;
        this.mainActorName = mainActorName;
        this.hebrewName = hebrewName;
        this.name = name;
        this.isComingSoon = isComingSoon;
        this.description = description;
        this.imageUrl = imageUrl;
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getDefault());
        this.screeningTime = new Date(screeningTime);
    }

    public int getId() {
        return id;
    }

    public String getProducerName() {
        return producerName;
    }

    public String getMainActorName() {
        return mainActorName;
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

    public Date getScreeningTime() {
        return screeningTime;
    }

    @Override
    public String toString() {
        return "CinemaScreeningMovie{" +
                "id=" + id +
                ", producerName='" + producerName + '\'' +
                ", mainActorName='" + mainActorName + '\'' +
                ", hebrewName='" + hebrewName + '\'' +
                ", name='" + name + '\'' +
                ", isComingSoon=" + isComingSoon +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
