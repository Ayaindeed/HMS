package org.hsc.hospital_management_system.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA Configuration class for managing EntityManagerFactory
 */
public class JpaConfig {
    private static EntityManagerFactory emf;

    static {
        try {
            Map<String, String> properties = new HashMap<>();
            
            // Read environment variables with defaults
            String dbHost = getEnv("DB_HOST", "localhost");
            String dbPort = getEnv("DB_PORT", "5432");
            String dbName = getEnv("DB_NAME", "hospital_db");
            String dbUser = getEnv("DB_USER", "postgres");
            String dbPassword = getEnv("DB_PASSWORD", "postgres");
            
            String jdbcUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
            
            properties.put("jakarta.persistence.jdbc.url", jdbcUrl);
            properties.put("jakarta.persistence.jdbc.user", dbUser);
            properties.put("jakarta.persistence.jdbc.password", dbPassword);
            
            System.out.println("Connecting to database: " + jdbcUrl);
            
            emf = Persistence.createEntityManagerFactory("hospital-pu", properties);
            System.out.println("EntityManagerFactory created successfully");
        } catch (Exception e) {
            System.err.println("Error creating EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            Map<String, String> properties = new HashMap<>();
            String dbHost = getEnv("DB_HOST", "localhost");
            String dbPort = getEnv("DB_PORT", "5432");
            String dbName = getEnv("DB_NAME", "hospital_db");
            String dbUser = getEnv("DB_USER", "postgres");
            String dbPassword = getEnv("DB_PASSWORD", "postgres");
            
            String jdbcUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
            properties.put("jakarta.persistence.jdbc.url", jdbcUrl);
            properties.put("jakarta.persistence.jdbc.user", dbUser);
            properties.put("jakarta.persistence.jdbc.password", dbPassword);
            
            emf = Persistence.createEntityManagerFactory("hospital-pu", properties);
        }
        return emf;
    }

    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
