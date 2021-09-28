package se.maol.skyblockores;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private static SkyblockOres main;

    private static Map<World, Map<String, Map<Material, Integer>>> blockTypes;
    private static Map<World, Map<String, Map<Integer, Material>>> randomCases;
    private static Map<String, String> localization;

    public Configuration(SkyblockOres instance) {
        main = instance;
    }

    public static void loadConfiguration() {
        blockTypes = new HashMap<>();
        randomCases = new HashMap<>();
        localization = new HashMap<>();

        for(String world : main.getConfig().getConfigurationSection("BlockTypes").getKeys(false)) {
            Map<String, Map<Material, Integer>> perWorld1 = new HashMap<>();
            Map<String, Map<Integer, Material>> perWorld2 = new HashMap<>();

            for(String permission : main.getConfig().getConfigurationSection("BlockTypes." + world).getKeys(false)) {
                Map<Material, Integer> perPermission1 = new HashMap<>();
                Map<Integer, Material> perPermission2 = new HashMap<>();

                int index = 1;
                for(String materialString : main.getConfig().getConfigurationSection("BlockTypes." + world + "." + permission).getKeys(false)) {
                    try {
                        Material material = Material.getMaterial(materialString.toUpperCase());
                        int chance = main.getConfig().getInt("BlockTypes." + world + "." + permission + "." + materialString.toUpperCase());

                        perPermission1.put(material, chance);

                        for(int i = 1; i <= chance; i++) {
                            perPermission2.put(index, material);
                            index++;
                        }
                    } catch(Exception e) {
                        Bukkit.getConsoleSender().sendMessage("§4ERROR: §cThe material §4" + materialString.toUpperCase() + " §cdoesn't exist, or its value is not an integer.");
                    }
                }

                perWorld1.put(permission, perPermission1);
                perWorld2.put(permission, perPermission2);
            }

            if(Bukkit.getWorld(world) != null) {
                blockTypes.put(Bukkit.getWorld(world), perWorld1);
                randomCases.put(Bukkit.getWorld(world), perWorld2);
            } else {
                Bukkit.getConsoleSender().sendMessage("§4ERROR: §cThe world §4" + world + " §cisn't loaded. Make sure that the worlds are loaded before this plugin.");
            }
        }

        for(String local : main.getConfig().getConfigurationSection("Localization").getKeys(false)) {
            localization.put(local, main.getConfig().getString("Localization." + local));
        }
    }

    public static Map<Material, Integer> getBlockTypes(OfflinePlayer offlinePlayer, Location location) {
        if(!offlinePlayer.isOnline()) {
            return blockTypes.get(location.getWorld()).get("Default");
        }

        Player player = (Player) offlinePlayer;

        for(Map.Entry<String, Map<Material, Integer>> permission : blockTypes.get(player.getWorld()).entrySet()) {
            if(player.hasPermission("skyblockores." + permission.getKey())) {
                return permission.getValue();
            }
        }

        return blockTypes.get(player.getWorld()).get("Default");
    }

    public static Material getRandomCase(Location location, int id) {
        return randomCases.get(location.getWorld()).get("Default").get(id);
    }

    public static Material getRandomCase(Player player, int id) {
        for(Map.Entry<String, Map<Integer, Material>> permission : randomCases.get(player.getWorld()).entrySet()) {
            if(player.hasPermission("skyblockores." + permission.getKey())) {
                return permission.getValue().get(id);
            }
        }

        return randomCases.get(player.getWorld()).get("Default").get(id);
    }

    public static String getPermissionLevel(Player player) {
        for(Map.Entry<String, Map<Material, Integer>> permission : blockTypes.get(player.getWorld()).entrySet()) {
            if(player.hasPermission("skyblockores." + permission.getKey())) {
                return permission.getKey();
            }
        }

        return "Default";
    }

    public static Map<String, String> getLocalization() {
        return localization;
    }
}
