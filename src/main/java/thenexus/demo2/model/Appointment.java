package thenexus.demo2.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_seq")
    @SequenceGenerator(name = "appointment_seq", sequenceName = "appointments_seq", allocationSize = 1)
    @Column(name = "appointmentid")
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "customerid", nullable = false)
    private Customer customer;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "appointmentdate")
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;

    @ManyToOne
    @JoinColumn(name = "assignedmechanic", nullable = false)
    private User assignedMechanic;

    // Constructors
    public Appointment() {}

    public Appointment(Customer customer, String status, Date appointmentDate, User assignedMechanic) {
        this.customer = customer;
        this.status = status;
        this.appointmentDate = appointmentDate;
        this.assignedMechanic = assignedMechanic;
    }

    // Getters and Setters
    public Long getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public User getAssignedMechanic() {
        return assignedMechanic;
    }
    public void setAssignedMechanic(User assignedMechanic) {
        this.assignedMechanic = assignedMechanic;
    }
    @Override
    public String toString() {
        return appointmentId.toString() ; // or format it however you'd like
    }
}
