package de.t0bx.permifyvelocity.user;

import de.t0bx.permifyvelocity.group.PermissionGroup;

import java.util.UUID;

public interface IUserHandler {

    boolean setGroup(UUID uuid, PermissionGroup group);

    boolean setGroup(UUID uuid, String groupName);

    boolean addPermission(UUID uuid, String permission);

    boolean removePermission(UUID uuid, String permission);

    boolean hasPermission(UUID uuid, String permission);
}
