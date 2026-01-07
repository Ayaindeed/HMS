package org.hsc.hospital_management_system.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis Configuration class for caching and real-time statistics
 */
public class RedisConfig {
    private static JedisPool jedisPool;

    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(10);
            config.setMaxIdle(5);
            config.setMinIdle(1);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            config.setTestWhileIdle(true);
            config.setMinEvictableIdleTimeMillis(60000);
            config.setTimeBetweenEvictionRunsMillis(30000);
            config.setNumTestsPerEvictionRun(3);

            String redisHost = System.getenv("REDIS_HOST");
            int redisPort = 6379;
            
            if (redisHost == null) {
                redisHost = "localhost";
            }
            
            String redisPortEnv = System.getenv("REDIS_PORT");
            if (redisPortEnv != null) {
                try {
                    redisPort = Integer.parseInt(redisPortEnv);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid REDIS_PORT: " + redisPortEnv);
                }
            }

            jedisPool = new JedisPool(config, redisHost, redisPort);
        } catch (Exception e) {
            System.err.println("Error initializing Redis pool: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Jedis getJedis() {
        if (jedisPool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(10);
            jedisPool = new JedisPool(config, "localhost", 6379);
        }
        return jedisPool.getResource();
    }

    public static JedisPool getJedisPool() {
        if (jedisPool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(10);
            jedisPool = new JedisPool(config, "localhost", 6379);
        }
        return jedisPool;
    }

    public static void shutdown() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }
}
