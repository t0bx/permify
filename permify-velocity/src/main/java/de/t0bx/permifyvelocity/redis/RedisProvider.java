package de.t0bx.permifyvelocity.redis;

import de.t0bx.permifyvelocity.PermifyPlugin;
import org.slf4j.Logger;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RedisProvider implements IRedisProvider {

    private final Logger LOGGER = PermifyPlugin.getInstance().getLogger();
    private final JedisPooled pool;
    private final Executor executor;

    public RedisProvider(String host, int port) {
        Objects.requireNonNull(host, "Redis host cannot be null");
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port number: " + port);
        }

        this.pool = new JedisPooled(host, port);
        this.executor = Executors.newFixedThreadPool(1);
        this.LOGGER.info("Redis connection established to {}:{}", host, port);
    }

    @Override
    public void set(String key, String value) {
        this.validateKey(key);
        try {
            this.pool.set(key, value);
        } catch (JedisException e) {
            this.handleException("Failed to set value", e);
        }
    }

    public void setAsync(String key, String value) {
        this.validateKey(key);
        CompletableFuture.runAsync(() -> this.pool.set(key, value), this.executor);
    }

    @Override
    public String get(String key) {
        this.validateKey(key);
        try {
            return this.pool.get(key);
        } catch (JedisException e) {
            this.handleException("Failed to get value", e);
            return null;
        }
    }

    public CompletableFuture<String> getAsync(String key) {
        this.validateKey(key);
        return CompletableFuture.supplyAsync(() -> this.pool.get(key), this.executor);
    }

    @Override
    public void hset(String key, Map<String, String> hash) {
        this.validateKey(key);
        Objects.requireNonNull(hash, "Hash map cannot be null");
        try {
            this.pool.hset(key, hash);
        } catch (JedisException e) {
            this.handleException("Failed to set hash", e);
        }
    }

    public void hsetAsync(String key, Map<String, String> hash) {
        this.validateKey(key);
        Objects.requireNonNull(hash, "Hash map cannot be null");
        CompletableFuture.runAsync(() -> this.hset(key, hash), this.executor);
    }

    @Override
    public String hget(String key, String field) {
        this.validateKey(key);
        this.validateField(field);
        try {
            return this.pool.hget(key, field);
        } catch (JedisException e) {
            this.handleException("Failed to get hash field", e);
            return null;
        }
    }

    public CompletableFuture<String> hgetAsync(String key, String field) {
        this.validateKey(key);
        this.validateField(field);
        return CompletableFuture.supplyAsync(() -> this.hget(key, field), this.executor);
    }

    @Override
    public void rpush(String key, String... values) {
        this.validateKey(key);
        this.validateValues(values);
        try {
            this.pool.rpush(key, values);
        } catch (JedisException e) {
            this.handleException("Failed to push values to list", e);
        }
    }

    public void rpushAsync(String key, String... values) {
        this.validateKey(key);
        this.validateValues(values);
        CompletableFuture.runAsync(() -> this.rpush(key, values), this.executor);
    }

    @Override
    public void lpush(String key, String... values) {
        this.validateKey(key);
        this.validateValues(values);
        try {
            this.pool.lpush(key, values);
        } catch (JedisException e) {
            this.handleException("Failed to push values to list", e);
        }
    }

    public void lpushAsync(String key, String... values) {
        this.validateKey(key);
        this.validateValues(values);
        CompletableFuture.runAsync(() -> this.lpush(key, values), this.executor);
    }

    @Override
    public List<String> lrange(String key, int start, int end) {
        this.validateKey(key);
        try {
            return this.pool.lrange(key, start, end);
        } catch (JedisException e) {
            this.handleException("Failed to get range from list", e);
            return Collections.emptyList();
        }
    }

    public CompletableFuture<List<String>> lrangeAsync(String key, int start, int end) {
        this.validateKey(key);
        return CompletableFuture.supplyAsync(() -> this.lrange(key, start, end), this.executor);
    }

    @Override
    public void lrem(String key, int count, String value) {
        this.validateKey(key);
        try {
            this.pool.lrem(key, count, value);
        } catch (JedisException e) {
            this.handleException("Failed to remove value from list", e);
        }
    }

    public void lremAsync(String key, int count, String value) {
        this.validateKey(key);
        CompletableFuture.runAsync(() -> this.lrem(key, count, value), this.executor);
    }

    @Override
    public void lpop(String key) {
        this.validateKey(key);
        try {
            this.pool.lpop(key);
        } catch (JedisException e) {
            this.handleException("Failed to pop value from list", e);
        }
    }

    public void lpopAsync(String key) {
        this.validateKey(key);
        CompletableFuture.runAsync(() -> this.lpop(key), this.executor);
    }

    @Override
    public void rpop(String key) {
        this.validateKey(key);
        try {
            this.pool.rpop(key);
        } catch (JedisException e) {
            this.handleException("Failed to pop value from list", e);
        }
    }

    public void rpopAsync(String key) {
        this.validateKey(key);
        CompletableFuture.runAsync(() -> this.rpop(key), this.executor);
    }

    @Override
    public void publish(String channel, String message) {
        this.validateChannel(channel);
        Objects.requireNonNull(message, "Message cannot be null");
        try {
            this.pool.publish(channel, message);
        } catch (JedisException e) {
            this.handleException("Failed to publish message", e);
        }
    }

    public void publishAsync(String channel, String message) {
        this.validateChannel(channel);
        Objects.requireNonNull(message, "Message cannot be null");
        CompletableFuture.runAsync(() -> this.pool.publish(channel, message), this.executor);
    }

    @Override
    public void del(String key) {
        this.validateKey(key);
        try {
            this.pool.del(key);
        } catch (JedisException e) {
            this.handleException("Failed to delete key", e);
        }
    }

    public void delAsync(String key) {
        this.validateKey(key);
        CompletableFuture.runAsync(() -> this.del(key), this.executor);
    }

    @Override
    public void close() {
        try {
            if (this.pool != null) {
                this.pool.close();
                this.LOGGER.info("Redis connection closed");
            }
        } catch (Exception e) {
            this.LOGGER.error("Error closing Redis connection", e);
        }
    }

    private void validateKey(String key) {
        Objects.requireNonNull(key, "Key cannot be null");
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
    }

    private void validateField(String field) {
        Objects.requireNonNull(field, "Field cannot be null");
    }

    private void validateChannel(String channel) {
        Objects.requireNonNull(channel, "Channel cannot be null");
        if (channel.isEmpty()) {
            throw new IllegalArgumentException("Channel cannot be empty");
        }
    }

    private void validateValues(String... values) {
        Objects.requireNonNull(values, "Values cannot be null");
        if (values.length == 0) {
            throw new IllegalArgumentException("At least one value must be provided");
        }
    }

    private void handleException(String message, Exception e) {
        if (e instanceof JedisConnectionException) {
            this.LOGGER.error("{}: Connection error - {}", message, e.getMessage());
        } else {
            this.LOGGER.error("{}: {}", message, e.getMessage());
        }
    }
}