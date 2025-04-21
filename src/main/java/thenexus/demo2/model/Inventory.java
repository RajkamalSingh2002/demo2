package thenexus.demo2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_seq")
    @SequenceGenerator(name = "inventory_seq", sequenceName = "inventory_seq", allocationSize = 1)
    @Column(name = "itemid")
    private Long itemId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "availablity", length = 10)
    private String availability;

    // Constructors
    public Inventory() {}

    public Inventory(String name, Long quantity, double price, String availability) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.availability = availability;
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getQuantity() {
        return quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    public String getItemName() {
        return name;  // or whatever field holds the name
    }

}
