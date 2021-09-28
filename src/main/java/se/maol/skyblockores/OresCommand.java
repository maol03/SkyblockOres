package se.maol.skyblockores;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class OresCommand extends BukkitCommand {
    private static SkyblockOres main;

    public OresCommand(SkyblockOres instance) {
        super("ores");
        this.setAliases(new ArrayList<>(Arrays.asList("skyblockores", "generator")));
        this.description = "Displays your chances when generating cobblestone.";
        this.usageMessage = "/ores [reload]";
        this.setPermission("skyblockores.info");

        main = instance;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(!player.hasPermission(this.getPermission())) {
                player.sendMessage(Configuration.getLocalization().get("NoPermission").replaceAll("&", "§"));
                return true;
            }

            if(args.length == 1 && args[0].equalsIgnoreCase("reload") && player.hasPermission("skyblockores.reload")) {
                main.reloadConfig();
                Configuration.loadConfiguration();

                player.sendMessage(Configuration.getLocalization().get("ConfigReloaded").replaceAll("&", "§"));
            } else {
                if(Configuration.getPermissionLevel(player).equalsIgnoreCase("Default")) {
                    player.sendMessage(Configuration.getLocalization().get("YouHaveDefault").replaceAll("&", "§"));
                } else {
                    player.sendMessage(Configuration.getLocalization().get("YouHavePermission").replaceAll("%permission%", Configuration.getPermissionLevel(player)).replaceAll("&", "§"));
                }

                player.sendMessage(Configuration.getLocalization().get("YourRates").replaceAll("&", "§"));

                for(Map.Entry<Material, Integer> material : Configuration.getBlockTypes(player, null).entrySet()) {
                    player.sendMessage(Configuration.getLocalization().get("YourRatesRow").replaceAll("%material%", material.getKey().toString()).replaceAll("%chance%", ((double) (material.getValue() / 10)) + "%").replaceAll("&", "§"));
                }
            }
        } else {
            if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                main.reloadConfig();
                Configuration.loadConfiguration();

                sender.sendMessage(Configuration.getLocalization().get("ConfigReloaded").replaceAll("&", "§"));
            } else {
                sender.sendMessage("§cUsage: " + this.getUsage());
            }
        }

        return true;
    }
}
