package org.hsc.hospital_management_system.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hsc.hospital_management_system.config.JpaConfig;
import org.hsc.hospital_management_system.entity.RendezVous;
import java.time.LocalDate;
import java.util.List;

/**
 * DAO for RendezVous entity
 */
public class RendezVousDAO extends GenericDAO<RendezVous> {
    public RendezVousDAO() {
        super(RendezVous.class);
    }

    /**
     * Find all appointments for a patient
     */
    public List<RendezVous> findByPatientId(Long patientId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RendezVous> query = em.createQuery(
                    "SELECT r FROM RendezVous r WHERE r.patient.patientId = :patientId ORDER BY r.appointmentDate DESC",
                    RendezVous.class);
            query.setParameter("patientId", patientId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find all appointments for a doctor
     */
    public List<RendezVous> findByMedecinId(Long medecinId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RendezVous> query = em.createQuery(
                    "SELECT r FROM RendezVous r WHERE r.medecin.medecinId = :medecinId ORDER BY r.appointmentDate DESC",
                    RendezVous.class);
            query.setParameter("medecinId", medecinId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find appointments by date
     */
    public List<RendezVous> findByDate(LocalDate date) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RendezVous> query = em.createQuery(
                    "SELECT r FROM RendezVous r WHERE r.appointmentDate = :date ORDER BY r.appointmentTime",
                    RendezVous.class);
            query.setParameter("date", date);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find appointments between dates
     */
    public List<RendezVous> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RendezVous> query = em.createQuery(
                    "SELECT r FROM RendezVous r WHERE r.appointmentDate BETWEEN :startDate AND :endDate " +
                            "ORDER BY r.appointmentDate, r.appointmentTime",
                    RendezVous.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find scheduled appointments
     */
    public List<RendezVous> findScheduledAppointments() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RendezVous> query = em.createQuery(
                    "SELECT r FROM RendezVous r WHERE r.status = 'SCHEDULED' AND r.appointmentDate >= CURRENT_DATE " +
                            "ORDER BY r.appointmentDate, r.appointmentTime",
                    RendezVous.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find completed appointments
     */
    public List<RendezVous> findCompletedAppointments() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RendezVous> query = em.createQuery(
                    "SELECT r FROM RendezVous r WHERE r.status = 'COMPLETED' ORDER BY r.appointmentDate DESC",
                    RendezVous.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find cancelled appointments
     */
    public List<RendezVous> findCancelledAppointments() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RendezVous> query = em.createQuery(
                    "SELECT r FROM RendezVous r WHERE r.status = 'CANCELLED' ORDER BY r.appointmentDate DESC",
                    RendezVous.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Count appointments by status
     */
    public long countByStatus(String status) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(r) FROM RendezVous r WHERE r.status = :status", Long.class)
                    .setParameter("status", status)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
