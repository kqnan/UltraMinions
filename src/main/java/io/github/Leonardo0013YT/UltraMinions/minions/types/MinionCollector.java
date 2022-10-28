package io.github.Leonardo0013YT.UltraMinions.minions.types;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionChest;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

public class MinionCollector extends Minion {

    public MinionCollector(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
    }

    @Override
    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        if (armor == null || pm == null) return;
        if (!spawn.getChunk().isLoaded()) return;
        if (pm.isChest()) {
            armor.setItemInHand(getHandItem());
            PlayerMinionChest pmc = pm.getChest();
            Collection<Entity> entities = spawn.getWorld().getNearbyEntities(spawn, 5, 5, 5).stream().filter(e -> e instanceof Item).collect(Collectors.toList());
            for (Entity e : entities) {
                Item i = (Item) e;
                if (pmc.addItem(i.getItemStack())) {
                    i.remove();
                    pm.setFull(false);
                } else {
                    pm.setFull(true);
                }
            }
        }
    }

    @Override
    public boolean check(Location spawn) {
        return true;
    }

}