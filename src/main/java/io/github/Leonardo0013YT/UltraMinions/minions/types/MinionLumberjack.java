package io.github.Leonardo0013YT.UltraMinions.minions.types;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionPlaceEvent;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraMinions.utils.MinionUtils_1_19_R1;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MinionLumberjack extends Minion {

    private List<Vector> woods = Arrays.asList(new Vector(2, 0, 2), new Vector(-2, 0, 2), new Vector(2, 0, -2), new Vector(-2, 0, -2), new Vector(2, 0, 0), new Vector(0, 0, 2), new Vector(-2, 0, 0), new Vector(0, 0, -2));

    public MinionLumberjack(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
    }

    @Override
    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        if (armor == null || pm == null) return;
        ItemStack material = getPlace();
        Location l = pm.getSelected("SAPPLING");
        if (l != null) {
            if (!plugin.getAdm().isStackable(l)) {
                String t = material.getType().name().split("_")[0];
                if (t.startsWith("DARK")) {
                    t = "DARK_OAK";
                }
                Material sapling = Material.valueOf(t + "_SAPLING");
                armor.setItemInHand(new ItemStack(sapling, 1));
                l.getBlock().setType(sapling);
                Bukkit.getServer().getPluginManager().callEvent(new MinionPlaceEvent(pm, l.getBlock()));
            }
            action.done(false);
        } else {
            Location lo = pm.getSelected("BUILD");
            if (lo != null) {
                if (!plugin.getAdm().isStackable(lo)) {
                    armor.setItemInHand(ItemBuilder.item(Material.BONE_MEAL, 1, (short) 15, "", ""));
                    buildTree(lo, material.getType());
                    stat.setGenerated(stat.getGenerated() + 3);
                }
                action.done(false);
            } else {
                armor.setItemInHand(getHandItem());
                Location de = pm.getSelected("DESTROY");
                if (de != null) {
                    if (!plugin.getAdm().isStackable(de)) {
                        destroyTree(de);
                        MinionUtils_1_19_R1.damageBlock(de.getBlock().getLocation(), -1);
                    }
                    action.done(true);
                }
            }
        }
    }

    @Override
    public boolean check(Location spawn) {
        boolean yes = true;
        for (Vector l : woods) {
            Location lo = spawn.clone().add(l.getX(), l.getY() - 1, l.getZ());
            Material t = lo.getBlock().getType();
            if (t.equals(Material.DIRT) || t.equals(Material.GRASS_BLOCK)) {
                continue;
            }
            yes = false;
        }
        return yes;
    }

    public Location checkArroundWood(Location loc) {
        for (Vector l : woods) {
            Location lo1 = loc.clone().add(l.getX(), l.getY(), l.getZ());
            Location lo2 = loc.clone().add(l.getX(), l.getY() + 1, l.getZ());
            Location lo3 = loc.clone().add(l.getX(), l.getY() + 2, l.getZ());
            if (lo1.getBlock().getType().name().contains("SAPLING")) {
                continue;
            }
            if (lo1.getBlock().getType().equals(Material.AIR) || lo2.getBlock().getType().equals(Material.AIR) || lo3.getBlock().getType().equals(Material.AIR)) {
                return lo1;
            }
        }
        return null;
    }

    public Location checkArroundSappling(Location loc) {
        for (Vector l : woods) {
            Location lo = loc.clone().add(l.getX(), l.getY(), l.getZ());
            if (lo.getBlock().getType().name().contains("SAPLING")) {
                return lo;
            }
        }
        return null;
    }

    public Location getArroundRandomWood(Location loc) {
        return loc.clone().add(woods.get(ThreadLocalRandom.current().nextInt(woods.size())));
    }

    public void destroyTree(Location loc) {
        Location t1 = loc.clone();
        Location t2 = loc.clone().add(0, 1, 0);
        Location t3 = loc.clone().add(0, 2, 0);
        t1.getBlock().setType(Material.AIR);
        t2.getBlock().setType(Material.AIR);
        t3.getBlock().setType(Material.AIR);
    }

    public void buildTree(Location loc, Material material) {
        String t = material.name().split("_")[0];
        if (t.startsWith("DARK")) {
            t = "DARK_OAK";
        }
        Material sapling = Material.valueOf(t + "_LEAVES");
        Material log = Material.valueOf(t + "_LOG");
        Location t1 = loc.clone();
        Location t2 = loc.clone().add(0, 1, 0);
        Location t3 = loc.clone().add(0, 2, 0);
        Location l1 = loc.clone().add(1, 2, 0);
        Location l2 = loc.clone().add(-1, 2, 0);
        Location l3 = loc.clone().add(0, 2, -1);
        Location l4 = loc.clone().add(0, 2, 1);
        Location l5 = loc.clone().add(0, 3, 0);
        t1.getBlock().setType(log);
        t2.getBlock().setType(log);
        t3.getBlock().setType(log);
        l1.getBlock().setType(sapling);
        l2.getBlock().setType(sapling);
        l3.getBlock().setType(sapling);
        l4.getBlock().setType(sapling);
        l5.getBlock().setType(sapling);
    }

}