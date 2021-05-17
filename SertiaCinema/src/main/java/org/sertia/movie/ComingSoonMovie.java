package org.sertia.movie;

import org.sertia.cinema.LeadingActor;

import java.util.Collection;

public class ComingSoonMovie extends Movie {
    protected ComingSoonMovie(String name, String hebrewName, String producer, Collection<LeadingActor> leadingActors, String summary, Object image) {
        super(name, hebrewName, producer, leadingActors, summary, image);
    }
}
