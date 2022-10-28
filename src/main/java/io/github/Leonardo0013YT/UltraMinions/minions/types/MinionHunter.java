package io.github.Leonardo0013YT.UltraMinions.minions.types;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class MinionHunter extends Minion {

    public MinionHunter(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
    }

    @Override
    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        if (armor == null || pm == null) return;
        if (!spawn.getChunk().isLoaded()) return;
        Collection<Entity> ents = spawn.getWorld().getNearbyEntities(spawn, Main.get().getCfm().getX(), Main.get().getCfm().getY(), Main.get().getCfm().getZ());
        ents.removeIf(e -> !e.getType().equals(getSpawn()));
        if (ents.size() < 2) {
            stat.setGenerated(stat.getGenerated() + 1);
            Entity entity = spawn.getWorld().spawnEntity(spawn, getSpawn());
            entity.setMetadata("MINION", new FixedMetadataValue(Main.get(), "HUNTER"));
            armor.setItemInHand(new ItemStack(Material.SPAWNER));
            action.done(false);
        } else {
            armor.setItemInHand(getHandItem());
            ArrayList<Entity> entities = new ArrayList<>(ents);
            Location armorC = armor.getLocation();
            Entity ent = entities.get(ThreadLocalRandom.current().nextInt(0, entities.size()));
            Vector toNpc = ent.getLocation().toVector().subtract(armorC.toVector());
            armorC.setDirection(toNpc);
            armor.teleport(armorC);
            if (ent instanceof Damageable) {
                Damageable en = (Damageable) ent;
                if (en.getHealth() - getDamage() <= 0) {
                    action.done(true);
                } else {
                    action.done(false);
                }
                en.damage(getDamage());
            }
        }
    }

    @Override
    public boolean check(Location spawn) {
        return true;
    }

}