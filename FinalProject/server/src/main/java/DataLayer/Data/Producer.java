package DataLayer.Data;

import javax.persistence.*;

@Entity
@Table(name="producers")
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="producer_id")
    private int id;
    private String fullName;

    public Producer(String fullName) {
        this.fullName = fullName;
    }
}
