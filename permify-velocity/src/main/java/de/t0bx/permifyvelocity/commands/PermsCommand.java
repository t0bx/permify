package de.t0bx.permifyvelocity.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.UuidUtils;
import de.t0bx.permifyvelocity.PermifyPlugin;
import de.t0bx.permifyvelocity.group.IGroupHandler;
import de.t0bx.permifyvelocity.permission.IPermissionHandler;
import de.t0bx.permifyvelocity.permission.PermissionHandler;
import de.t0bx.permifyvelocity.player.UUIDFetcher;
import de.t0bx.permifyvelocity.redis.RedisProvider;
import de.t0bx.permifyvelocity.user.IUserHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.UUID;

public class PermsCommand implements SimpleCommand {

    private final RedisProvider redisProvider;

    private final IPermissionHandler permissionHandler;

    private final IGroupHandler groupHandler;

    private final IUserHandler userHandler;

    private final MiniMessage mm;

    private final String prefix;

    public PermsCommand() {
        this.redisProvider = PermifyPlugin.getInstance().getRedisProvider();
        this.permissionHandler = PermifyPlugin.getInstance().getPermissionHandler();
        this.groupHandler = PermifyPlugin.getInstance().getGroupHandler();
        this.userHandler = PermifyPlugin.getInstance().getUserHandler();
        this.mm = MiniMessage.miniMessage();
        this.prefix = PermifyPlugin.getInstance().getPrefix();
    }

    @Override
    public void execute(Invocation invocation) {

        String[] args = invocation.arguments();

        if (args.length == 0) {
            sendPermsHelp(invocation);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "creategroup" -> {
                if (args.length != 2) {
                    invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms creategroup <group>"));
                    return;
                }

                String group = args[1];
                if (this.groupHandler.existGroup(group)) {
                    invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Group " + group + " already exists"));
                    return;
                }

                this.groupHandler.createGroup(group);
                invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Group " + group + " created"));
            }

            case "deletegroup" -> {
                if (args.length != 2) {
                    invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms deletegroup <group>"));
                    return;
                }

                String group = args[1];
                if (!this.groupHandler.existGroup(group)) {
                    invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Group " + group + " does not exist"));
                    return;
                }

                this.groupHandler.deleteGroup(group);
                invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Group " + group + " deleted"));
            }

            case "user" -> {
                if (args.length < 3) {
                    invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms user <user> setgroup <group>"));
                    invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms user <user> permission add <permission>"));
                    invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms user <user> permission remove <permission>"));
                    return;
                }

                switch (args[2].toLowerCase()) {
                    case "setgroup" -> {
                        if (args.length != 5) {
                            invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms user <user> setgroup <group>"));
                            return;
                        }

                        final String playerName = args[2];
                        final String group = args[4];
                        UUID uuid;
                        try {
                            uuid = UUIDFetcher.getUUID(playerName);
                        } catch (Exception exception) {
                            invocation.source().sendMessage(this.mm.deserialize(this.prefix + "<red>The Player " + playerName + " doesn't exist"));
                            return;
                        }
                    }

                    case "permission" -> {

                    }
                }
            }

            case "group" -> {

            }

            default -> sendPermsHelp(invocation);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("permify.command");
    }

    private void sendPermsHelp(Invocation invocation) {
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + "<st>--------------------------</st>"));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + " "));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms creategroup <group>"));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms deletegroup <group>"));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms user <user> setgroup <group>"));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms user <user> permission add <permission>"));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms user <user> permission remove <permission>"));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms group <group> permission add <permission>"));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + "Usage: /perms group <group> permission remove <permission>"));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + " "));
        invocation.source().sendMessage(this.mm.deserialize(this.prefix + "<st>--------------------------</st>"));
    }
}
