package thenexus.demo2.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoices_seq")
    @SequenceGenerator(name = "invoices_seq", sequenceName = "invoices_seq", allocationSize = 1)
    @Column(name = "invoiceid")
    private Long invoiceId;

    @Column(name = "totalprice")
    private double totalPrice;

    @Column(name = "invoicedate")
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;

    @Column(name = "paidornot", length = 10)
    private String paidOrNot;

    @ManyToOne
    @JoinColumn(name = "generatedby", nullable = false)
    private User generatedBy;

    @ManyToOne
    @JoinColumn(name = "appointmentid", nullable = false)
    private Appointment appointment;

    // Constructors
    public Invoice() {}

    public Invoice(double totalPrice, Date invoiceDate, String paidOrNot, User generatedBy, Appointment appointment) {
        this.totalPrice = totalPrice;
        this.invoiceDate = invoiceDate;
        this.paidOrNot = paidOrNot;
        this.generatedBy = generatedBy;
        this.appointment = appointment;
    }

    // Getters and Setters
    public Long getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Date getInvoiceDate() {
        return invoiceDate;
    }
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    public String getPaidOrNot() {
        return paidOrNot;
    }
    public void setPaidOrNot(String paidOrNot) {
        this.paidOrNot = paidOrNot;
    }
    public User getGeneratedBy() {
        return generatedBy;
    }
    public void setGeneratedBy(User generatedBy) {
        this.generatedBy = generatedBy;
    }
    public Appointment getAppointment() {
        return appointment;
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
