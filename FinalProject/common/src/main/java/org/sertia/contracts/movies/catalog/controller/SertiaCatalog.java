package org.sertia.contracts.movies.catalog.controller;

import java.util.List;

public class SertiaCatalog {
    public SertiaCatalog(List<SertiaMovie> movies) {
        this.movies = movies;
    }

    public List<SertiaMovie> movies;
}