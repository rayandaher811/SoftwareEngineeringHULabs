package org.sertia.contracts.movies.catalog;

import java.io.Serializable;

public class ClientHall implements Serializable {
    public int hallId;
    public int hallNumber;

    public ClientHall(int hallId, int hallNumber) {
        this.hallId = hallId;
        this.hallNumber = hallNumber;
    }
}
