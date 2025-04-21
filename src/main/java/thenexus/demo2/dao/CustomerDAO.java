package thenexus.demo2.dao;


import thenexus.demo2.model.Customer;
import thenexus.demo2.model.Vehicle;
import thenexus.demo2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.List;

public class CustomerDAO {


    public List<Customer> getAllCustomers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Customer", Customer.class).list();
        }
    }

    // Call stored procedure: insert_customer
    public void insertCustomer(String name, String email, Long contact, Long vin, String address) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            StoredProcedureQuery query = session.createStoredProcedureQuery("insert_customer");
            query.registerStoredProcedureParameter("v_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_email", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_contact", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_vin", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_address", String.class, ParameterMode.IN);

            query.setParameter("v_name", name);
            query.setParameter("v_email", email);
            query.setParameter("v_contact", contact);
            query.setParameter("v_vin", vin);
            query.setParameter("v_address", address);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: update_customer
    public void updateCustomer(Long customerId, String name, String email, Long contact, Long vin, String address) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            StoredProcedureQuery query = session.createStoredProcedureQuery("update_customer");
            query.registerStoredProcedureParameter("p_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_contact", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_vin", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN);

            query.setParameter("p_id", customerId);
            query.setParameter("p_name", name);
            query.setParameter("p_email", email);
            query.setParameter("p_contact", contact);
            query.setParameter("p_vin", vin);
            query.setParameter("p_address", address);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: delete_customer
    public void deleteCustomer(Long customerId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            StoredProcedureQuery query = session.createStoredProcedureQuery("delete_customer");
            query.registerStoredProcedureParameter("v_id", Long.class, ParameterMode.IN);
            query.setParameter("v_id", customerId);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Optional: Retrieve a Customer (using HQL)
    public Customer getById(Long customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Customer.class, customerId);
        }
    }
}

