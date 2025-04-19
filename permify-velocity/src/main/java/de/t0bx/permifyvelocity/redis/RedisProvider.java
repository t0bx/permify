package de.t0bx.permifyvelocity.redis;

import de.t0bx.permifyvelocity.PermifyPlugin;
import de.t0bx.permifyvelocity.config.ConfigManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPooled;

import java.util.List;
import java.util.Map;

public class RedisProvider implements IRedisProvider {

    private final JedisPooled pool;

    public RedisProvider(String host, int port) {
        this.pool = new JedisPooled(host, port);
    }

    @Override
    public void set(String key, String value) {
        this.pool.set(key, value);
    }

    @Override
    public String get(String key) {
        return this.pool.get(key);
    }

    @Override
    public void hset(String key, Map<String, String> hash) {
        this.pool.hset(key, hash);
    }

    @Override
    public String hget(String key, String field) {
       return this.pool.hget(key, field);
    }

    @Override
    public void rpush(String key, String... values) {
        this.pool.rpush(key, values);
    }

    @Override
    public void lpush(String key, String... values) {
        this.pool.lpush(key, values);
    }

    @Override
    public List<String> lrange(String key, int start, int end) {
        return this.pool.lrange(key, start, end);
    }

    @Override
    public void lrem(String key, int count, String value) {
        this.pool.lrem(key, count, value);
    }

    @Override
    public void lpop(String key) {
        this.pool.lpop(key);
    }

    @Override
    public void rpop(String key) {
        this.pool.rpop(key);
    }

    @Override
    public void publish(String channel, String message) {
        this.pool.publish(channel, message);
    }

    @Override
    public void del(String key) {
        this.pool.del(key);
    }

    @Override
    public void close() {
        this.pool.close();
    }
}
