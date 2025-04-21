package thenexus.demo2.dao;

import thenexus.demo2.model.LowStockNotification;
import thenexus.demo2.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class LowStockNotificationDAO {
    public List<LowStockNotification> getAllNotifications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM LowStockNotification", LowStockNotification.class).list();
        }
    }
}