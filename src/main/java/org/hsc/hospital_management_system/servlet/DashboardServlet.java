package org.hsc.hospital_management_system.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hsc.hospital_management_system.service.DashboardService;
import jakarta.inject.Inject;
import com.google.gson.Gson;
import java.io.IOException;

/**
 * Servlet for providing dashboard statistics and analytics
 */
@WebServlet("/api/dashboard")
public class DashboardServlet extends HttpServlet {
    @Inject
    private DashboardService dashboardService;
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
        String action = request.getParameter("action");
        
        try {
            if ("stats".equals(action)) {
                DashboardService.DashboardStats stats = dashboardService.getOverallStats();
                response.getWriter().write(gson.toJson(stats));
            } else if ("trends".equals(action)) {
                DashboardService.AppointmentTrends trends = dashboardService.getAppointmentTrends();
                response.getWriter().write(gson.toJson(trends));
            } else if ("demographics".equals(action)) {
                DashboardService.PatientDemographics demographics = dashboardService.getPatientDemographics();
                response.getWriter().write(gson.toJson(demographics));
            } else {
                // Default: return all dashboard data
                DashboardData data = new DashboardData();
                data.stats = dashboardService.getOverallStats();
                data.trends = dashboardService.getAppointmentTrends();
                data.demographics = dashboardService.getPatientDemographics();
                response.getWriter().write(gson.toJson(data));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    private static class DashboardData {
        public DashboardService.DashboardStats stats;
        public DashboardService.AppointmentTrends trends;
        public DashboardService.PatientDemographics demographics;
    }

    private static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}
