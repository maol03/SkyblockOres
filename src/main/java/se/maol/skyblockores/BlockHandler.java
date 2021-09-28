package se.maol.skyblockores;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

public class BlockHandler implements Listener {
    private static SkyblockOres main;

    public BlockHandler(SkyblockOres instance) {
        Bukkit.getPluginManager().registerEvents(this, instance);
        main = instance;
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if(e.isCancelled()) {
            return;
        }

        e.getBlock().setMetadata("owner", new FixedMetadataValue(main, e.getPlayer().getUniqueId().toString()));
    }

    public static UUID getBlockOwner(Block block) {
        List<MetadataValue> metas = block.getMetadata("owner");

        for(MetadataValue meta : metas) {
            if(meta.getOwningPlugin().getName().equals(main.getName())) {
                return UUID.fromString(meta.asString());
            }
        }

        return null;
    }
}
