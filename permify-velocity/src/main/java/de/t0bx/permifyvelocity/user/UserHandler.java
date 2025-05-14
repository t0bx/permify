package de.t0bx.permifyvelocity.user;

import de.t0bx.permifyvelocity.PermifyPlugin;
import de.t0bx.permifyvelocity.database.IMySQLHandler;
import de.t0bx.permifyvelocity.group.IGroupHandler;
import de.t0bx.permifyvelocity.group.PermissionGroup;
import de.t0bx.permifyvelocity.redis.IRedisProvider;

import java.util.Objects;
import java.util.UUID;

public class UserHandler implements IUserHandler {

    private final IRedisProvider redisProvider;

    private final IMySQLHandler mysqlHandler;

    private final IGroupHandler groupHandler;

    public UserHandler() {
        this.redisProvider = PermifyPlugin.getInstance().getRedisProvider();
        this.mysqlHandler = PermifyPlugin.getInstance().getMySQLHandler();
        this.groupHandler = PermifyPlugin.getInstance().getGroupHandler();
    }

    @Override
    public boolean setGroup(UUID uuid, PermissionGroup group) {
        if (!this.groupHandler.existGroup(group.getGroupName())) return false;

        String sql = "UPDATE permify_users SET groupName = ? WHERE uuid = ?";

        this.mysqlHandler.updateAsync(sql, group.getGroupName(), uuid.toString());
        this.redisProvider.set("users." + uuid, group.getGroupName());
        return true;
    }

    @Override
    public boolean setGroup(UUID uuid, String groupName) {
        if (!this.groupHandler.existGroup(groupName)) return false;

        String sql = "UPDATE permify_users SET groupName = ? WHERE uuid = ?";

        this.mysqlHandler.updateAsync(sql, groupName, uuid.toString());
        this.redisProvider.set("users." + uuid, groupName);
        return true;
    }

    @Override
    public boolean addPermission(UUID uuid, String permission) {

        return false;
    }

    @Override
    public boolean removePermission(UUID uuid, String permission) {
        return false;
    }

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        return Objects.equals(this.redisProvider.get("users." + uuid + ".permissions"), permission);
    }
}
