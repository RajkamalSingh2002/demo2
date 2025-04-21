package thenexus.demo2.model;

import jakarta.persistence.*; import java.util.Date;

@Entity @Table(name = "low_stock_notifications")
public class LowStockNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "notification_seq", allocationSize = 1)
    @Column(name = "notification_id")
    private int notificationId;

    @Column(name = "itemid")
    private int itemId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "notification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate;

    // Default constructor
    public LowStockNotification() {
    }

    // Parameterized constructor (optional)
    public LowStockNotification(int itemId, int quantity, Date notificationDate) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.notificationDate = notificationDate;
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }
}