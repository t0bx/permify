package de.t0bx.permifyvelocity.redis;

import de.t0bx.permifyvelocity.PermifyPlugin;
import de.t0bx.permifyvelocity.config.ConfigManager;
import redis.clients.jedis.JedisPool;

public class RedisProvider {

    private final JedisPool jedisPool;

    private final ConfigManager configManager;

    public RedisProvider() {
        this.configManager = PermifyPlugin.getInstance().getConfigManager();
        this.jedisPool = new JedisPool(this.configManager.getRedisHost(), this.configManager.getRedisPort());
    }
}
