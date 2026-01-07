package org.hsc.hospital_management_system.service;

import org.hsc.hospital_management_system.dao.PatientDAO;
import org.hsc.hospital_management_system.entity.Patient;
import org.hsc.hospital_management_system.entity.DossierMedical;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for Patient operations with business logic
 */
@ApplicationScoped
public class PatientService {
    private PatientDAO patientDAO = new PatientDAO();

    /**
     * Create a new patient
     */
    public Patient createPatient(String firstName, String lastName, LocalDate dateOfBirth, String gender) {
        Patient patient = new Patient(firstName, lastName, dateOfBirth, gender);
        return patientDAO.create(patient);
    }
    
    /**
     * Create a new patient with full object
     */
    public Patient createPatient(Patient patient) {
        return patientDAO.create(patient);
    }

    /**
     * Update patient information
     */
    public Patient updatePatient(Patient patient) {
        // Reload the entity from database to avoid lazy loading issues
        Patient existing = patientDAO.read(patient.getPatientId());
        if (existing == null) {
            throw new RuntimeException("Patient not found");
        }
        
        // Update only non-null fields to support partial updates
        if (patient.getFirstName() != null) existing.setFirstName(patient.getFirstName());
        if (patient.getLastName() != null) existing.setLastName(patient.getLastName());
        if (patient.getDateOfBirth() != null) existing.setDateOfBirth(patient.getDateOfBirth());
        if (patient.getGender() != null) existing.setGender(patient.getGender());
        if (patient.getEmail() != null) existing.setEmail(patient.getEmail());
        if (patient.getPhone() != null) existing.setPhone(patient.getPhone());
        if (patient.getAddress() != null) existing.setAddress(patient.getAddress());
        if (patient.getCity() != null) existing.setCity(patient.getCity());
        if (patient.getBloodType() != null) existing.setBloodType(patient.getBloodType());
        if (patient.getInsuranceNumber() != null) existing.setInsuranceNumber(patient.getInsuranceNumber());
        if (patient.getIsActive() != null) existing.setIsActive(patient.getIsActive());
        
        return patientDAO.update(existing);
    }

    /**
     * Get patient by ID
     */
    public Patient getPatientById(Long patientId) {
        return patientDAO.read(patientId);
    }

    /**
     * Get patient by email
     */
    public Patient getPatientByEmail(String email) {
        return patientDAO.findByEmail(email);
    }

    /**
     * Get all active patients
     */
    public List<Patient> getAllActivePatients() {
        return patientDAO.findActivePatients();
    }

    /**
     * Get all patients
     */
    public List<Patient> getAllPatients() {
        return patientDAO.findAll();
    }

    /**
     * Delete patient
     */
    public void deletePatient(Long patientId) {
        patientDAO.delete(patientId);
    }

    /**
     * Search patients by name
     */
    public List<Patient> searchPatients(String firstName, String lastName) {
        return patientDAO.searchByName(firstName, lastName);
    }

    /**
     * Get patients by city
     */
    public List<Patient> getPatientsByCity(String city) {
        return patientDAO.findByCity(city);
    }

    /**
     * Get patients by blood type
     */
    public List<Patient> getPatientsByBloodType(String bloodType) {
        return patientDAO.findByBloodType(bloodType);
    }

    /**
     * Get total patient count
     */
    public long getTotalPatients() {
        return patientDAO.count();
    }

    /**
     * Get active patient count
     */
    public long getActivePatientCount() {
        return patientDAO.findActivePatients().size();
    }

    /**
     * Initialize with mock data
     */
    public void initializeMockData() {
        try {
            // Check if data already exists
            if (patientDAO.count() > 0) {
                return;
            }

            // Create mock patients with Moroccan data
            String[][] mockPatients = {
                    {"Fatima", "Bennani", "1985-05-15", "Female", "fatima.bennani@email.com", "O+"},
                    {"Mohammed", "Alaoui", "1990-08-22", "Male", "mohammed.alaoui@email.com", "A-"},
                    {"Amina", "Belkasmi", "1988-03-10", "Female", "amina.belkasmi@email.com", "B+"},
                    {"Hassan", "Tafat", "1992-11-30", "Male", "hassan.tafat@email.com", "AB+"},
                    {"Noor", "Bouafia", "1987-07-25", "Female", "noor.bouafia@email.com", "O-"},
                    {"Ahmed", "Zahra", "1993-02-14", "Male", "ahmed.zahra@email.com", "A+"},
                    {"Layla", "Rachid", "1989-09-05", "Female", "layla.rachid@email.com", "B-"},
                    {"Karim", "Ouadda", "1994-12-20", "Male", "karim.ouadda@email.com", "AB-"},
                    {"Samira", "Assala", "1986-04-18", "Female", "samira.assala@email.com", "O+"},
                    {"Youssef", "Machroo", "1991-06-12", "Male", "youssef.machroo@email.com", "A+"},
            };

            for (String[] patientData : mockPatients) {
                Patient patient = new Patient(
                        patientData[0], // firstName
                        patientData[1], // lastName
                        LocalDate.parse(patientData[2]), // dateOfBirth
                        patientData[3]  // gender
                );
                patient.setEmail(patientData[4]);
                patient.setBloodType(patientData[5]);
                patient.setPhone("+212612345678");
                patient.setCity("Casablanca");
                patient.setAddress("123 Avenue Hassan II");
                patient.setInsuranceNumber("ASSOC" + System.nanoTime());
                patient = patientDAO.create(patient);

                // Create associated medical file
                DossierMedical dossier = new DossierMedical(patient);
                patient.setDossierMedical(dossier);
                patientDAO.update(patient);
            }

            System.out.println("Mock patient data initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing mock data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
