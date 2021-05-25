package org.sertia.server.dl.classes;

import javax.persistence.*;

@Entity
@Table(name="costumers_info")
public class CostumerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="costumer_id")
    private int id;
    private String fullName;
    private String email;
    private String phoneNumber;

    public CostumerInfo(String fullName, String email, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
