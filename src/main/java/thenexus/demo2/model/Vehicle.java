package thenexus.demo2.model;


import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @Column(name = "vin")
    private Long vin;

    @Column(name = "model", length = 30, nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "licenseplate", length = 30, nullable = false)
    private String licensePlate;

    // Constructors
    public Vehicle() {
    }

    public Vehicle(Long vin, String model, Integer year, String licensePlate) {
        this.vin = vin;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
    }

    // Getters and Setters
    public Long getVin() {
        return vin;
    }
    public void setVin(Long vin) {
        this.vin = vin;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}

