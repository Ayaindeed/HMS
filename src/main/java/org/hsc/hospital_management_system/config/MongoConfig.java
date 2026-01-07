package org.hsc.hospital_management_system.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB Configuration class for managing connections
 */
public class MongoConfig {
    private static final String DATABASE_NAME = "hospital_logs";
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    static {
        try {
            // MongoDB connection string (adjust for Docker environment)
            String mongoUri = System.getenv("MONGO_URI");
            if (mongoUri == null || mongoUri.isEmpty()) {
                mongoUri = "mongodb://admin:admin@localhost:27017/hospital_logs?authSource=admin";
            }
            mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase(DATABASE_NAME);
            
            // Test connection by listing collections
            database.listCollectionNames().first();
            System.out.println("MongoDB connected successfully to database: " + DATABASE_NAME);
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            String mongoUri = System.getenv("MONGO_URI");
            if (mongoUri == null || mongoUri.isEmpty()) {
                mongoUri = "mongodb://admin:admin@localhost:27017/hospital_logs?authSource=admin";
            }
            mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase(DATABASE_NAME);
        }
        return database;
    }

    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            String mongoUri = System.getenv("MONGO_URI");
            if (mongoUri == null || mongoUri.isEmpty()) {
                mongoUri = "mongodb://admin:admin@localhost:27017/hospital_logs?authSource=admin";
            }
            mongoClient = MongoClients.create(mongoUri);
        }
        return mongoClient;
    }

    public static void shutdown() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
