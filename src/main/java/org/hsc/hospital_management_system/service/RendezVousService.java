package org.hsc.hospital_management_system.service;

import org.hsc.hospital_management_system.dao.RendezVousDAO;
import org.hsc.hospital_management_system.dao.PatientDAO;
import org.hsc.hospital_management_system.entity.RendezVous;
import org.hsc.hospital_management_system.entity.Patient;
import org.hsc.hospital_management_system.entity.Medecin;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service for RendezVous (Appointment) operations
 */
@ApplicationScoped
public class RendezVousService {
    private RendezVousDAO rendezVousDAO = new RendezVousDAO();
    private PatientDAO patientDAO = new PatientDAO();

    /**
     * Create new appointment
     */
    public RendezVous createAppointment(Patient patient, Medecin medecin, LocalDate date, LocalTime time, String reason) {
        RendezVous rdv = new RendezVous(patient, medecin, date, time);
        rdv.setReason(reason);
        return rendezVousDAO.create(rdv);
    }

    /**
     * Update appointment
     */
    public RendezVous updateAppointment(RendezVous rendezVous) {
        return rendezVousDAO.update(rendezVous);
    }

    /**
     * Cancel appointment
     */
    public RendezVous cancelAppointment(Long rendezVousId) {
        RendezVous rdv = rendezVousDAO.read(rendezVousId);
        if (rdv != null) {
            rdv.setStatus("CANCELLED");
            return rendezVousDAO.update(rdv);
        }
        return null;
    }

    /**
     * Complete appointment
     */
    public RendezVous completeAppointment(Long rendezVousId, String notes) {
        RendezVous rdv = rendezVousDAO.read(rendezVousId);
        if (rdv != null) {
            rdv.setStatus("COMPLETED");
            rdv.setNotes(notes);
            return rendezVousDAO.update(rdv);
        }
        return null;
    }

    /**
     * Get appointment by ID
     */
    public RendezVous getAppointmentById(Long rendezVousId) {
        return rendezVousDAO.read(rendezVousId);
    }

    /**
     * Get all appointments for a patient
     */
    public List<RendezVous> getPatientAppointments(Long patientId) {
        return rendezVousDAO.findByPatientId(patientId);
    }

    /**
     * Get all appointments for a doctor
     */
    public List<RendezVous> getDoctorAppointments(Long medecinId) {
        return rendezVousDAO.findByMedecinId(medecinId);
    }

    /**
     * Get appointments by date
     */
    public List<RendezVous> getAppointmentsByDate(LocalDate date) {
        return rendezVousDAO.findByDate(date);
    }

    /**
     * Get appointments between dates
     */
    public List<RendezVous> getAppointmentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return rendezVousDAO.findBetweenDates(startDate, endDate);
    }

    /**
     * Get all scheduled appointments
     */
    public List<RendezVous> getScheduledAppointments() {
        return rendezVousDAO.findScheduledAppointments();
    }

    /**
     * Get all completed appointments
     */
    public List<RendezVous> getCompletedAppointments() {
        return rendezVousDAO.findCompletedAppointments();
    }

    /**
     * Get all cancelled appointments
     */
    public List<RendezVous> getCancelledAppointments() {
        return rendezVousDAO.findCancelledAppointments();
    }

    /**
     * Delete appointment
     */
    public void deleteAppointment(Long rendezVousId) {
        rendezVousDAO.delete(rendezVousId);
    }

    /**
     * Get appointment statistics
     */
    public AppointmentStats getAppointmentStats() {
        AppointmentStats stats = new AppointmentStats();
        stats.setScheduledCount(rendezVousDAO.countByStatus("SCHEDULED"));
        stats.setCompletedCount(rendezVousDAO.countByStatus("COMPLETED"));
        stats.setCancelledCount(rendezVousDAO.countByStatus("CANCELLED"));
        stats.setTotalCount(stats.getScheduledCount() + stats.getCompletedCount() + stats.getCancelledCount());
        return stats;
    }

    /**
     * Get today's appointments
     */
    public List<RendezVous> getTodayAppointments() {
        return rendezVousDAO.findByDate(LocalDate.now());
    }

    /**
     * Statistics helper class
     */
    public static class AppointmentStats {
        private long scheduledCount;
        private long completedCount;
        private long cancelledCount;
        private long totalCount;

        public long getScheduledCount() { return scheduledCount; }
        public void setScheduledCount(long count) { this.scheduledCount = count; }

        public long getCompletedCount() { return completedCount; }
        public void setCompletedCount(long count) { this.completedCount = count; }

        public long getCancelledCount() { return cancelledCount; }
        public void setCancelledCount(long count) { this.cancelledCount = count; }

        public long getTotalCount() { return totalCount; }
        public void setTotalCount(long count) { this.totalCount = count; }

        public double getCompletionRate() {
            if (totalCount == 0) return 0;
            return ((double) completedCount / totalCount) * 100;
        }
    }
}
