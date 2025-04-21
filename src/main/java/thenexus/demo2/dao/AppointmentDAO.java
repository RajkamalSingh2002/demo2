package thenexus.demo2.dao;

import thenexus.demo2.model.Appointment;
import thenexus.demo2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.Collections;
import java.util.List;

public class AppointmentDAO {

    public List<Appointment> getAllAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Appointment", Appointment.class).list();
        }
    }

    // Call stored procedure: insert_appointment
    public void insertAppointment(Long customerId, String status, Long assignedMechanic) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("insert_appointment");
            query.registerStoredProcedureParameter("v_customerid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_status", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_assignedmechanic", Long.class, ParameterMode.IN);

            query.setParameter("v_customerid", customerId);
            query.setParameter("v_status", status);
            query.setParameter("v_assignedmechanic", assignedMechanic);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: update_appointment
    public void updateAppointment(Long appointmentId, String status, Long assignedMechanic) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("update_appointment");
            query.registerStoredProcedureParameter("p_appointmentid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_status", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_assignedmechanic", Long.class, ParameterMode.IN);

            query.setParameter("p_appointmentid", appointmentId);
            query.setParameter("p_status", status);
            query.setParameter("p_assignedmechanic", assignedMechanic);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: delete_appointment
    public void deleteAppointment(Long appointmentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("delete_appointment");
            query.registerStoredProcedureParameter("v_appointmentid", Long.class, ParameterMode.IN);
            query.setParameter("v_appointmentid", appointmentId);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Appointment getByAppointmentId(Long appointmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Appointment.class, appointmentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Appointment> getAppointmentsByMechanic(int mechanicId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Appointment a WHERE a.assignedMechanic.id = :mechanicId";
            return session.createQuery(hql, Appointment.class)
                    .setParameter("mechanicId", mechanicId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
