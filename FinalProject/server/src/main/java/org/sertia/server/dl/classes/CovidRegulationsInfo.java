package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="covid_regulations_info")
public class CovidRegulationsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="regulations_info_id")
    private int id;

    private int maxNumberOfPeople;

    private boolean isActive;

    public CovidRegulationsInfo() {
    }
}
