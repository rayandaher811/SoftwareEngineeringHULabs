package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="covid_regulations_info")
public class CovidRegulationsInfo {
    // The table is single lined table and this is it's single record Id
    public static final int singleRecordId = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="regulations_info_id")
    private int id;

    private int maxNumberOfPeople;

    private boolean isActive;

    public CovidRegulationsInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxNumberOfPeople() {
        return maxNumberOfPeople;
    }

    public void setMaxNumberOfPeople(int maxNumberOfPeople) {
        this.maxNumberOfPeople = maxNumberOfPeople;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
