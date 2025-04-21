package thenexus.demo2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "roleid")
    private int id;

    @Column(name = "rolename", nullable = false)
    private String roleName;

    // Getters and setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    @Override
    public String toString() {
        // Returns a string with the id and username
        return ""+id;
    }
}
