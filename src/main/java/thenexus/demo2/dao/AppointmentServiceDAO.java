package thenexus.demo2.dao;

import thenexus.demo2.model.AppointmentService;
import thenexus.demo2.model.AppointmentServiceId;
import thenexus.demo2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.List;

public class AppointmentServiceDAO {

    public List<AppointmentService> getAllAppointmentServices() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM AppointmentService", AppointmentService.class).list();
        }
    }

    // Call stored procedure: insert_appointment_service
    public void insertAppointmentService(Long appointmentId, int serviceId, Long itemId, int quantityUsed) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("insert_appointment_service");
            query.registerStoredProcedureParameter("v_appointmentid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_serviceid", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_itemid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_quantityused", Integer.class, ParameterMode.IN);

            query.setParameter("v_appointmentid", appointmentId);
            query.setParameter("v_serviceid", serviceId);
            query.setParameter("v_itemid", itemId);
            query.setParameter("v_quantityused", quantityUsed);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: update_appointment_service
    public void updateAppointmentService(Long appointmentId, int serviceId, Long itemId, int quantityUsed) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("update_appointment_service");
            query.registerStoredProcedureParameter("p_appointmentid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_serviceid", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_itemid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_quantityused", Integer.class, ParameterMode.IN);

            query.setParameter("p_appointmentid", appointmentId);
            query.setParameter("p_serviceid", serviceId);
            query.setParameter("p_itemid", itemId);
            query.setParameter("p_quantityused", quantityUsed);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: delete_appointment_service
    public void deleteAppointmentService(Long appointmentId, int serviceId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("delete_appointment_service");
            query.registerStoredProcedureParameter("p_appointmentid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_serviceid", Integer.class, ParameterMode.IN);

            query.setParameter("p_appointmentid", appointmentId);
            query.setParameter("p_serviceid", serviceId);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Method to get an AppointmentService by composite key (appointmentId and serviceId)
    public AppointmentService getByAppointmentServiceId(Long appointmentId, int serviceId) {
        AppointmentService appointmentService = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            AppointmentServiceId id = new AppointmentServiceId(appointmentId, serviceId);
            appointmentService = session.get(AppointmentService.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentService;
    }
}
