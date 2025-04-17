package de.t0bx.permifyvelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(
    id = "permify-velocity",
    name = "permify-velocity",
    version = "1.0.0"
    ,authors = {"t0bx"}
)
public class PermifyPlugin {

    private final Logger logger;

    private final ProxyServer proxy;

    private final DataDirectory dataDirectory;

    @Inject
    public PermifyPlugin(Logger logger, ProxyServer proxy, DataDirectory dataDirectory) {
        this.logger = logger;
        this.proxy = proxy;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
