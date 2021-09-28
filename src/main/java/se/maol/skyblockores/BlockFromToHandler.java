package se.maol.skyblockores;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.UUID;

public class BlockFromToHandler implements Listener {
    private SkyblockOres main;

    private BlockFace[] permittedFaces = new BlockFace[] {BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    public BlockFromToHandler(SkyblockOres instance) {
        Bukkit.getPluginManager().registerEvents(this, instance);
        main = instance;
    }

    @EventHandler
    private void onBlockFromTo(BlockFromToEvent e) {
        Block block = e.getBlock();
        Block toBlock = e.getToBlock();

        if(toBlock.getType() != Material.AIR || (block.getType() != Material.WATER && block.getType() != Material.LAVA)) {
            return;
        }

        Location location = getLocation(block.getType(), toBlock);

        if(generatesCobble(toBlock) && location != null) {
            Block targetBlock = location.getBlock();
            int random = 1 + (int) (Math.random() * 1000);

            UUID playerUuid = BlockHandler.getBlockOwner(targetBlock);

            if(playerUuid != null && Bukkit.getOfflinePlayer(playerUuid).isOnline()) {
                targetBlock.setType(BlockHandler.getBlockOwner(targetBlock) != null ? Configuration.getRandomCase(Bukkit.getPlayer(playerUuid), random) : Configuration.getRandomCase(location, random));
            } else {
                targetBlock.setType(Configuration.getRandomCase(location, random));
            }

            Location effectLocation = location.add(new Location(location.getWorld(), 0, 1.5, 0));
            effectLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, effectLocation, 5);
            effectLocation.getWorld().playSound(effectLocation, Sound.BLOCK_LAVA_EXTINGUISH, 0.25f, 1f);

            e.setCancelled(true);
        }
    }

    private boolean generatesCobble(Block block) {
        for(BlockFace face: permittedFaces) {
            if(block.getRelative(face).getType() == Material.WATER || block.getRelative(face).getType() == Material.LAVA) {
                return true;
            }
        }

        return false;
    }

    public Location getLocation(Material type, Block block) {
        Location location = null;

        for(BlockFace face: permittedFaces) {
            if(block.getRelative(face, 1).getType() == (type == Material.WATER ? Material.LAVA : Material.WATER) || block.getRelative(face, 1).getType() == (type == Material.WATER ? Material.LAVA : Material.WATER)) {
                location = block.getLocation();
            }
        }

        return location;
    }
}
