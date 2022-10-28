package io.github.Leonardo0013YT.UltraMinions.addons.skyblocks;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.api.IslandCreateEvent;
import com.iridium.iridiumskyblock.api.IslandDeleteEvent;
import com.iridium.iridiumskyblock.api.IslandRegenEvent;
import com.iridium.iridiumskyblock.database.Island;
import io.github.Leonardo0013YT.UltraMinions.Main;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Optional;

public class IridiumSkyBlockAddon implements Listener {

    private Main plugin;

    public IridiumSkyBlockAddon(Main plugin) {
        this.plugin = plugin;
    }

    public boolean checkMember(Player p) {
        Optional<Island> island = IridiumSkyblockAPI.getInstance().getIslandViaLocation(p.getLocation());
        if (island.isPresent()) {
            Island is = island.get();
            if (is.getOwner() != null) {
                if (is.getOwner().getUuid().equals(p.getUniqueId())) {
                    return true;
                }
            }
            return is.getMembers().contains(IridiumSkyblockAPI.getInstance().getUser(p));
        }
        return true;
    }

    @EventHandler
    public void onCreate(IslandCreateEvent e) {
        Player p = Bukkit.getPlayer(e.getUser().getUuid());
        if (!plugin.getCfm().isAutoMinionEnabled()) {
            return;
        }
        Island island = e.getUser().getIsland().orElse(null);
        if (island == null) return;
        Location miLoc = island.getCenter(island.getHome().getWorld()).clone().add(plugin.getCfm().getAddX(), plugin.getCfm().getAddY(), plugin.getCfm().getAddZ());
        plugin.getMm().createIslandMinion(p, miLoc);
    }

    @EventHandler
    public void onRegen(IslandRegenEvent e) {
        remove(e.getIsland());
    }

    private void remove(Island island2) {
        Location loc = island2.getCenter(island2.getHome().getWorld());
        int size = island2.getSize() / 2;
        int minX = loc.getBlockX() - size;
        int maxZ = loc.getBlockZ() - size;
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (int y = 0; y < 16; y += 16) {
            for (int x = minX; x <= minX + island2.getSize(); x += 16) {
                for (int z = maxZ; z <= maxZ + island2.getSize(); z += 16) {
                    Chunk c = loc.getWorld().getBlockAt(x, y, z).getChunk();
                    if (!chunks.contains(c)) {
                        chunks.add(c);
                    }
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
    public void onDelete(IslandDeleteEvent e) {
        remove(e.getIsland());
    }


}