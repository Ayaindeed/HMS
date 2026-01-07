package org.hsc.hospital_management_system.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hsc.hospital_management_system.service.PatientService;
import org.hsc.hospital_management_system.entity.Patient;
import org.hsc.hospital_management_system.nosql.AuditMongoService;
import jakarta.inject.Inject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Servlet for handling Patient CRUD operations
 */
@WebServlet("/api/patients")
public class PatientServlet extends HttpServlet {
    @Inject
    private PatientService patientService;
    
    @Inject
    private AuditMongoService auditService;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) -> 
                date == null ? null : context.serialize(date.toString()))
            .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (time, type, context) -> 
                time == null ? null : context.serialize(time.toString()))
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    // Exclude lazy-loaded collections to avoid LazyInitializationException
                    return f.getName().equals("rendezVousList") || f.getName().equals("dossierMedical");
                }
                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
        String action = request.getParameter("action");
        
        try {
            if ("all".equals(action)) {
                List<Patient> patients = patientService.getAllPatients();
                response.getWriter().write(gson.toJson(patients));
            } else if ("active".equals(action)) {
                List<Patient> patients = patientService.getAllActivePatients();
                response.getWriter().write(gson.toJson(patients));
            } else if ("search".equals(action)) {
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                List<Patient> patients = patientService.searchPatients(firstName != null ? firstName : "", 
                                                                      lastName != null ? lastName : "");
                response.getWriter().write(gson.toJson(patients));
            } else if ("city".equals(action)) {
                String city = request.getParameter("city");
                List<Patient> patients = patientService.getPatientsByCity(city);
                response.getWriter().write(gson.toJson(patients));
            } else if ("bloodType".equals(action)) {
                String bloodType = request.getParameter("bloodType");
                List<Patient> patients = patientService.getPatientsByBloodType(bloodType);
                response.getWriter().write(gson.toJson(patients));
            } else {
                // Check if ID is provided, otherwise return all patients
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    Long patientId = Long.parseLong(idParam);
                    Patient patient = patientService.getPatientById(patientId);
                    response.getWriter().write(gson.toJson(patient));
                } else {
                    // Default: return all patients
                    List<Patient> patients = patientService.getAllPatients();
                    response.getWriter().write(gson.toJson(patients));
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String dateOfBirth = request.getParameter("dateOfBirth");
            String gender = request.getParameter("gender");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String city = request.getParameter("city");
            String bloodType = request.getParameter("bloodType");
            String insuranceNumber = request.getParameter("insuranceNumber");
            
            // Create patient with all fields at once
            Patient patient = new Patient(firstName, lastName, LocalDate.parse(dateOfBirth), gender);
            patient.setEmail(email);
            patient.setPhone(phone);
            patient.setAddress(address);
            patient.setCity(city);
            patient.setBloodType(bloodType);
            patient.setInsuranceNumber(insuranceNumber);
            
            // Save in one transaction
            patient = patientService.createPatient(patient);
            
            // Log to MongoDB
            auditService.logAction("PATIENT", "CREATE", 
                String.valueOf(patient.getPatientId()), 
                "Created patient: " + firstName + " " + lastName, 
                "system");
            
            response.getWriter().write(gson.toJson(patient));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
        try {
            Long patientId = Long.parseLong(request.getParameter("id"));
            
            // Create a patient object with only the ID and fields to update
            Patient patient = new Patient();
            patient.setPatientId(patientId);
            
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String city = request.getParameter("city");
            String bloodType = request.getParameter("bloodType");
            
            if (firstName != null) patient.setFirstName(firstName);
            if (lastName != null) patient.setLastName(lastName);
            if (email != null) patient.setEmail(email);
            if (phone != null) patient.setPhone(phone);
            if (address != null) patient.setAddress(address);
            if (city != null) patient.setCity(city);
            if (bloodType != null) patient.setBloodType(bloodType);
            
            patient = patientService.updatePatient(patient);
            
            // Log to MongoDB
            auditService.logAction("PATIENT", "UPDATE", 
                String.valueOf(patientId), 
                "Updated patient fields", 
                "system");
            
            response.getWriter().write(gson.toJson(patient));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
        try {
            Long patientId = Long.parseLong(request.getParameter("id"));
            patientService.deletePatient(patientId);
            
            // Log to MongoDB
            auditService.logAction("PATIENT", "DELETE", 
                String.valueOf(patientId), 
                "Deleted patient", 
                "system");
            
            response.getWriter().write(gson.toJson(new SuccessResponse("Patient deleted successfully")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    private static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }

    private static class SuccessResponse {
        public String message;
        public SuccessResponse(String message) { this.message = message; }
    }
}
