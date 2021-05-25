package org.sertia.server.communication.messages;

public class CinemaScreeningMovie {
    private int id;
    private String producerName;
    private String mainActorName;
    private String hebrewName;
    private String name;
    private boolean isComingSoon;
    private String description;
    private String imageUrl;

    public CinemaScreeningMovie(int id, String producerName, String mainActorName, String hebrewName, String name,
                                boolean isComingSoon, String description, String imageUrl) {
        this.id = id;
        this.producerName = producerName;
        this.mainActorName = mainActorName;
        this.hebrewName = hebrewName;
        this.name = name;
        this.isComingSoon = isComingSoon;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
