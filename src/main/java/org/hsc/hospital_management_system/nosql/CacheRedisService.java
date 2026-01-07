package org.hsc.hospital_management_system.nosql;

import org.hsc.hospital_management_system.config.RedisConfig;
import redis.clients.jedis.Jedis;
import java.util.Set;

/**
 * Redis service for caching and real-time statistics
 */
public class CacheRedisService {

    /**
     * Set a cache entry with expiration
     */
    public void setCache(String key, String value, int expirationSeconds) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            jedis.setex(key, expirationSeconds, value);
        } catch (Exception e) {
            System.err.println("Error setting cache: " + e.getMessage());
        }
    }

    /**
     * Get a cache entry
     */
    public String getCache(String key) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            return jedis.get(key);
        } catch (Exception e) {
            System.err.println("Error getting cache: " + e.getMessage());
            return null;
        }
    }

    /**
     * Delete a cache entry
     */
    public void deleteCache(String key) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            jedis.del(key);
        } catch (Exception e) {
            System.err.println("Error deleting cache: " + e.getMessage());
        }
    }

    /**
     * Clear all cache
     */
    public void clearAllCache() {
        try (Jedis jedis = RedisConfig.getJedis()) {
            jedis.flushDB();
        } catch (Exception e) {
            System.err.println("Error clearing cache: " + e.getMessage());
        }
    }

    /**
     * Check if key exists in cache
     */
    public boolean cacheExists(String key) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            return jedis.exists(key);
        } catch (Exception e) {
            System.err.println("Error checking cache existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Increment a counter
     */
    public long incrementCounter(String key, int increment) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            return jedis.incrBy(key, increment);
        } catch (Exception e) {
            System.err.println("Error incrementing counter: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Get counter value
     */
    public long getCounter(String key) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            String value = jedis.get(key);
            return value != null ? Long.parseLong(value) : 0;
        } catch (Exception e) {
            System.err.println("Error getting counter: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Add to set
     */
    public void addToSet(String key, String... members) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            jedis.sadd(key, members);
        } catch (Exception e) {
            System.err.println("Error adding to set: " + e.getMessage());
        }
    }

    /**
     * Get set members
     */
    public Set<String> getSetMembers(String key) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            return jedis.smembers(key);
        } catch (Exception e) {
            System.err.println("Error getting set members: " + e.getMessage());
            return null;
        }
    }

    /**
     * Cache statistics
     */
    public void cacheStatistics(String statsKey, String statsData, int expirationSeconds) {
        setCache(statsKey, statsData, expirationSeconds);
    }

    /**
     * Get cached statistics
     */
    public String getCachedStatistics(String statsKey) {
        return getCache(statsKey);
    }

    /**
     * Track active users
     */
    public void trackActiveUser(String userId, int sessionDurationSeconds) {
        String key = "active_user:" + userId;
        setCache(key, userId, sessionDurationSeconds);
        addToSet("active_users", userId);
    }

    /**
     * Get active users count
     */
    public long getActiveUsersCount() {
        try (Jedis jedis = RedisConfig.getJedis()) {
            return jedis.scard("active_users");
        } catch (Exception e) {
            System.err.println("Error getting active users count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Store appointment data
     */
    public void cacheAppointmentData(Long appointmentId, String appointmentData) {
        setCache("appointment:" + appointmentId, appointmentData, 86400); // Cache for 24 hours
    }

    /**
     * Retrieve appointment data
     */
    public String getCachedAppointmentData(Long appointmentId) {
        return getCache("appointment:" + appointmentId);
    }

    /**
     * Store patient data
     */
    public void cachePatientData(Long patientId, String patientData) {
        setCache("patient:" + patientId, patientData, 86400); // Cache for 24 hours
    }

    /**
     * Retrieve patient data
     */
    public String getCachedPatientData(Long patientId) {
        return getCache("patient:" + patientId);
    }
}
