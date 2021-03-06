package org.sertia.server.dl.classes;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
    public static final String usernameFieldName = "username";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    
	@NaturalId
    @Column(name = usernameFieldName)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
