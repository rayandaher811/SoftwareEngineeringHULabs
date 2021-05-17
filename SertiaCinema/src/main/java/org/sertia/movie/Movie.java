package org.sertia.movie;

import org.sertia.cinema.LeadingActor;

import java.util.Collection;

public abstract class Movie {
    protected String name;
    private String hebrewName;
    private String producer;
    private Collection<LeadingActor> leadingActors;
    private String summary;
    private Object image;

    protected Movie(String name, String hebrewName, String producer, Collection<LeadingActor> leadingActors, String summary, Object image) {
        this.name = name;
        this.hebrewName = hebrewName;
        this.producer = producer;
        this.leadingActors = leadingActors;
        this.summary = summary;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", hebrewName='" + hebrewName + '\'' +
                ", producer='" + producer + '\'' +
                ", leadingActors=" + leadingActors +
                ", summary='" + summary + '\'' +
                ", image=" + image +
                '}';
    }
}
