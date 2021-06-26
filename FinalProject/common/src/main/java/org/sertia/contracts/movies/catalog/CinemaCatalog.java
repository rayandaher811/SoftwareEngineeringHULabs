package org.sertia.contracts.movies.catalog;

import java.io.Serializable;
import java.util.List;

public class CinemaCatalog implements Serializable {
    public List<CinemaScreeningMovie> movies;
}