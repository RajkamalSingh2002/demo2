package thenexus.demo2.model;



import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customerid")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
    @SequenceGenerator(name = "customer_seq_gen", sequenceName = "customers_seq", allocationSize = 1)
    private Long customerId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "contact", nullable = false)
    private Long contact;

    @Column(name = "address", length = 255)
    private String address;

    // Mapping foreign key "vin" using ManyToOne association
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vin", nullable = false)
    private Vehicle vehicle;

    // Constructors
    public Customer() {
    }

    public Customer(String name, String email, Long contact, Vehicle vehicle, String address) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.vehicle = vehicle;
        this.address = address;
    }

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Long getContact() {
        return contact;
    }
    public void setContact(Long contact) {
        this.contact = contact;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public String toString() {
        // Returns a string with the id and username
        return ""+customerId+" "+name;
    }

}

