package thenexus.demo2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "services_seq")
    @SequenceGenerator(name = "services_seq", sequenceName = "services_seq", allocationSize = 1)
    @Column(name = "serviceid")
    private int id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

    // Constructors
    public Service() {}

    public Service(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String toString() {
        // Returns a string with the id and username
        return getId()+getName();
    }
}
