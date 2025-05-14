package de.t0bx.permifyvelocity.group;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Getter
@Builder
public class PermissionGroup {

    private final @NotNull String groupName;
    private final String inheritanceFrom;
    private final Set<String> permissions;

}
