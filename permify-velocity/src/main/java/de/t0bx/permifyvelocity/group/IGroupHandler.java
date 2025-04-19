package de.t0bx.permifyvelocity.group;

import java.util.UUID;

public interface IGroupHandler {

    boolean createGroup(String groupName);

    boolean deleteGroup(String groupName);

    PermissionGroup getGroup(String groupName);

    boolean existGroup(String groupName);

    void loadGroups();
}
