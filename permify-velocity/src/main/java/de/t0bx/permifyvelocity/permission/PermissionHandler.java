package de.t0bx.permifyvelocity.permission;

import de.t0bx.permifyvelocity.PermifyPlugin;
import de.t0bx.permifyvelocity.group.IGroupHandler;
import de.t0bx.permifyvelocity.group.PermissionGroup;
import de.t0bx.permifyvelocity.redis.IRedisProvider;
import de.t0bx.permifyvelocity.user.IUserHandler;

import java.util.UUID;

public class PermissionHandler implements IPermissionHandler {

    private final IRedisProvider redisProvider;

    private final IGroupHandler groupHandler;

    private final IUserHandler userHandler;

    public PermissionHandler() {
        this.redisProvider = PermifyPlugin.getInstance().getRedisProvider();
        this.groupHandler = PermifyPlugin.getInstance().getGroupHandler();
        this.userHandler = PermifyPlugin.getInstance().getUserHandler();
    }

    /**
     * Sets a User a group with the PermissionGroup
     * @param uuid the uuid from the user
     * @param group the PermissionGroup
     * @return If returned false then group couldn't be set
     */
    @Override
    public boolean setGroup(UUID uuid, PermissionGroup group) {
        return false;
    }

    /**
     * Sets a User a group with the GroupName
     * @param uuid the uuid from the user
     * @param groupName the groupName
     * @return If returned false then group couldn't be set
     */
    @Override
    public boolean setGroup(UUID uuid, String groupName) {
        return false;
    }

    /**
     * Gets the PermissionGroup with the GroupId
     * @param uuid the GroupId
     * @return PermissionGroup of the specific group
     */
    @Override
    public PermissionGroup getGroup(UUID uuid) {
        return null;
    }

    /**
     * Gets the PermissionGroup with the GroupName
     * @param groupName the GroupName
     * @return PermissionGroup of the specific group
     */
    @Override
    public PermissionGroup getGroup(String groupName) {
        return null;
    }


    /**
     * Adds a group a permission
     * @param group the PermissionGroup
     * @param permission the Permission
     * @return If returned false then permission to group couldn't be set
     */
    @Override
    public boolean addGroupPermission(PermissionGroup group, String permission) {
        return false;
    }

    /**
     * Adds a group a permission
     * @param groupName the groupName
     * @param permission the Permission
     * @return If returned false then permission to group couldn't be set
     */
    @Override
    public boolean addGroupPermission(String groupName, String permission) {
        return false;
    }

    /**
     * Removes a group a permission
     * @param group the PermissionGroup
     * @param permission the permission
     * @return If returned false then permission to group couldn't be removed
     */
    @Override
    public boolean removeGroupPermission(PermissionGroup group, String permission) {
        return false;
    }


    /**
     * Removes a group a permission
     * @param groupName the groupName
     * @param permission the permission
     * @return If returned false then permission to group couldn't be removed
     */
    @Override
    public boolean removeGroupPermission(String groupName, String permission) {
        return false;
    }
}
