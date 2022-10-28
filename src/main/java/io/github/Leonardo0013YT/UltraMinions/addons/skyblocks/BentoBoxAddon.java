package io.github.Leonardo0013YT.UltraMinions.addons.skyblocks;

import io.github.Leonardo0013YT.UltraMinions.Main;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.events.island.IslandCreateEvent;
import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.bentobox.api.events.island.IslandResetEvent;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.IslandsManager;

import java.util.ArrayList;
import java.util.Optional;

public class BentoBoxAddon implements Listener {

    private final Main plugin;

    public BentoBoxAddon(Main plugin) {
        this.plugin = plugin;
    }

    public boolean checkMember(Player p) {
        IslandsManager im = BentoBox.getInstance().getIslands();
        Optional<Island> islands = im.getIslandAt(p.getLocation());
        if (islands.isPresent()) {
            Island is = islands.get();
            if (is.getOwner() != null && is.getOwner().equals(p.getUniqueId())) {
                return true;
            }
            return is.getMembers().containsKey(p.getUniqueId());
        }
        return false;
    }

    @EventHandler
    public void onCreate(IslandCreateEvent e) {
        Player p = Bukkit.getPlayer(e.getOwner());
        if (!plugin.getCfm().isAutoMinionEnabled()) {
            return;
        }
        Island island = e.getIsland();
        Location miLoc = island.getCenter().clone().add(plugin.getCfm().getAddX(), plugin.getCfm().getAddY(), plugin.getCfm().getAddZ());
        plugin.getMm().createIslandMinion(p, miLoc);
    }

    @EventHandler
    public void onDelete(IslandDeleteEvent e) {
        Island island = e.getIsland();
        Location loc = island.getSpawnPoint(World.Environment.NORMAL);
        if (loc == null) return;
        int size = island.getProtectionRange() / 2;
        int minX = loc.getBlockX() - size;
        int maxZ = loc.getBlockZ() - size;
        byte y = 0;
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (int x = minX; x <= minX + island.getProtectionRange(); x += 16) {
            for (int z = maxZ; z <= maxZ + island.getProtectionRange(); z += 16) {
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
        Island island = e.getIsland();
        Location loc = island.getSpawnPoint(World.Environment.NORMAL);
        if (loc == null) return;
        int size = island.getProtectionRange() / 2;
        int minX = loc.getBlockX() - size;
        int maxZ = loc.getBlockZ() - size;
        byte y = 0;
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (int x = minX; x <= minX + island.getProtectionRange(); x += 16) {
            for (int z = maxZ; z <= maxZ + island.getProtectionRange(); z += 16) {
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