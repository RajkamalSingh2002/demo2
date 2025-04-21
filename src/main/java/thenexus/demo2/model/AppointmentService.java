package thenexus.demo2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "appointmentservices")
public class AppointmentService {

    @EmbeddedId
    private AppointmentServiceId id;

    @ManyToOne
    @MapsId("appointmentId")
    @JoinColumn(name = "appointmentid")
    private Appointment appointment;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "serviceid")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "itemid", nullable = false)
    private Inventory inventory;

    @Column(name = "quantityused")
    private int quantityUsed;

    @Column(name = "price")
    private double price;

    // Constructors
    public AppointmentService() {}

    public AppointmentService(Appointment appointment, Service service, Inventory inventory, int quantityUsed, double price) {
        this.id = new AppointmentServiceId(appointment.getAppointmentId(), service.getId());
        this.appointment = appointment;
        this.service = service;
        this.inventory = inventory;
        this.quantityUsed = quantityUsed;
        this.price = price;
    }

    // Getters and Setters
    public AppointmentServiceId getId() {
        return id;
    }
    public void setId(AppointmentServiceId id) {
        this.id = id;
    }
    public Appointment getAppointment() {
        return appointment;
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    public Service getService() {
        return service;
    }
    public void setService(Service service) {
        this.service = service;
    }
    public Inventory getInventory() {
        return inventory;
    }
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public int getQuantityUsed() {
        return quantityUsed;
    }
    public void setQuantityUsed(int quantityUsed) {
        this.quantityUsed = quantityUsed;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public Long getAppointmentId() {
        return (id != null) ? id.getAppointmentId() : null;
    }

    public int getServiceId() {
        return (id != null) ? id.getServiceId() : 0;
    }

    // Assuming your Inventory entity has a getter for its itemId, you can add:
    public Long getItemId() {
        return (inventory != null) ? inventory.getItemId() : null;
    }

}
