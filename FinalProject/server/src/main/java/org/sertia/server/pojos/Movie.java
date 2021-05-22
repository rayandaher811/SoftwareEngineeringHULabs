package org.sertia.server.pojos;

import java.util.Objects;

public class Movie {
    private String name;
    private String hebrewName;

    public Movie(String name, String hebrewName) {
        this.name = name;
        this.hebrewName = hebrewName;
    }

    public String getName() {
        return name;
    }

    public String getHebrewName() {
        return hebrewName;
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
