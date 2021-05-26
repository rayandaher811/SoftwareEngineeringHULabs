package org.sertia.server.communication.messages;

public class CinemaScreeningMovie {
    private int screeningId;
    private String producerName;
    private String mainActorName;
    private String hebrewName;
    private String name;
    private boolean isComingSoon;
    private String description;
    private String imageUrl;
    private String screeningTimeStampStr;
    private String branchName;
    private int hallNumber;

    public CinemaScreeningMovie(int screeningId, String producerName, String mainActorName, String hebrewName, String name,
                                boolean isComingSoon, String description, String imageUrl, String screeningTimeStampStr, String branchName, int hallNumber) {
        this.screeningId = screeningId;
        this.producerName = producerName;
        this.mainActorName = mainActorName;
        this.hebrewName = hebrewName;
        this.name = name;
        this.isComingSoon = isComingSoon;
        this.description = description;
        this.imageUrl = imageUrl;
        this.screeningTimeStampStr = screeningTimeStampStr;
        this.branchName = branchName;
        this.hallNumber = hallNumber;
    }

    public int getScreeningId() {
        return screeningId;
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

    public String getScreeningTimeStampStr() {
        return screeningTimeStampStr;
    }

    public void setScreeningTimeStampStr(String screeningTimeStampStr) {
        this.screeningTimeStampStr = screeningTimeStampStr;
    }

    public String getBranchName() {
        return branchName;
    }

    public int getHallNumber() {
        return hallNumber;
    }

    @Override
    public String toString() {
        return "CinemaScreeningMovie{" +
                "id=" + screeningId +
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
