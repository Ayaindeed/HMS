package org.hsc.hospital_management_system.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hsc.hospital_management_system.service.MedecinService;
import org.hsc.hospital_management_system.entity.Medecin;
import org.hsc.hospital_management_system.nosql.AuditMongoService;
import jakarta.inject.Inject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for handling Doctor (Medecin) CRUD operations
 */
@WebServlet("/api/medecins")
public class MedecinServlet extends HttpServlet {
    @Inject
    private MedecinService medecinService;
    
    @Inject
    private AuditMongoService auditService;
    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .create();

    // Simple Gson for basic serialization
    private Gson simpleGson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        
        try {
            if ("all".equals(action) || action == null) {
                List<Medecin> doctors = medecinService.getAllDoctors();
                response.getWriter().write(convertDoctorsToJson(doctors));
            } else if ("available".equals(action)) {
                List<Medecin> doctors = medecinService.getAvailableDoctors();
                response.getWriter().write(convertDoctorsToJson(doctors));
            } else if ("specialization".equals(action)) {
                String spec = request.getParameter("specialization");
                List<Medecin> doctors = medecinService.getDoctorsBySpecialization(spec);
                response.getWriter().write(convertDoctorsToJson(doctors));
            } else if ("search".equals(action)) {
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                List<Medecin> doctors = medecinService.searchDoctors(
                        firstName != null ? firstName : "", 
                        lastName != null ? lastName : "");
                response.getWriter().write(convertDoctorsToJson(doctors));
            } else if ("count".equals(action)) {
                Map<String, Long> counts = new HashMap<>();
                counts.put("total", medecinService.countAllDoctors());
                counts.put("available", medecinService.countAvailableDoctors());
                response.getWriter().write(simpleGson.toJson(counts));
            } else if (idParam != null) {
                Long id = Long.parseLong(idParam);
                Medecin doctor = medecinService.getDoctorById(id);
                if (doctor != null) {
                    response.getWriter().write(convertDoctorToJson(doctor));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Doctor not found\"}");
                }
            } else {
                List<Medecin> doctors = medecinService.getAllDoctors();
                response.getWriter().write(convertDoctorsToJson(doctors));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String specialization = request.getParameter("specialization");
            String licenseNumber = request.getParameter("licenseNumber");
            String serviceIdStr = request.getParameter("serviceId");
            
            // Default to service 1 if not provided
            Long serviceId = 1L;
            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                serviceId = Long.parseLong(serviceIdStr);
            }
            
            // Map specialization to service ID
            if (specialization != null) {
                serviceId = mapSpecializationToServiceId(specialization);
            }
            
            Medecin doctor = medecinService.createDoctor(firstName, lastName, specialization, licenseNumber, serviceId);
            
            // Set optional fields
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String officeHours = request.getParameter("officeHours");
            
            if (email != null && !email.isEmpty()) doctor.setEmail(email);
            if (phone != null && !phone.isEmpty()) doctor.setPhone(phone);
            if (officeHours != null && !officeHours.isEmpty()) doctor.setOfficeHours(officeHours);
            
            doctor = medecinService.updateDoctor(doctor);
            
            // Log to MongoDB
            auditService.logAction("DOCTOR", "CREATE", 
                String.valueOf(doctor.getMedecinId()), 
                "Created doctor: " + firstName + " " + lastName + " - " + specialization, 
                "system");
            
            response.getWriter().write(convertDoctorToJson(doctor));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String idParam = request.getParameter("id");
            String action = request.getParameter("action");
            
            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"ID is required\"}");
                return;
            }
            
            Long id = Long.parseLong(idParam);
            
            if ("toggle".equals(action)) {
                Medecin doctor = medecinService.toggleAvailability(id);
                response.getWriter().write(convertDoctorToJson(doctor));
            } else {
                // Regular update
                Medecin doctor = medecinService.getDoctorById(id);
                if (doctor == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Doctor not found\"}");
                    return;
                }
                
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String specialization = request.getParameter("specialization");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String officeHours = request.getParameter("officeHours");
                
                if (firstName != null && !firstName.isEmpty()) doctor.setFirstName(firstName);
                if (lastName != null && !lastName.isEmpty()) doctor.setLastName(lastName);
                if (specialization != null && !specialization.isEmpty()) doctor.setSpecialization(specialization);
                if (email != null) doctor.setEmail(email);
                if (phone != null) doctor.setPhone(phone);
                if (officeHours != null) doctor.setOfficeHours(officeHours);
                
                doctor = medecinService.updateDoctor(doctor);
                
                // Log to MongoDB
                auditService.logAction("DOCTOR", "UPDATE", 
                    String.valueOf(id), 
                    "Updated doctor fields", 
                    "system");
                
                response.getWriter().write(convertDoctorToJson(doctor));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"ID is required\"}");
                return;
            }
            
            Long id = Long.parseLong(idParam);
            medecinService.deleteDoctor(id);
            
            // Log to MongoDB
            auditService.logAction("DOCTOR", "DELETE", 
                String.valueOf(id), 
                "Deleted doctor", 
                "system");
            
            response.getWriter().write("{\"success\": true, \"message\": \"Doctor deleted successfully\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Map specialization to service ID
     */
    private Long mapSpecializationToServiceId(String specialization) {
        switch (specialization) {
            case "Cardiology": return 1L;
            case "Pediatrics": return 2L;
            case "Surgery": return 3L;
            case "General Practice": return 4L;
            case "Neurology": return 5L;
            case "Dermatology": return 6L;
            case "Orthopedics": return 7L;
            case "Psychiatry": return 8L;
            default: return 4L; // Default to General Practice
        }
    }

    /**
     * Convert doctor to JSON (avoiding circular references)
     */
    private String convertDoctorToJson(Medecin doctor) {
        Map<String, Object> map = new HashMap<>();
        map.put("medecinId", doctor.getMedecinId());
        map.put("firstName", doctor.getFirstName());
        map.put("lastName", doctor.getLastName());
        map.put("specialization", doctor.getSpecialization());
        map.put("licenseNumber", doctor.getLicenseNumber());
        map.put("email", doctor.getEmail());
        map.put("phone", doctor.getPhone());
        map.put("officeHours", doctor.getOfficeHours());
        map.put("isAvailable", doctor.getIsAvailable());
        return simpleGson.toJson(map);
    }

    /**
     * Convert list of doctors to JSON
     */
    private String convertDoctorsToJson(List<Medecin> doctors) {
        return simpleGson.toJson(doctors.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("medecinId", d.getMedecinId());
            map.put("firstName", d.getFirstName());
            map.put("lastName", d.getLastName());
            map.put("specialization", d.getSpecialization());
            map.put("licenseNumber", d.getLicenseNumber());
            map.put("email", d.getEmail());
            map.put("phone", d.getPhone());
            map.put("officeHours", d.getOfficeHours());
            map.put("isAvailable", d.getIsAvailable());
            return map;
        }).toList());
    }
}
