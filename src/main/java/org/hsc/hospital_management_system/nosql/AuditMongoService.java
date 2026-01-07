package org.hsc.hospital_management_system.nosql;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.hsc.hospital_management_system.config.MongoConfig;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MongoDB service for audit logging and non-structured data
 */
@ApplicationScoped
public class AuditMongoService {
    private MongoDatabase database;
    private MongoCollection<Document> auditCollection;

    public AuditMongoService() {
        try {
            this.database = MongoConfig.getDatabase();
            this.auditCollection = database.getCollection("audit_logs");
        } catch (Exception e) {
            System.err.println("Error initializing MongoDB: " + e.getMessage());
        }
    }

    /**
     * Log an action
     */
    public void logAction(String entityType, String action, String entityId, String details, String userId) {
        try {
            if (auditCollection == null) {
                System.err.println("Audit collection not initialized");
                return;
            }

            Document log = new Document()
                    .append("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .append("entityType", entityType)
                    .append("action", action)
                    .append("entityId", entityId)
                    .append("details", details)
                    .append("userId", userId);

            auditCollection.insertOne(log);
        } catch (Exception e) {
            System.err.println("Error logging action: " + e.getMessage());
        }
    }

    /**
     * Get audit logs for an entity
     */
    public void getAuditLogsForEntity(String entityType, String entityId) {
        try {
            if (auditCollection == null) {
                System.err.println("Audit collection not initialized");
                return;
            }

            var logs = auditCollection.find(
                    new Document("entityType", entityType)
                            .append("entityId", entityId)
            ).into(new java.util.ArrayList<>());

            System.out.println("Found " + logs.size() + " audit logs");
        } catch (Exception e) {
            System.err.println("Error retrieving audit logs: " + e.getMessage());
        }
    }

    /**
     * Log patient creation
     */
    public void logPatientCreated(String patientId, String firstName, String lastName) {
        logAction("Patient", "CREATE", patientId, 
                "Created new patient: " + firstName + " " + lastName, "system");
    }

    /**
     * Log patient update
     */
    public void logPatientUpdated(String patientId, String updates) {
        logAction("Patient", "UPDATE", patientId, "Updated patient: " + updates, "system");
    }

    /**
     * Log appointment creation
     */
    public void logAppointmentCreated(String appointmentId, String patientId, String medecinId) {
        logAction("Appointment", "CREATE", appointmentId,
                "Created appointment for patient: " + patientId + " with doctor: " + medecinId, "system");
    }

    /**
     * Log appointment status change
     */
    public void logAppointmentStatusChanged(String appointmentId, String oldStatus, String newStatus) {
        logAction("Appointment", "STATUS_CHANGE", appointmentId,
                "Status changed from " + oldStatus + " to " + newStatus, "system");
    }

    /**
     * Get recent logs
     */
    public void getRecentLogs(int limit) {
        try {
            if (auditCollection == null) {
                System.err.println("Audit collection not initialized");
                return;
            }

            var logs = auditCollection.find()
                    .sort(new Document("_id", -1))
                    .limit(limit)
                    .into(new java.util.ArrayList<>());

            System.out.println("Retrieved " + logs.size() + " recent logs");
        } catch (Exception e) {
            System.err.println("Error retrieving recent logs: " + e.getMessage());
        }
    }

    /**
     * Clear old audit logs (older than specified days)
     */
    public void clearOldLogs(int daysOld) {
        try {
            if (auditCollection == null) {
                System.err.println("Audit collection not initialized");
                return;
            }

            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
            String cutoffString = cutoffDate.format(DateTimeFormatter.ISO_DATE_TIME);

            var result = auditCollection.deleteMany(
                    new Document("timestamp", new Document("$lt", cutoffString))
            );

            System.out.println("Deleted " + result.getDeletedCount() + " old audit logs");
        } catch (Exception e) {
            System.err.println("Error clearing old logs: " + e.getMessage());
        }
    }
}
