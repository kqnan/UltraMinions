package io.github.Leonardo0013YT.UltraMinions.addons.skyblocks;

import com.wasteofplastic.acidisland.ASkyBlockAPI;
import com.wasteofplastic.acidisland.Island;
import com.wasteofplastic.acidisland.events.IslandPreDeleteEvent;
import com.wasteofplastic.acidisland.events.IslandResetEvent;
import io.github.Leonardo0013YT.UltraMinions.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class AcidIslandAddon implements Listener {

    private ASkyBlockAPI instance = ASkyBlockAPI.getInstance();
    private Main plugin;

    public AcidIslandAddon(Main plugin) {
        this.plugin = plugin;
    }

    public boolean checkMember(Player p) {
        Location loc = p.getLocation();
        Island island = instance.getIslandAt(loc);
        if ((this.instance.getIslandWorld() == loc.getWorld() || this.instance.getNetherWorld() == loc.getWorld()) && this.instance.playerIsOnIsland(p)) {
            return island.getOwner().equals(p.getUniqueId()) || island.getMembers().contains(p.getUniqueId());
        }
        return false;
    }

    @EventHandler
    public void onDelete(IslandPreDeleteEvent e) {
        Island island = e.getIsland();
        Location loc = island.getCenter();
        int size = island.getProtectionSize() / 2;
        int minX = loc.getBlockX() - size;
        int maxZ = loc.getBlockZ() - size;
        byte y = 0;
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (int x = minX; x <= minX + island.getProtectionSize(); x += 16) {
            for (int z = maxZ; z <= maxZ + island.getProtectionSize(); z += 16) {
                Chunk c = loc.getWorld().getBlockAt(x, y, z).getChunk();
                if (!chunks.contains(c)) {
                    chunks.add(c);
                }
            }
        }
        for (Chunk c : chunks) {
            for (Entity ent : c.getEntities()) {
                if (!(ent instanceof ArmorStand)) continue;
                plugin.getMm().removeIslandMinion(ent);
            }
        }
    }

    @EventHandler
    public void onReset(IslandResetEvent e) {
        Island island = instance.getIslandAt(e.getLocation());
        if (island == null) return;
        Location loc = island.getCenter();
        int size = island.getProtectionSize() / 2;
        int minX = loc.getBlockX() - size;
        int maxZ = loc.getBlockZ() - size;
        byte y = 0;
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (int x = minX; x <= minX + island.getProtectionSize(); x += 16) {
            for (int z = maxZ; z <= maxZ + island.getProtectionSize(); z += 16) {
                Chunk c = loc.getWorld().getBlockAt(x, y, z).getChunk();
                if (!chunks.contains(c)) {
                    chunks.add(c);
                }
            }
        }
        for (Chunk c : chunks) {
            for (Entity ent : c.getEntities()) {
                if (!(ent instanceof ArmorStand)) continue;
                plugin.getMm().removeIslandMinion(ent);
            }
        }
    }


}