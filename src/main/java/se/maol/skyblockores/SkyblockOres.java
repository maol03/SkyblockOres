package se.maol.skyblockores;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class SkyblockOres extends JavaPlugin {
    @Override
    public void onEnable() {
        configure();
        registerCommands();
        registerListeners();

        Bukkit.getConsoleSender().sendMessage("§2INFO: §aSkyblockOres has been enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§4INFO: §cSkyblockOres has been disabled.");
    }

    public void configure() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        new Configuration(this);
        Configuration.loadConfiguration();
    }

    public void registerCommands() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register("ores", new OresCommand(this));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void registerListeners() {
        new BlockHandler(this);
        new BlockFromToHandler(this);
    }
}
