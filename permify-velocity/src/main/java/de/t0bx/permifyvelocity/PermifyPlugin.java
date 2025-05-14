package de.t0bx.permifyvelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.t0bx.permifyvelocity.commands.PermsCommand;
import de.t0bx.permifyvelocity.config.ConfigManager;
import de.t0bx.permifyvelocity.database.IMySQLHandler;
import de.t0bx.permifyvelocity.database.MySQLHandler;
import de.t0bx.permifyvelocity.group.GroupHandler;
import de.t0bx.permifyvelocity.group.IGroupHandler;
import de.t0bx.permifyvelocity.language.Language;
import de.t0bx.permifyvelocity.language.MessageProvider;
import de.t0bx.permifyvelocity.permission.IPermissionHandler;
import de.t0bx.permifyvelocity.permission.PermissionHandler;
import de.t0bx.permifyvelocity.redis.RedisProvider;
import de.t0bx.permifyvelocity.user.IUserHandler;
import de.t0bx.permifyvelocity.user.UserHandler;
import lombok.Getter;
import org.slf4j.Logger;

import java.sql.SQLException;

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

    private final ConfigManager configManager;

    private final MessageProvider messageProvider;

    private final RedisProvider redisProvider;

    private final IMySQLHandler mySQLHandler;

    private final IPermissionHandler permissionHandler;

    private final IGroupHandler groupHandler;

    private final IUserHandler userHandler;

    private final String prefix = "<gradient:#00aaaa:#55ffff>Permify</gradient> <dark_gray>|</dark_gray> <gray>";

    @Inject
    public PermifyPlugin(Logger logger, ProxyServer proxy) {
        instance = this;
        this.logger = logger;
        this.proxy = proxy;
        getLogger().info("Initializing Permify Velocity...");

        this.configManager = new ConfigManager();

        this.messageProvider = new MessageProvider(Language.fromCode(this.configManager.getLanguage()));

        this.redisProvider = new RedisProvider(this.configManager.getRedisHost(), this.configManager.getRedisPort());

        this.mySQLHandler = new MySQLHandler(
                this.configManager.getDatabaseHost(),
                this.configManager.getDatabasePort(),
                this.configManager.getDatabaseUsername(),
                this.configManager.getDatabasePassword(),
                this.configManager.getDatabaseName()
        );

        try {
            this.mySQLHandler.connect();
            this.createDefaultTables();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        this.groupHandler = new GroupHandler();
        this.userHandler = new UserHandler();

        this.permissionHandler = new PermissionHandler();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        this.initCommands();

        getLogger().info("Started Permify Velocity.");
    }

    private void createDefaultTables() throws SQLException {
        String userTable = "CREATE TABLE IF NOT EXISTS permify_users(" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "groupName TEXT NOT NULL)";

        this.mySQLHandler.update(userTable);

        String userPermissionTable = "CREATE TABLE IF NOT EXISTS permify_users_permissions(" +
                "uuid VARCHAR(36) NOT NULL, " +
                "permission TEXT NOT NULL)";

        this.mySQLHandler.update(userPermissionTable);

        String groupTable = "CREATE TABLE IF NOT EXISTS permify_groups(" +
                "groupName VARCHAR(26) PRIMARY KEY, " +
                "inheritanceFrom TEXT)";

        this.mySQLHandler.update(groupTable);

        String groupPermissionTable = "CREATE TABLE IF NOT EXISTS permify_groups_permissions(" +
                "groupName TEXT NOT NULL, " +
                "permission TEXT NOT NULL)";

        this.mySQLHandler.update(groupPermissionTable);
    }

    private void initCommands() {
        final CommandManager commandManager = this.proxy.getCommandManager();
        final CommandMeta permsCommandMeta = commandManager.metaBuilder("perms").aliases("permissions", "permify").plugin(this).build();

        commandManager.register(permsCommandMeta, new PermsCommand(this.messageProvider));
    }
}
