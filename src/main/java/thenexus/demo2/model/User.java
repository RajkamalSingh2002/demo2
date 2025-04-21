package thenexus.demo2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "userid")
    private int id;

    @Column(name = "name", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active")
    private String active;

    @ManyToOne
    @JoinColumn(name = "roleid", nullable = false)
    private Role role;

    // Constructors, getters, and setters

    public User() {
    }

    public User(String username, String password, String active, Role role) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    @Override
    public String toString() {
        // Returns a string with the id and username
        return ""+id+" "+username;
    }
}
