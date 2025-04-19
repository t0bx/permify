package de.t0bx.permifyvelocity.redis;

import java.util.List;
import java.util.Map;

public interface IRedisProvider {

    void set(String key, String value);

    String get(String key);

    void hset(String key, Map<String, String> hash);

    String hget(String key, String field);

    void rpush(String key, String... values);

    void lpush(String key, String... values);

    List<String> lrange(String key, int start, int end);

    void lrem(String key, int count, String value);

    void lpop(String key);

    void rpop(String key);

    void publish(String channel, String message);

    void del(String key);

    void close();
}
