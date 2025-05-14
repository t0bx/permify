package de.t0bx.permifypaper;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;

public final class PermifyPlugin extends JavaPlugin {

    private static PermifyPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        this.getLogger().info("Permify Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void attachPermission(Player player, String permission) {
        PermissionAttachment attachment = player.addAttachment(this);
        attachment.setPermission(permission, true);
    }

    private void detachPermission(Player player, String permission) {
        PermissionAttachment attachment = player.addAttachment(this);
        attachment.unsetPermission(permission);
    }

    private void setPermission(Player player, String permission, boolean value) {
        try {
            Class<?> craftHumanClass = player.getClass().getSuperclass();
            Field permField = craftHumanClass.getDeclaredField("perm");
            permField.setAccessible(true);

            Object permissibleBase = permField.get(player);
            Class<?> permissibleBaseClass = permissibleBase.getClass();

            Field permissionsField = permissibleBaseClass.getDeclaredField("permissions");
            permissionsField.setAccessible(true);

            Map<String, Boolean> permissions = (Map<String, Boolean>) permissionsField.get(permissibleBase);
            permissions.put(permission, value);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
