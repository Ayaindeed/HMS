package org.hsc.hospital_management_system.service;

import org.hsc.hospital_management_system.dao.MedecinDAO;
import org.hsc.hospital_management_system.entity.Medecin;
import org.hsc.hospital_management_system.entity.ServiceHospitalier;
import org.hsc.hospital_management_system.dao.GenericDAO;
import jakarta.persistence.EntityManager;
import org.hsc.hospital_management_system.config.JpaConfig;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Service layer for Medecin (Doctor) operations
 */
@ApplicationScoped
public class MedecinService {
    private MedecinDAO medecinDAO = new MedecinDAO();

    /**
     * Get all doctors
     */
    public List<Medecin> getAllDoctors() {
        return medecinDAO.findAll();
    }

    /**
     * Get doctor by ID
     */
    public Medecin getDoctorById(Long id) {
        return medecinDAO.read(id);
    }

    /**
     * Get doctor by email
     */
    public Medecin getDoctorByEmail(String email) {
        return medecinDAO.findByEmail(email);
    }

    /**
     * Get doctors by specialization
     */
    public List<Medecin> getDoctorsBySpecialization(String specialization) {
        return medecinDAO.findBySpecialization(specialization);
    }

    /**
     * Get available doctors
     */
    public List<Medecin> getAvailableDoctors() {
        return medecinDAO.findAvailable();
    }

    /**
     * Search doctors by name
     */
    public List<Medecin> searchDoctors(String firstName, String lastName) {
        return medecinDAO.searchByName(firstName, lastName);
    }

    /**
     * Create a new doctor
     */
    public Medecin createDoctor(String firstName, String lastName, String specialization, 
                                String licenseNumber, Long serviceId) {
        // Get service
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        ServiceHospitalier service;
        try {
            service = em.find(ServiceHospitalier.class, serviceId);
            if (service == null) {
                // Use default service (id=1) if not found
                service = em.find(ServiceHospitalier.class, 1L);
            }
        } finally {
            em.close();
        }
        
        Medecin doctor = new Medecin(firstName, lastName, specialization, licenseNumber);
        doctor.setService(service);
        doctor.setIsAvailable(true);
        return medecinDAO.create(doctor);
    }

    /**
     * Update doctor
     */
    public Medecin updateDoctor(Medecin doctor) {
        // Reload the entity from database to avoid lazy loading issues
        Medecin existing = medecinDAO.read(doctor.getMedecinId());
        if (existing == null) {
            throw new RuntimeException("Doctor not found");
        }
        
        // Update only the fields that were provided
        existing.setFirstName(doctor.getFirstName());
        existing.setLastName(doctor.getLastName());
        existing.setSpecialization(doctor.getSpecialization());
        existing.setEmail(doctor.getEmail());
        existing.setPhone(doctor.getPhone());
        existing.setOfficeHours(doctor.getOfficeHours());
        existing.setIsAvailable(doctor.getIsAvailable());
        
        // Update service if changed
        if (doctor.getService() != null) {
            existing.setService(doctor.getService());
        }
        
        return medecinDAO.update(existing);
    }

    /**
     * Delete doctor
     */
    public void deleteDoctor(Long id) {
        medecinDAO.delete(id);
    }

    /**
     * Toggle doctor availability
     */
    public Medecin toggleAvailability(Long id) {
        Medecin doctor = medecinDAO.read(id);
        if (doctor != null) {
            doctor.setIsAvailable(!doctor.getIsAvailable());
            return medecinDAO.update(doctor);
        }
        return null;
    }

    /**
     * Count all doctors
     */
    public long countAllDoctors() {
        return medecinDAO.countAll();
    }

    /**
     * Count available doctors
     */
    public long countAvailableDoctors() {
        return medecinDAO.countAvailable();
    }
}
