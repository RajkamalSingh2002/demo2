package thenexus.demo2.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AppointmentServiceId implements Serializable {

    @Column(name = "appointmentid")
    private Long appointmentId;

    @Column(name = "serviceid")
    private int serviceId;

    // Constructors
    public AppointmentServiceId() {}

    public AppointmentServiceId(Long appointmentId, int serviceId) {
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
    }

    // Getters and Setters
    public Long getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    public int getServiceId() {
        return serviceId;
    }
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    // Override equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentServiceId)) return false;
        AppointmentServiceId that = (AppointmentServiceId) o;
        return serviceId == that.serviceId && Objects.equals(appointmentId, that.appointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId, serviceId);
    }
}
