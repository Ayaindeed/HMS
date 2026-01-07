package org.hsc.hospital_management_system.service;

import org.hsc.hospital_management_system.config.RedisConfig;
import redis.clients.jedis.Jedis;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for providing dashboard statistics and analytics
 * Uses Redis for caching real-time data
 */
@ApplicationScoped
public class DashboardService {
    private PatientService patientService = new PatientService();
    private RendezVousService rendezVousService = new RendezVousService();

    /**
     * Get overall hospital statistics
     */
    public DashboardStats getOverallStats() {
        DashboardStats stats = new DashboardStats();
        
        // Get patient stats
        stats.setTotalPatients(patientService.getTotalPatients());
        stats.setActivePatients(patientService.getActivePatientCount());
        
        // Get appointment stats
        RendezVousService.AppointmentStats apptStats = rendezVousService.getAppointmentStats();
        stats.setTotalAppointments(apptStats.getTotalCount());
        stats.setScheduledAppointments(apptStats.getScheduledCount());
        stats.setCompletedAppointments(apptStats.getCompletedCount());
        stats.setCancelledAppointments(apptStats.getCancelledCount());
        stats.setCompletionRate(apptStats.getCompletionRate());
        
        // Cache in Redis
        cacheStats(stats);
        
        return stats;
    }

    /**
     * Get appointment trends
     */
    public AppointmentTrends getAppointmentTrends() {
        AppointmentTrends trends = new AppointmentTrends();
        LocalDate today = LocalDate.now();
        
        // Get appointments for the past 30 days
        for (int i = 29; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            long count = rendezVousService.getAppointmentsByDate(date).size();
            trends.addDayCount(date, count);
        }
        
        return trends;
    }

    /**
     * Get patient demographics
     */
    public PatientDemographics getPatientDemographics() {
        PatientDemographics demographics = new PatientDemographics();
        var allPatients = patientService.getAllPatients();
        
        // Count by gender
        long maleCount = allPatients.stream().filter(p -> "Male".equals(p.getGender())).count();
        long femaleCount = allPatients.stream().filter(p -> "Female".equals(p.getGender())).count();
        demographics.setMalePatients(maleCount);
        demographics.setFemalePatients(femaleCount);
        
        // Count by blood type
        Map<String, Long> bloodTypeDistribution = new HashMap<>();
        allPatients.forEach(p -> {
            if (p.getBloodType() != null) {
                bloodTypeDistribution.put(p.getBloodType(),
                        bloodTypeDistribution.getOrDefault(p.getBloodType(), 0L) + 1);
            }
        });
        demographics.setBloodTypeDistribution(bloodTypeDistribution);
        
        // Count by city
        Map<String, Long> cityDistribution = new HashMap<>();
        allPatients.forEach(p -> {
            if (p.getCity() != null) {
                cityDistribution.put(p.getCity(),
                        cityDistribution.getOrDefault(p.getCity(), 0L) + 1);
            }
        });
        demographics.setCityDistribution(cityDistribution);
        
        return demographics;
    }

    /**
     * Cache statistics in Redis
     */
    private void cacheStats(DashboardStats stats) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            String key = "dashboard_stats_" + LocalDate.now();
            jedis.setex(key, 3600, stats.toString()); // Cache for 1 hour
        } catch (Exception e) {
            System.err.println("Error caching stats to Redis: " + e.getMessage());
        }
    }

    /**
     * Get cached statistics from Redis
     */
    public String getCachedStats() {
        try (Jedis jedis = RedisConfig.getJedis()) {
            String key = "dashboard_stats_" + LocalDate.now();
            return jedis.get(key);
        } catch (Exception e) {
            System.err.println("Error retrieving cached stats: " + e.getMessage());
            return null;
        }
    }

    /**
     * Dashboard Statistics class
     */
    public static class DashboardStats {
        private long totalPatients;
        private long activePatients;
        private long totalAppointments;
        private long scheduledAppointments;
        private long completedAppointments;
        private long cancelledAppointments;
        private double completionRate;

        // Getters and Setters
        public long getTotalPatients() { return totalPatients; }
        public void setTotalPatients(long count) { this.totalPatients = count; }

        public long getActivePatients() { return activePatients; }
        public void setActivePatients(long count) { this.activePatients = count; }

        public long getTotalAppointments() { return totalAppointments; }
        public void setTotalAppointments(long count) { this.totalAppointments = count; }

        public long getScheduledAppointments() { return scheduledAppointments; }
        public void setScheduledAppointments(long count) { this.scheduledAppointments = count; }

        public long getCompletedAppointments() { return completedAppointments; }
        public void setCompletedAppointments(long count) { this.completedAppointments = count; }

        public long getCancelledAppointments() { return cancelledAppointments; }
        public void setCancelledAppointments(long count) { this.cancelledAppointments = count; }

        public double getCompletionRate() { return completionRate; }
        public void setCompletionRate(double rate) { this.completionRate = rate; }

        @Override
        public String toString() {
            return "DashboardStats{" +
                    "totalPatients=" + totalPatients +
                    ", activePatients=" + activePatients +
                    ", totalAppointments=" + totalAppointments +
                    '}';
        }
    }

    /**
     * Appointment Trends class
     */
    public static class AppointmentTrends {
        private Map<LocalDate, Long> dayCounts = new HashMap<>();

        public void addDayCount(LocalDate date, long count) {
            dayCounts.put(date, count);
        }

        public Map<LocalDate, Long> getDayCounts() { return dayCounts; }
    }

    /**
     * Patient Demographics class
     */
    public static class PatientDemographics {
        private long malePatients;
        private long femalePatients;
        private Map<String, Long> bloodTypeDistribution = new HashMap<>();
        private Map<String, Long> cityDistribution = new HashMap<>();

        public long getMalePatients() { return malePatients; }
        public void setMalePatients(long count) { this.malePatients = count; }

        public long getFemalePatients() { return femalePatients; }
        public void setFemalePatients(long count) { this.femalePatients = count; }

        public Map<String, Long> getBloodTypeDistribution() { return bloodTypeDistribution; }
        public void setBloodTypeDistribution(Map<String, Long> dist) { this.bloodTypeDistribution = dist; }

        public Map<String, Long> getCityDistribution() { return cityDistribution; }
        public void setCityDistribution(Map<String, Long> dist) { this.cityDistribution = dist; }
    }
}
