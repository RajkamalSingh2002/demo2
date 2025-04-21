package thenexus.demo2.dao;

import thenexus.demo2.model.Invoice;
import thenexus.demo2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.List;

public class InvoiceDAO {

    public List<Invoice> getAllInvoices() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Invoice", Invoice.class).list();
        }
    }

    // Call stored procedure: insert_invoice
    public void insertInvoice(Long generatedBy, Long appointmentId, String paidOrNot) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("insert_invoice");
            query.registerStoredProcedureParameter("v_generatedby", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_appointmentid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("v_paidornot", String.class, ParameterMode.IN);

            query.setParameter("v_generatedby", generatedBy);
            query.setParameter("v_appointmentid", appointmentId);
            query.setParameter("v_paidornot", paidOrNot);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: update_invoice
    public void updateInvoice(Long invoiceId, double totalPrice, Long generatedBy, Long appointmentId, String paidOrNot) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("update_invoice");
            query.registerStoredProcedureParameter("p_invoiceid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_totalprice", Double.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_generatedby", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_appointmentid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_paidornot", String.class, ParameterMode.IN);

            query.setParameter("p_invoiceid", invoiceId);
            query.setParameter("p_totalprice", totalPrice);
            query.setParameter("p_generatedby", generatedBy);
            query.setParameter("p_appointmentid", appointmentId);
            query.setParameter("p_paidornot", paidOrNot);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Call stored procedure: delete_invoice
    public void deleteInvoice(Long invoiceId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoredProcedureQuery query = session.createStoredProcedureQuery("delete_invoice");
            query.registerStoredProcedureParameter("v_invoiceid", Long.class, ParameterMode.IN);
            query.setParameter("v_invoiceid", invoiceId);

            query.execute();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Optional: Retrieve an Invoice by its ID
    public Invoice getByInvoiceId(Long invoiceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Invoice.class, invoiceId);
        }
    }

    public Invoice getLatestInvoiceByAppointmentId(Long appointmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Invoice i WHERE i.appointment.appointmentId = :appointmentId ORDER BY i.invoiceDate DESC";
            return session.createQuery(hql, Invoice.class)
                    .setParameter("appointmentId", appointmentId)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

}
