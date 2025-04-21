package thenexus.demo2.dao;

import thenexus.demo2.model.Inventory;
import thenexus.demo2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.List;

public class InventoryDAO {

    public List<Inventory> getAllInventory() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Inventory", Inventory.class).list();
        }
    }

    // Call stored procedure: insert_inventory
    public void insertInventory(String name, Long quantity, double price) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("insert_inventory");
            query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_quantity", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_price", Double.class, ParameterMode.IN);

            query.setParameter("p_name", name);
            query.setParameter("p_quantity", quantity);
            query.setParameter("p_price", price);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: update_inventory
    public void updateInventory(Long itemId, String name, Long quantity, double price) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("update_inventory");
            query.registerStoredProcedureParameter("p_itemid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_quantity", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_price", Double.class, ParameterMode.IN);

            query.setParameter("p_itemid", itemId);
            query.setParameter("p_name", name);
            query.setParameter("p_quantity", quantity);
            query.setParameter("p_price", price);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: delete_inventory
    public void deleteInventory(Long itemId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("delete_inventory");
            query.registerStoredProcedureParameter("p_itemid", Long.class, ParameterMode.IN);
            query.setParameter("p_itemid", itemId);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Optional: Retrieve an Inventory item by its ID
    public Inventory getByInventoryId(Long itemId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Inventory.class, itemId);
        }
    }
}
