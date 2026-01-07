// MongoDB initialization script for Hospital Management System
// Creates the hospital_logs database with audit and cache collections

// Switch to hospital_logs database
db = db.getSiblingDB('hospital_logs');

// Authenticate if root credentials exist (for first-time setup)
try {
    db.auth('admin', 'admin');
} catch(e) {
    // Authentication might fail on first run, continue anyway
    print("Auth skipped (first run or already authenticated)");
}

// Create audit_logs collection for tracking system events
db.createCollection('audit_logs');

// Insert initial audit log entries
db.audit_logs.insertMany([
    {
        action: "SYSTEM_INIT",
        entity: "System",
        entityId: null,
        timestamp: new Date(),
        details: "Hospital Management System initialized",
        userId: "system"
    },
    {
        action: "PATIENT_VIEW",
        entity: "Patient",
        entityId: 1,
        timestamp: new Date(),
        details: "Patient record accessed: Fatima Bennani",
        userId: "admin"
    },
    {
        action: "APPOINTMENT_CREATE",
        entity: "Appointment",
        entityId: 1,
        timestamp: new Date(),
        details: "Appointment scheduled for Fatima Bennani with Dr. Ahmed Benali",
        userId: "admin"
    },
    {
        action: "PATIENT_CREATE",
        entity: "Patient",
        entityId: 2,
        timestamp: new Date(),
        details: "New patient registered: Mohammed Alaoui",
        userId: "admin"
    },
    {
        action: "DOCTOR_LOGIN",
        entity: "Doctor",
        entityId: 1,
        timestamp: new Date(),
        details: "Dr. Ahmed Benali logged into the system",
        userId: "ahmed.benali"
    }
]);

// Create cache collection for Redis-like caching
db.createCollection('cache');

// Insert sample cache entries
db.cache.insertMany([
    {
        key: "patient_count",
        value: 10,
        ttl: 3600,
        createdAt: new Date()
    },
    {
        key: "appointment_count_today",
        value: 5,
        ttl: 1800,
        createdAt: new Date()
    },
    {
        key: "doctors_available",
        value: 6,
        ttl: 300,
        createdAt: new Date()
    }
]);

// Create indexes for better query performance
db.audit_logs.createIndex({ "timestamp": -1 });
db.audit_logs.createIndex({ "action": 1 });
db.audit_logs.createIndex({ "entity": 1, "entityId": 1 });
db.cache.createIndex({ "key": 1 }, { unique: true });

print("MongoDB initialized successfully with hospital_logs database");
print("Collections created: audit_logs, cache");
