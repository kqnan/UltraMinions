package io.github.Leonardo0013YT.UltraMinions.addons.skyblocks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import io.github.Leonardo0013YT.UltraMinions.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class SuperiorSkyBlockAddon implements Listener {

    private Main plugin;

    public SuperiorSkyBlockAddon(Main plugin) {
        this.plugin = plugin;
    }

    public boolean checkMember(Player p) {
        Location loc = p.getLocation();
        Island var3 = SuperiorSkyblockAPI.getIslandAt(loc);
        if (var3 != null) {
            SuperiorPlayer var4 = SuperiorSkyblockAPI.getPlayer(p);
            return var3.hasPermission(var4, IslandPrivilege.getByName("BUILD"));
        }
        return false;
    }

    @EventHandler
    public void onCreate(IslandCreateEvent e) {
        Player p = e.getPlayer().asPlayer();
        if (!plugin.getCfm().isAutoMinionEnabled()) {
            return;
        }
        Island island = e.getIsland();
        Location miLoc = island.getCenter(World.Environment.NORMAL).clone().add(plugin.getCfm().getAddX(), plugin.getCfm().getAddY(), plugin.getCfm().getAddZ());
        plugin.getMm().createIslandMinion(p, miLoc);
    }

    @EventHandler
    public void onDelete(IslandDisbandEvent e) {
        Island island = e.getIsland();
        Location loc = island.getCenter(World.Environment.NORMAL);
        int size = island.getIslandSize() / 2;
        int minX = loc.getBlockX() - size;
        int maxZ = loc.getBlockZ() - size;
        byte y = 0;
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (int x = minX; x <= minX + island.getIslandSize(); x += 16) {
            for (int z = maxZ; z <= maxZ + island.getIslandSize(); z += 16) {
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