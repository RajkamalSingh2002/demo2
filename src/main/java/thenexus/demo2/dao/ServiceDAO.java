package thenexus.demo2.dao;

import thenexus.demo2.model.Service;
import thenexus.demo2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.List;

public class ServiceDAO {

    public List<Service> getAllServices() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Service", Service.class).list();
        }
    }

    // Call stored procedure: insert_service
    public void insertService(String name, String description) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("insert_service");
            query.registerStoredProcedureParameter("v_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_description", String.class, ParameterMode.IN);

            query.setParameter("v_name", name);
            query.setParameter("v_description", description);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: update_service
    public void updateService(int serviceId, String name, String description) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("update_service");
            query.registerStoredProcedureParameter("p_serviceid", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN);

            query.setParameter("p_serviceid", serviceId);
            query.setParameter("p_name", name);
            query.setParameter("p_description", description);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: delete_service
    public void deleteService(int serviceId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("delete_service");
            query.registerStoredProcedureParameter("p_serviceid", Integer.class, ParameterMode.IN);
            query.setParameter("p_serviceid", serviceId);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Optional: Retrieve a Service by its ID
    public Service getByServiceId(int serviceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Service.class, serviceId);
        }
    }
}
