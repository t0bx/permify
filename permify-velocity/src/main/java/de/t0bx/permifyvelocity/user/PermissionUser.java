package de.t0bx.permifyvelocity.user;

import de.t0bx.permifyvelocity.group.PermissionGroup;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public abstract class PermissionUser {

    private final UUID uuid;
    private final PermissionGroup permissionGroup;
    private final List<String> permissions;

}
