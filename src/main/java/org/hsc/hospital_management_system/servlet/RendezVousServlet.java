package org.hsc.hospital_management_system.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hsc.hospital_management_system.service.RendezVousService;
import org.hsc.hospital_management_system.service.PatientService;
import org.hsc.hospital_management_system.service.MedecinService;
import org.hsc.hospital_management_system.entity.RendezVous;
import org.hsc.hospital_management_system.entity.Patient;
import org.hsc.hospital_management_system.entity.Medecin;
import org.hsc.hospital_management_system.dao.PatientDAO;
import org.hsc.hospital_management_system.dao.MedecinDAO;
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
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Servlet for handling RendezVous (Appointment) CRUD operations
 */
@WebServlet("/api/rendezvous")
public class RendezVousServlet extends HttpServlet {
    @Inject
    private RendezVousService rendezVousService;
    @Inject
    private MedecinDAO medecinDAO;
    @Inject
    private PatientDAO patientDAO;
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
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        
        try {
            if ("patient".equals(action)) {
                Long patientId = Long.parseLong(request.getParameter("patientId"));
                List<RendezVous> appointments = rendezVousService.getPatientAppointments(patientId);
                response.getWriter().write(convertAppointmentsToJson(appointments));
            } else if ("doctor".equals(action)) {
                Long medecinId = Long.parseLong(request.getParameter("medecinId"));
                List<RendezVous> appointments = rendezVousService.getDoctorAppointments(medecinId);
                response.getWriter().write(convertAppointmentsToJson(appointments));
            } else if ("scheduled".equals(action)) {
                List<RendezVous> appointments = rendezVousService.getScheduledAppointments();
                response.getWriter().write(convertAppointmentsToJson(appointments));
            } else if ("completed".equals(action)) {
                List<RendezVous> appointments = rendezVousService.getCompletedAppointments();
                response.getWriter().write(convertAppointmentsToJson(appointments));
            } else if ("today".equals(action)) {
                List<RendezVous> appointments = rendezVousService.getTodayAppointments();
                response.getWriter().write(convertAppointmentsToJson(appointments));
            } else if ("stats".equals(action)) {
                RendezVousService.AppointmentStats stats = rendezVousService.getAppointmentStats();
                response.getWriter().write(gson.toJson(stats));
            } else if ("all".equals(action) || action == null) {
                // Return all appointments
                List<RendezVous> appointments = rendezVousService.getScheduledAppointments();
                appointments.addAll(rendezVousService.getCompletedAppointments());
                appointments.addAll(rendezVousService.getCancelledAppointments());
                response.getWriter().write(convertAppointmentsToJson(appointments));
            } else {
                // Get single appointment by ID
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    Long rendezVousId = Long.parseLong(idParam);
                    RendezVous rdv = rendezVousService.getAppointmentById(rendezVousId);
                    response.getWriter().write(convertAppointmentToJson(rdv));
                } else {
                    // Default: return all appointments
                    List<RendezVous> appointments = rendezVousService.getScheduledAppointments();
                    response.getWriter().write(convertAppointmentsToJson(appointments));
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            Long patientId = Long.parseLong(request.getParameter("patientId"));
            Long medecinId = Long.parseLong(request.getParameter("medecinId"));
            LocalDate appointmentDate = LocalDate.parse(request.getParameter("appointmentDate"));
            LocalTime appointmentTime = LocalTime.parse(request.getParameter("appointmentTime"));
            String reason = request.getParameter("reason");
            
            Patient patient = patientDAO.read(patientId);
            Medecin medecin = medecinDAO.read(medecinId);
            
            if (patient == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Patient not found\"}");
                return;
            }
            
            if (medecin == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Doctor not found\"}");
                return;
            }
            
            RendezVous rdv = rendezVousService.createAppointment(patient, medecin, 
                                                                appointmentDate, appointmentTime, reason);
            response.getWriter().write(convertAppointmentToJson(rdv));
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
            Long rendezVousId = Long.parseLong(request.getParameter("id"));
            String action = request.getParameter("action");
            
            if ("cancel".equals(action)) {
                RendezVous rdv = rendezVousService.cancelAppointment(rendezVousId);
                response.getWriter().write(convertAppointmentToJson(rdv));
            } else if ("complete".equals(action)) {
                String notes = request.getParameter("notes");
                RendezVous rdv = rendezVousService.completeAppointment(rendezVousId, notes);
                response.getWriter().write(convertAppointmentToJson(rdv));
            } else {
                // Generic update
                RendezVous rdv = rendezVousService.getAppointmentById(rendezVousId);
                if (rdv != null) {
                    String reason = request.getParameter("reason");
                    if (reason != null) rdv.setReason(reason);
                    
                    rdv = rendezVousService.updateAppointment(rdv);
                    response.getWriter().write(convertAppointmentToJson(rdv));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Appointment not found\"}");
                }
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
            Long rendezVousId = Long.parseLong(request.getParameter("id"));
            rendezVousService.deleteAppointment(rendezVousId);
            response.getWriter().write("{\"success\": true, \"message\": \"Appointment deleted successfully\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Convert appointment to JSON (avoiding circular references)
     */
    private String convertAppointmentToJson(RendezVous rdv) {
        if (rdv == null) return "null";
        Map<String, Object> map = new HashMap<>();
        map.put("rendezvousId", rdv.getRendezVousId());
        
        // Use safe access for lazy-loaded objects
        try {
            Long patientId = null;
            String patientName = null;
            if (rdv.getPatient() != null && rdv.getPatient().getPatientId() != null) {
                patientId = rdv.getPatient().getPatientId();
                patientName = rdv.getPatient().getFirstName() + " " + rdv.getPatient().getLastName();
            }
            map.put("patientId", patientId);
            map.put("patientName", patientName);
        } catch (Exception e) {
            map.put("patientId", null);
            map.put("patientName", null);
        }
        
        try {
            Long medecinId = null;
            String doctorName = null;
            if (rdv.getMedecin() != null && rdv.getMedecin().getMedecinId() != null) {
                medecinId = rdv.getMedecin().getMedecinId();
                doctorName = "Dr. " + rdv.getMedecin().getFirstName() + " " + rdv.getMedecin().getLastName();
            }
            map.put("medecinId", medecinId);
            map.put("doctorName", doctorName);
        } catch (Exception e) {
            map.put("medecinId", null);
            map.put("doctorName", null);
        }
        
        map.put("appointmentDate", rdv.getAppointmentDate() != null ? rdv.getAppointmentDate().toString() : null);
        map.put("appointmentTime", rdv.getAppointmentTime() != null ? rdv.getAppointmentTime().toString() : null);
        map.put("status", rdv.getStatus());
        map.put("reason", rdv.getReason());
        map.put("notes", rdv.getNotes());
        return gson.toJson(map);
    }

    /**
     * Convert list of appointments to JSON
     */
    private String convertAppointmentsToJson(List<RendezVous> appointments) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RendezVous rdv : appointments) {
            Map<String, Object> map = new HashMap<>();
            map.put("rendezvousId", rdv.getRendezVousId());
            
            // Use safe access for lazy-loaded objects
            try {
                Long patientId = null;
                String patientName = null;
                if (rdv.getPatient() != null && rdv.getPatient().getPatientId() != null) {
                    patientId = rdv.getPatient().getPatientId();
                    patientName = rdv.getPatient().getFirstName() + " " + rdv.getPatient().getLastName();
                }
                map.put("patientId", patientId);
                map.put("patientName", patientName);
            } catch (Exception e) {
                map.put("patientId", null);
                map.put("patientName", null);
            }
            
            try {
                Long medecinId = null;
                String doctorName = null;
                if (rdv.getMedecin() != null && rdv.getMedecin().getMedecinId() != null) {
                    medecinId = rdv.getMedecin().getMedecinId();
                    doctorName = "Dr. " + rdv.getMedecin().getFirstName() + " " + rdv.getMedecin().getLastName();
                }
                map.put("medecinId", medecinId);
                map.put("doctorName", doctorName);
            } catch (Exception e) {
                map.put("medecinId", null);
                map.put("doctorName", null);
            }
            
            map.put("appointmentDate", rdv.getAppointmentDate() != null ? rdv.getAppointmentDate().toString() : null);
            map.put("appointmentTime", rdv.getAppointmentTime() != null ? rdv.getAppointmentTime().toString() : null);
            map.put("status", rdv.getStatus());
            map.put("reason", rdv.getReason());
            list.add(map);
        }
        return gson.toJson(list);
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
