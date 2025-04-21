package thenexus.demo2.dao;
import thenexus.demo2.model.Vehicle;
import thenexus.demo2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.List;

public class VehicleDAO {

    public List<Vehicle> getAllVehicles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Vehicle", Vehicle.class).list();
        }
    }

    // Call stored procedure: insert_vehicle
    public void insertVehicle(Long vin, String model, Integer year, String licensePlate) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            StoredProcedureQuery query = session.createStoredProcedureQuery("insert_vehicle");
            query.registerStoredProcedureParameter("v_vin", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_model", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_year", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_licenseplate", String.class, ParameterMode.IN);

            query.setParameter("v_vin", vin);
            query.setParameter("v_model", model);
            query.setParameter("v_year", year);
            query.setParameter("v_licenseplate", licensePlate);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: update_vehicle
    public void updateVehicle(Long vin, String model, Integer year, String licensePlate) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            StoredProcedureQuery query = session.createStoredProcedureQuery("update_vehicle");
            query.registerStoredProcedureParameter("p_vin", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_model", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_year", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_licenseplate", String.class, ParameterMode.IN);

            query.setParameter("p_vin", vin);
            query.setParameter("p_model", model);
            query.setParameter("p_year", year);
            query.setParameter("p_licenseplate", licensePlate);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: delete_vehicle
    public void deleteVehicle(Long vin) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            StoredProcedureQuery query = session.createStoredProcedureQuery("delete_vehicle");
            query.registerStoredProcedureParameter("v_vin", Long.class, ParameterMode.IN);
            query.setParameter("v_vin", vin);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Optional: Retrieve a Vehicle (using HQL)
    public Vehicle getByVin(Long vin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Vehicle.class, vin);
        }
    }
}

