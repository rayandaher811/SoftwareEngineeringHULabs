package org.sertia.contracts.movies.catalog;

import java.io.Serializable;
import java.util.List;

public class SertiaCatalog implements Serializable {
    public SertiaCatalog(List<SertiaMovie> movies) {
        this.movies = movies;
    }

    public List<SertiaMovie> movies;
}