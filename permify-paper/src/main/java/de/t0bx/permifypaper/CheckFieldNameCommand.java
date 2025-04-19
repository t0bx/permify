package de.t0bx.permifypaper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CheckFieldNameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        player.sendMessage("Checking for PermissionClass...");
        return false;
    }

    /*public void findPermissionsField(Player player) {
        try {
            // Beginne mit der Player-Klasse (ein CraftPlayer)
            Class<?> craftPlayerClass = player.getClass();

            System.out.println("Player class: " + craftPlayerClass.getName());

            // Finde die Superklasse (wahrscheinlich CraftHumanEntity)
            Class<?> craftHumanClass = craftPlayerClass.getSuperclass();
            System.out.println("Superclass: " + craftHumanClass.getName());

            // Durchlaufe alle Felder in der Klasse und ihren Superklassen
            // und gibt deren Namen aus
            for (Class<?> clazz = craftHumanClass; clazz != null; clazz = clazz.getSuperclass()) {
                System.out.println("Checking class: " + clazz.getName());

                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    System.out.println("  Field: " + field.getName() + " of type " + field.getType().getName());

                    // Wenn wir ein Feld finden, das eine Map ist, könnte es die permissions sein
                    if (Map.class.isAssignableFrom(field.getType())) {
                        try {
                            Map<?, ?> map = (Map<?, ?>) field.get(player);
                            if (map != null && !map.isEmpty()) {
                                // Schaue dir den ersten Eintrag an, um zu sehen ob es sich um Permissions handelt
                                Object firstKey = map.keySet().iterator().next();
                                Object firstValue = map.get(firstKey);

                                System.out.println("    Potential permissions map found!");
                                System.out.println("    Sample entry: " + firstKey + " -> " + firstValue);

                                if (firstKey instanceof String && firstValue instanceof Boolean) {
                                    System.out.println("    THIS LOOKS LIKE THE PERMISSIONS MAP!");
                                    System.out.println("    Field name: " + field.getName());
                                }
                            }
                        } catch (Exception e) {
                            // Ignoriere Fehler beim Zugriff auf dieses Feld
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPermission(Player player) {
        try {
            Class<?> craftHumanClass = player.getClass().getSuperclass();
            Field permField = craftHumanClass.getDeclaredField("perm");
            permField.setAccessible(true);

            Object permissibleBase = permField.get(player);
            Class<?> permissibleBaseClass = permissibleBase.getClass();

            //Field permissionsField = permissibleBaseClass.getDeclaredField("permissions");
            //permissionsField.setAccessible(true);

            for (Field field : permissibleBaseClass.getDeclaredFields()) {
                System.out.println("Field: " + field.getName() + " of type " + field.getType().getName());
            }

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }*/
}
