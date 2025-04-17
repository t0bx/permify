package de.t0bx.permifyvelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.t0bx.permifyvelocity.config.ConfigManager;
import de.t0bx.permifyvelocity.redis.RedisProvider;
import lombok.Getter;
import org.slf4j.Logger;

@Getter
@Plugin(
    id = "permify-velocity",
    name = "permify-velocity",
    version = "1.0.0"
    ,authors = {"t0bx"}
)
public class PermifyPlugin {

    @Getter
    private static PermifyPlugin instance;

    private final Logger logger;

    private final ProxyServer proxy;

    private final DataDirectory dataDirectory;

    private final ConfigManager configManager;

    private final RedisProvider redisProvider;

    @Inject
    public PermifyPlugin(Logger logger, ProxyServer proxy, DataDirectory dataDirectory) {
        instance = this;
        this.logger = logger;
        this.proxy = proxy;
        this.dataDirectory = dataDirectory;

        this.configManager = new ConfigManager();

        this.redisProvider = new RedisProvider();

        this.getLogger().info("Initializing Permify Velocity...");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        this.getLogger().info("Started Permify Velocity.");
    }
}
