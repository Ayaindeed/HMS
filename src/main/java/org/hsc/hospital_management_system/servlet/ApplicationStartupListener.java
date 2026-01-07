package org.hsc.hospital_management_system.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hsc.hospital_management_system.service.PatientService;
import org.hsc.hospital_management_system.config.JpaConfig;

/**
 * Application startup listener to initialize mock data
 * NOTE: Disabled @WebListener to prevent startup errors due to DB not ready
 * Mock data will be initialized on first servlet access instead
 */
// @WebListener
public class ApplicationStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("========================================");
        System.out.println("Hospital Management System Starting...");
        System.out.println("========================================");

        try {
            // Initialize mock patient data
            PatientService patientService = new PatientService();
            patientService.initializeMockData();
            
            System.out.println("✓ Mock data initialization completed");
            System.out.println("✓ Application ready at /hospital_management_system");
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("✗ Error during startup initialization:");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Hospital Management System shutting down...");
        
        // Cleanup resources
        try {
            JpaConfig.shutdown();
            System.out.println("✓ JPA resources cleaned up");
        } catch (Exception e) {
            System.err.println("Error cleaning up JPA resources: " + e.getMessage());
        }
    }
}
