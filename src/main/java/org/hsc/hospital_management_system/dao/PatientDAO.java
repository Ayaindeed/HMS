package org.hsc.hospital_management_system.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hsc.hospital_management_system.config.JpaConfig;
import org.hsc.hospital_management_system.entity.Patient;
import java.util.List;

/**
 * DAO for Patient entity
 */
public class PatientDAO extends GenericDAO<Patient> {
    public PatientDAO() {
        super(Patient.class);
    }

    /**
     * Find patient by email
     */
    public Patient findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Patient> query = em.createQuery(
                    "SELECT p FROM Patient p WHERE p.email = :email", Patient.class);
            query.setParameter("email", email);
            List<Patient> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Find patients by city
     */
    public List<Patient> findByCity(String city) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Patient> query = em.createQuery(
                    "SELECT p FROM Patient p WHERE p.city = :city ORDER BY p.lastName", Patient.class);
            query.setParameter("city", city);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find active patients
     */
    public List<Patient> findActivePatients() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Patient> query = em.createQuery(
                    "SELECT p FROM Patient p WHERE p.isActive = true ORDER BY p.lastName", Patient.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find patients by blood type
     */
    public List<Patient> findByBloodType(String bloodType) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Patient> query = em.createQuery(
                    "SELECT p FROM Patient p WHERE p.bloodType = :bloodType", Patient.class);
            query.setParameter("bloodType", bloodType);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find patients by insurance
     */
    public List<Patient> findByInsurance(String insuranceNumber) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Patient> query = em.createQuery(
                    "SELECT p FROM Patient p WHERE p.insuranceNumber = :insuranceNumber", Patient.class);
            query.setParameter("insuranceNumber", insuranceNumber);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Search patients by name
     */
    public List<Patient> searchByName(String firstName, String lastName) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE LOWER(:firstName) " +
                    "AND LOWER(p.lastName) LIKE LOWER(:lastName) ORDER BY p.lastName";
            TypedQuery<Patient> query = em.createQuery(jpql, Patient.class);
            query.setParameter("firstName", "%" + firstName + "%");
            query.setParameter("lastName", "%" + lastName + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
