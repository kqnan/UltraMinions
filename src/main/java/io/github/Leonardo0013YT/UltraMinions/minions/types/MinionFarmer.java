package io.github.Leonardo0013YT.UltraMinions.minions.types;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionBreakEvent;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionPlaceEvent;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class MinionFarmer extends Minion {

    private ArrayList<Vector> farmers = new ArrayList<>(Arrays.asList(new Vector(1, -1, 0), new Vector(0, -1, 1), new Vector(1, -1, 1), new Vector(-1, -1, 0), new Vector(0, -1, -1), new Vector(-1, -1, -1), new Vector(1, -1, -1), new Vector(-1, -1, 1), new Vector(2, -1, 0), new Vector(2, -1, -1), new Vector(2, -1, 1), new Vector(-2, -1, 0), new Vector(-2, -1, -1), new Vector(-2, -1, 1), new Vector(0, -1, 2), new Vector(1, -1, 2), new Vector(-1, -1, 2), new Vector(0, -1, -2), new Vector(1, -1, -2), new Vector(-1, -1, -2), new Vector(2, -1, -2), new Vector(2, -1, 2), new Vector(-2, -1, -2), new Vector(-2, -1, 2)));

    public MinionFarmer(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
    }

    @Override
    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        if (armor == null || pm == null) return;
        ItemStack material = getPlace();
        Location l = pm.getSelected("DIRT");
        if (material.getType().equals(Material.NETHER_WART)) {
            l = null;
        }
        if (l != null) {
            if (!plugin.getAdm().isStackable(l) && !material.getType().equals(Material.NETHER_WART)) {
                armor.setItemInHand(getHandItem());
                l.getBlock().setType(Material.FARMLAND);
                Farmland f = (Farmland) l.getBlock().getBlockData();
                f.setMoisture(7);
            }
            action.done(false);
        } else {
            Location lo = pm.getSelected("PRODUCT");
            if (lo != null) {
                if (!plugin.getAdm().isStackable(lo)) {
                    MinionPlaceEvent mpe = new MinionPlaceEvent(pm, lo.getBlock());
                    Bukkit.getServer().getPluginManager().callEvent(mpe);
                    if (material.getType().equals(Material.WHEAT_SEEDS) || material.getType().equals(Material.WHEAT)) {
                        lo.getBlock().setType(Material.WHEAT);
                    } else if (material.getType().equals(Material.CARROT)) {
                        lo.getBlock().setType(Material.CARROTS);
                    } else if (material.getType().equals(Material.POTATO)) {
                        lo.getBlock().setType(Material.POTATOES);
                    } else if (material.getType().equals(Material.BEETROOT)) {
                        lo.getBlock().setType(Material.BEETROOTS);
                    } else if (material.getType().equals(Material.NETHER_WART)) {
                        lo.getBlock().setType(Material.NETHER_WART);
                    } else {
                        lo.getBlock().setType(material.getType());
                    }
                    stat.setGenerated(stat.getGenerated() + 1);
                    armor.setItemInHand(new ItemStack(material));
                }
                action.done(false);
            } else {
                Location mt = pm.getSelected("FARMER");
                if (mt != null && Utils.isMax(mt.getBlock())) {
                    mt = null;
                }
                if (mt != null) {
                    if (!plugin.getAdm().isStackable(mt)) {
                        armor.setItemInHand(new ItemStack(Material.BONE_MEAL, 1));
                        if (material.getType().equals(Material.WHEAT_SEEDS) || material.getType().equals(Material.WHEAT) || material.getType().equals(Material.BEETROOT) || material.getType().equals(Material.CARROT) || material.getType().equals(Material.POTATO)) {
                            BlockData data = mt.getBlock().getBlockData();
                            if (data instanceof Ageable) {
                                Ageable ag = (Ageable) data;
                                ag.setAge(7);
                                mt.getBlock().setBlockData(ag);
                            }
                        } else if (material.getType().equals(Material.NETHER_WART)) {
                            BlockData data = mt.getBlock().getBlockData();
                            if (data instanceof Ageable) {
                                Ageable ag = (Ageable) data;
                                ag.setAge(3);
                                mt.getBlock().setBlockData(ag);
                            }
                        } else {
                            mt.getBlock().setType(material.getType());
                        }
                    }
                    action.done(false);
                } else {
                    armor.setItemInHand(new ItemStack(material));
                    Location ll = pm.getSelected("READY");
                    Location mf = (ll == null) ? getArroundRandomReady(spawn) : ll;
                    if (!plugin.getAdm().isStackable(mf)) {
                        mf.getBlock().setType(Material.AIR);
                        Bukkit.getServer().getPluginManager().callEvent(new MinionBreakEvent(pm, mf.getBlock()));
                    }
                    action.done(true);
                }
            }
        }
    }

    @Override
    public boolean check(Location spawn) {
        boolean yes = true;
        for (Vector l : farmers) {
            Location lo = spawn.clone().add(l.getX(), l.getY(), l.getZ());
            if (lo.getBlock().getType().equals(Material.SOUL_SAND) || lo.getBlock().getType().equals(Material.GRASS_BLOCK) || lo.getBlock().getType().equals(Material.GRASS) || lo.getBlock().getType().equals(Material.DIRT) || lo.getBlock().getType().equals(Material.valueOf("FARMLAND"))) {
                continue;
            }
            yes = false;
        }
        return yes;
    }

    public Location checkFamerDirt(Location loc) {
        for (Vector l : farmers) {
            Location lo = loc.clone().add(l.getX(), l.getY(), l.getZ());
            if (lo.getBlock().getType().equals(Material.SOUL_SAND) || lo.getBlock().getType().equals(Material.DIRT) || lo.getBlock().getType().equals(Material.GRASS) || lo.getBlock().getType().equals(Material.GRASS_BLOCK)) {
                return lo;
            }
        }
        return null;
    }

    public Location getArroundRandomFarmer(Location loc) {
        for (Vector l : farmers) {
            Location lo = loc.clone().add(l.getX(), l.getY() + 1, l.getZ());
            if (lo.getBlock().getType().equals(Material.NETHER_WART) && lo.getBlock().getData() != 3) {
                return lo;
            }
            if ((lo.getBlock().getType().equals(Material.WHEAT) || lo.getBlock().getType().equals(Material.BEETROOT_SEEDS) || lo.getBlock().getType().equals(Material.CARROTS) || lo.getBlock().getType().equals(Material.POTATOES)) && lo.getBlock().getData() != 7) {
                return lo;
            }
        }
        return null;
    }

    public Location checkFamerProduct(Location loc) {
        for (Vector l : farmers) {
            Location lo = loc.clone().add(l.getX(), l.getY() + 1, l.getZ());
            if (lo.getBlock().getType().equals(Material.AIR)) {
                return lo;
            }
        }
        return null;
    }

    public Location getArroundRandomReady(Location loc) {
        Location lo = loc.clone();
        lo.add(farmers.get(ThreadLocalRandom.current().nextInt(farmers.size())));
        return lo.clone().add(0, 1, 0);
    }

}