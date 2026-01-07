package org.hsc.hospital_management_system.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hsc.hospital_management_system.entity.Medecin;
import java.util.List;

/**
 * DAO for Medecin (Doctor) entity
 */
public class MedecinDAO extends GenericDAO<Medecin> {
    public MedecinDAO() {
        super(Medecin.class);
    }

    /**
     * Find all doctors
     */
    public List<Medecin> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Medecin> query = em.createQuery(
                    "SELECT m FROM Medecin m ORDER BY m.lastName", Medecin.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find doctor by email
     */
    public Medecin findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Medecin> query = em.createQuery(
                    "SELECT m FROM Medecin m WHERE m.email = :email", Medecin.class);
            query.setParameter("email", email);
            List<Medecin> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Find doctor by license number
     */
    public Medecin findByLicenseNumber(String licenseNumber) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Medecin> query = em.createQuery(
                    "SELECT m FROM Medecin m WHERE m.licenseNumber = :licenseNumber", Medecin.class);
            query.setParameter("licenseNumber", licenseNumber);
            List<Medecin> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Find doctors by specialization
     */
    public List<Medecin> findBySpecialization(String specialization) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Medecin> query = em.createQuery(
                    "SELECT m FROM Medecin m WHERE m.specialization = :specialization ORDER BY m.lastName", Medecin.class);
            query.setParameter("specialization", specialization);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find available doctors
     */
    public List<Medecin> findAvailable() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Medecin> query = em.createQuery(
                    "SELECT m FROM Medecin m WHERE m.isAvailable = true ORDER BY m.lastName", Medecin.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Search doctors by name
     */
    public List<Medecin> searchByName(String firstName, String lastName) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT m FROM Medecin m WHERE LOWER(m.firstName) LIKE LOWER(:firstName) " +
                    "AND LOWER(m.lastName) LIKE LOWER(:lastName) ORDER BY m.lastName";
            TypedQuery<Medecin> query = em.createQuery(jpql, Medecin.class);
            query.setParameter("firstName", "%" + (firstName != null ? firstName : "") + "%");
            query.setParameter("lastName", "%" + (lastName != null ? lastName : "") + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Count all doctors
     */
    public long countAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(m) FROM Medecin m", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Count available doctors
     */
    public long countAvailable() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(m) FROM Medecin m WHERE m.isAvailable = true", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
