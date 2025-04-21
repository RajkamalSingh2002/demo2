package thenexus.demo2.dao;

import thenexus.demo2.model.User;
import thenexus.demo2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.List;

public class UserDAO {

    // Retrieve all Users using HQL.
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    // Call stored procedure: insert_user
    // Assumes your stored procedure insert_user(v_name, v_password, v_role_id, v_active)
    public void insertUser(String username, String password, int roleId, String active) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            StoredProcedureQuery query = session.createStoredProcedureQuery("insert_user");
            query.registerStoredProcedureParameter("v_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_password", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_role_id", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_active", String.class, ParameterMode.IN);

            query.setParameter("v_name", username);
            query.setParameter("v_password", password);
            query.setParameter("v_role_id", roleId);
            query.setParameter("v_active", active);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: update_user
    // Assumes your stored procedure update_user(p_user_id, p_name, p_password, p_role_id, p_active)
    public void updateUser(Long userId, String username, String password, int roleId, String active) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            StoredProcedureQuery query = session.createStoredProcedureQuery("update_user");
            query.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_password", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_role_id", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_active", String.class, ParameterMode.IN);

            query.setParameter("p_user_id", userId);
            query.setParameter("p_name", username);
            query.setParameter("p_password", password);
            query.setParameter("p_role_id", roleId);
            query.setParameter("p_active", active);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }



    // Retrieve a User (using HQL and session.get)
    public User getByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, userId);
        }
    }
}
