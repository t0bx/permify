package de.t0bx.permifyvelocity.group;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Builder
public abstract class PermissionGroup {

    private final @NotNull String groupName;
    private final String inheritanceFrom;
    private final List<String> permissions;

}
