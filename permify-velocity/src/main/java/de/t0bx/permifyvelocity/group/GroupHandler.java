package de.t0bx.permifyvelocity.group;

import de.t0bx.permifyvelocity.PermifyPlugin;
import de.t0bx.permifyvelocity.database.IMySQLHandler;
import de.t0bx.permifyvelocity.redis.IRedisProvider;

import java.sql.SQLException;

public class GroupHandler implements IGroupHandler {

    private final IRedisProvider redisProvider;

    private final IMySQLHandler mysqlHandler;

    public GroupHandler() {
        this.redisProvider = PermifyPlugin.getInstance().getRedisProvider();
        this.mysqlHandler = PermifyPlugin.getInstance().getMySQLHandler();
        this.loadGroups();
    }

    @Override
    public boolean createGroup(String groupName) {
        if (existGroup(groupName)) return false;

        String sql = "INSERT INTO permify_groups (groupName, inheritanceFrom) VALUES (?, ?)";

        this.mysqlHandler.updateAsync(sql, groupName, "null");
        this.redisProvider.set("groups." + groupName, "null");
        return true;
    }

    @Override
    public boolean deleteGroup(String groupName) {
        if (!existGroup(groupName)) return false;

        String sql = "DELETE FROM permify_groups WHERE groupName = ?";

        this.mysqlHandler.updateAsync(sql, groupName);
        this.redisProvider.del("groups." + groupName);
        return true;
    }

    @Override
    public PermissionGroup getGroup(String groupName) {
        return null;
    }

    @Override
    public boolean existGroup(String groupName) {
        return this.redisProvider.get("groupnames." + groupName) != null;
    }

    @Override
    public void loadGroups() {
        String query = "SELECT groupName, inheritanceFrom FROM permify_groups";

        this.mysqlHandler.queryAsync(query, resultSet -> {
           try {
               this.redisProvider.set("groups." + resultSet.getString("groupName"), resultSet.getString("inheritanceFrom"));
           } catch (SQLException exception) {
               throw new RuntimeException(exception);
           }
           return null;
        });
    }
}
