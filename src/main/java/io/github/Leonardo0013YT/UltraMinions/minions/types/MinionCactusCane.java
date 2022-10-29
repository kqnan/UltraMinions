package io.github.Leonardo0013YT.UltraMinions.minions.types;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionPlaceEvent;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MinionCactusCane extends Minion {

    private ItemStack place;
    private List<Vector> woods = Arrays.asList(new Vector(2, 0, 2), new Vector(-2, 0, 2), new Vector(2, 0, -2), new Vector(-2, 0, -2), new Vector(2, 0, 0), new Vector(0, 0, 2), new Vector(-2, 0, 0), new Vector(0, 0, -2));
    private List<Vector> water = Arrays.asList(new Vector(-1, 0, -2), new Vector(-2, 0, -1), new Vector(-2, 0, 1), new Vector(-1, 0, 2), new Vector(1, 0, 2), new Vector(2, 0, 1), new Vector(2, 0, -1), new Vector(1, 0, -2));

    public MinionCactusCane(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
        this.place = getPlace();
    }

    @Override
    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        if (armor == null || pm == null) return;
        ItemStack material = getPlace();
        Location l = pm.getSelected("SAPPLING");
        if (l != null) {
            if (!plugin.getAdm().isStackable(l)) {
                if (!l.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
                    pm.setEnabled(false);
                    pm.getP().sendMessage(plugin.getLang().get("messages.noPerfect"));
                    return;
                }
                if (material.getType().equals(Material.SUGAR_CANE)) {
                    armor.setItemInHand(new ItemStack(Material.SUGAR_CANE));
                    buildTree(l, Material.SUGAR_CANE);
                } else if (material.getType().equals(Material.CACTUS)) {
                    armor.setItemInHand(new ItemStack(Material.CACTUS));
                    buildTree(l, Material.CACTUS);
                } else {
                    if (Utils.is1_14to1_16()) {
                        armor.setItemInHand(new ItemStack(Material.valueOf("BAMBOO")));
                        buildTree(l, Material.valueOf("BAMBOO"));
                    }
                }
                stat.setGenerated(stat.getGenerated() + 3);
                Bukkit.getServer().getPluginManager().callEvent(new MinionPlaceEvent(pm, l.getBlock()));
            }
            action.done(false);
        } else {
            armor.setItemInHand(getHandItem());
            Location de = pm.getSelected("DESTROY");
            if (de != null) {
                if (!plugin.getAdm().isStackable(de)) {
                    destroyTree(de);
                }
            }
            action.done(true);
        }
    }

    @Override
    public boolean check(Location spawn) {
        boolean yes = true;
        for (Vector l : water) {
            Location lo = spawn.clone().add(l.getX(), l.getY() - 1, l.getZ());
            Material t = lo.getBlock().getType();
            Material p = place.getType();
            if (p.equals(Material.CACTUS)) {
                continue;
            }
            if (Utils.is1_14to1_16() && p.equals(Material.valueOf("BAMBOO"))) {
                continue;
            }
            if (t.equals(Material.WATER)) {
                continue;
            }

            yes = false;
        }
        for (Vector l : woods) {
            Location lo = spawn.clone().add(l.getX(), l.getY() - 1, l.getZ());
            Material t = lo.getBlock().getType();
            Material p = place.getType();
            if (p.equals(Material.CACTUS) && t.equals(Material.SAND)) {
                continue;
            }
            if (p.equals(Material.SUGAR_CANE) && t.equals(Material.GRASS_BLOCK)) {
                continue;
            }
            if (Utils.is1_14to1_16() && p.equals(Material.valueOf("BAMBOO")) && t.equals(Material.GRASS_BLOCK)) {
                continue;
            }

            yes = false;
        }
        return yes;
    }

    public Location checkArroundAir(Location loc) {
        for (Vector l : woods) {
            Location lo = loc.clone().add(l.getX(), l.getY(), l.getZ());
            if (lo.getBlock().getType().equals(Material.AIR)) {
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
        t3.getBlock().setType(Material.AIR);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.get(), () -> t2.getBlock().setType(Material.AIR), 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.get(), () -> t1.getBlock().setType(Material.AIR), 2L);
    }

    public void buildTree(Location loc, Material material) {
        Location t1 = loc.clone();
        Location t2 = loc.clone().add(0, 1, 0);
        Location t3 = loc.clone().add(0, 2, 0);
        t1.getBlock().setType(material);
        t2.getBlock().setType(material);
        t3.getBlock().setType(material);
    }

}