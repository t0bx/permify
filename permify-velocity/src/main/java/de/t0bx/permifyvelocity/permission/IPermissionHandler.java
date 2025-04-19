package de.t0bx.permifyvelocity.permission;

import de.t0bx.permifyvelocity.group.PermissionGroup;

import java.util.UUID;

public interface IPermissionHandler {

    boolean setGroup(UUID uuid, PermissionGroup group);

    boolean setGroup(UUID uuid, String groupName);

    PermissionGroup getGroup(UUID uuid);

    PermissionGroup getGroup(String groupName);

    boolean addGroupPermission(PermissionGroup group, String permission);

    boolean addGroupPermission(String groupName, String permission);

    boolean removeGroupPermission(PermissionGroup group, String permission);

    boolean removeGroupPermission(String groupName, String permission);

}
