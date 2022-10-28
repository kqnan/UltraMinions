package io.github.Leonardo0013YT.UltraMinions.database;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.customs.CVector;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionChest;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionUpgrade;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionFace;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionType;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.animations.BlockBreakAnimation;
import io.github.Leonardo0013YT.UltraMinions.minions.animations.CropsUpgradeAnimation;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.minions.skins.MinionSkin;
import io.github.Leonardo0013YT.UltraMinions.minions.types.*;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;
import io.github.Leonardo0013YT.UltraMinions.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PlayerMinion {

    private Main plugin;
    private Player p;
    private HashMap<ItemStack, Integer> items = new HashMap<>();
    private ArmorStand armor;
    private Minion minion;
    private Location spawn;
    private PlayerMinionStat stat;
    private PlayerMinionUpgrade upgrade;
    private PlayerMinionChest chest;
    private String key, skin, id;
    private int action, actions = 0;
    private MinionType type;
    private MinionFace minionFace;
    private MinionLevel minionLevel;
    private HashMap<String, CVector> locations = new HashMap<>();
    private boolean enabled = true, full = false, isChest = false, loaded = false;

    public PlayerMinion(Location spawn, String key, Player p) {
        this.plugin = Main.get();
        this.p = p;
        this.spawn = spawn;
        this.key = key;
        this.minionFace = Utils.getCardinalDirection(p);
        this.spawn.setYaw(minionFace.getYaw());
        this.id = Utils.getLocationString(spawn);
        create();
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public String getId() {
        return id;
    }

    public void create() {
        this.skin = "none";
        this.minion = plugin.getMm().getMinion(key);
        this.type = minion.getType();
        this.enabled = minion.check(spawn);
    }

    public void firstSpawn() {
        if (minionLevel == null) {
            p.sendMessage(plugin.getLang().get("messages.noExistLevel"));
            return;
        }
        if (!spawn.getBlock().getType().equals(Material.BEDROCK) && !spawn.getBlock().getType().equals(Material.ENDER_CHEST) && !spawn.getBlock().getType().equals(Material.CHEST) && !spawn.getBlock().getType().equals(Material.TRAPPED_CHEST)) {
            spawn.getBlock().setType(Material.AIR);
        }
        ArrayList<Entity> entities = spawn.getWorld().getNearbyEntities(spawn, 0.5, 0.5, 0.5).stream().filter(entity -> entity.getType().equals(EntityType.ARMOR_STAND)).collect(Collectors.toCollection(ArrayList::new));
        entities.forEach(Entity::remove);
        armor = spawn.getWorld().spawn(spawn, ArmorStand.class);
        armor.setHelmet(minionLevel.getHead());
        ItemStack[] ar = ItemBuilder.getArmorMinion(minion.getRed(), minion.getBlue(), minion.getGreen());
        armor.setChestplate(ar[0]);
        armor.setLeggings(ar[1]);
        armor.setBoots(ar[2]);
        armor.setVisible(true);
        armor.setGravity(false);
        armor.setArms(true);
        armor.setBasePlate(false);
        armor.setSmall(true);
        armor.setCustomNameVisible(false);
        setSkin(skin);
        plugin.getMm().getActiveMinions().put(armor.getUniqueId(), this);
        Utils.addMinionUUID(armor.getUniqueId());
        this.enabled = minion.check(spawn);
        loaded = true;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
        plugin.getAdm().deleteHologram(this);
        if (full) {
            plugin.getAdm().createHologram(this, spawn, plugin.getHm().getFullyMessage(type));
        }
    }

    public void destroy() {
        plugin.getAdm().deleteHologram(this);
        if (armor != null) {
            Utils.removeMinionUUID(armor.getUniqueId());
            armor.remove();
        }
        if (armor != null) {
            plugin.getMm().getMinionsToRemove().add(armor.getUniqueId());
        }
        try {
            ArrayList<Entity> entities = spawn.getWorld().getNearbyEntities(spawn, 0.5, 0.5, 0.5).stream().filter(entity -> entity.getType().equals(EntityType.ARMOR_STAND)).collect(Collectors.toCollection(ArrayList::new));
            entities.forEach(Entity::remove);
        } catch (IllegalStateException e) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ArrayList<Entity> entities = spawn.getWorld().getNearbyEntities(spawn, 0.5, 0.5, 0.5).stream().filter(entity -> entity.getType().equals(EntityType.ARMOR_STAND)).collect(Collectors.toCollection(ArrayList::new));
                entities.forEach(Entity::remove);
            });
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void update() {
        if (armor == null) {
            plugin.getAdm().deleteHologram(this);
            return;
        }
        if (!enabled) {
            if (!plugin.getAdm().hasHologram(this) && loaded) {
                plugin.getAdm().createHologram(this, spawn, plugin.getAdm().parsePlaceholders(p, plugin.getHm().getNoCorrectMessage(type)));
            }
            return;
        }
        if (full) {
            return;
        }
        action++;
        if (plugin.getCfm().isStopOnlyFoodLow() && plugin.getCfm().isFood() && stat.getFood() <= 0) {
            return;
        }
        if (plugin.getCfm().isHealth() && plugin.getCfm().isFood()) {
            if (stat.getHealth() <= 0 && stat.getFood() <= 0) {
                if (!plugin.getAdm().hasHologram(this) && loaded) {
                    plugin.getAdm().createHologram(this, spawn, plugin.getAdm().parsePlaceholders(p, plugin.getHm().getNoHealthNoFood(type)));
                }
                return;
            }
        } else if (plugin.getCfm().isHealth()) {
            if (stat.getHealth() <= 0) {
                if (!plugin.getAdm().hasHologram(this) && loaded) {
                    plugin.getAdm().createHologram(this, spawn, plugin.getAdm().parsePlaceholders(p, plugin.getHm().getNoHealthNoFood(type)));
                }
                return;
            }
        }
        if (action == getDelay() - 3) {
            if (plugin.getCfm().isOptimizeOnUnloadChunk()) {
                if (loaded) {
                    executeAction();
                }
            } else {
                executeAction();
            }
        }
        if (action >= getDelay()) {
            action = 0;
            if (plugin.getCfm().isFood()) {
                stat.removeFood(1);
            }
            if (full) {
                if (loaded) {
                    if (!plugin.getAdm().hasHologram(this)) {
                        plugin.getAdm().createHologram(this, spawn, plugin.getAdm().parsePlaceholders(p, plugin.getHm().getFullyMessage(type)));
                    }
                }
            } else {
                if (plugin.getCfm().isOptimizeOnUnloadChunk()) {
                    if (loaded) {
                        executeFood();
                    }
                } else {
                    executeFood();
                }
            }
            minion.update(this, armor, stat, spawn, b -> {
                if (b) {
                    actions++;
                }
            });
        }
    }

    private void executeFood() {
        if (plugin.getCfm().isFood() && plugin.getCfm().isHealth()) {
            if (stat.getFood() < 10 && stat.getHealth() > 10) {
                plugin.getAdm().deleteHologram(this);
                plugin.getAdm().createHologram(this, spawn, plugin.getAdm().parsePlaceholders(p, plugin.getHm().getLowFoodMessage(type)));
            }
        } else if (plugin.getCfm().isHealth()) {
            if (stat.getHealth() < 10) {
                plugin.getAdm().deleteHologram(this);
                plugin.getAdm().createHologram(this, spawn, plugin.getAdm().parsePlaceholders(p, plugin.getHm().getLowHealthMessage(type)));
            }
        } else {
            int random = ThreadLocalRandom.current().nextInt(0, 10);
            if (!full) {
                if (plugin.getCfm().isSocialHolograms()) {
                    plugin.getAdm().deleteHologram(this);
                    if (random < 3) {
                        plugin.getAdm().createHologram(this, spawn, plugin.getAdm().parsePlaceholders(p, plugin.getHm().getSocialMessage(type)));
                    }
                }
            }
        }
    }

    private void executeAction() {
        Location armorC = armor.getLocation();
        Location s = null;
        if (minion instanceof MinionPeasant) {
            MinionPeasant mm = (MinionPeasant) minion;
            Location l = mm.checkFamerDirt(spawn);
            if (l != null) {
                s = l;
                locations.put("DIRT", new CVector(l.toVector()));
            } else {
                Location pr = mm.checkFamerProduct(spawn);
                if (pr != null) {
                    s = pr;
                    locations.put("PRODUCT", new CVector(pr.toVector()));
                } else {
                    Location fr = mm.getArroundRandomFarmer(spawn);
                    if (fr != null) {
                        s = fr;
                        locations.put("FARMER", new CVector(fr.toVector()));
                    } else {
                        Location rd = mm.checkFarmerBlock(spawn);
                        if (rd != null) {
                            s = rd;
                            locations.put("BLOCK", new CVector(rd.toVector()));
                        } else {
                            List<Location> bll = mm.getArroundRandomReady(spawn);
                            Location bl = null;
                            if (!bll.isEmpty()) {
                                bl = bll.get(0);
                            }
                            if (bl != null) {
                                s = bl;
                                locations.put("READY", new CVector(bl.toVector()));
                            }
                        }
                    }
                }
            }
        }
        if (minion instanceof MinionCactusCane) {
            MinionCactusCane mm = (MinionCactusCane) minion;
            Location l = mm.checkArroundAir(spawn);
            if (l != null) {
                s = l;
                locations.put("SAPPLING", new CVector(l.toVector()));
            } else {
                Location de = mm.getArroundRandomWood(spawn);
                s = de;
                plugin.getAm().addBlockAnimation(new BlockBreakAnimation(de.getBlock()));
                locations.put("DESTROY", new CVector(de.toVector()));
            }
        }
        if (minion instanceof MinionLumberjack) {
            MinionLumberjack mm = (MinionLumberjack) minion;
            Location l = mm.checkArroundWood(spawn);
            if (l != null) {
                s = l;
                locations.put("SAPPLING", new CVector(l.toVector()));
            } else {
                Location lo = mm.checkArroundSappling(spawn);
                if (lo != null) {
                    s = lo;
                    lo.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, lo, 0);
                    locations.put("BUILD", new CVector(lo.toVector()));
                } else {
                    Location de = mm.getArroundRandomWood(spawn);
                    s = de;
                    plugin.getAm().addBlockAnimation(new BlockBreakAnimation(de.getBlock()));
                    locations.put("DESTROY", new CVector(de.toVector()));
                }
            }
        }
        if (minion instanceof MinionMiner) {
            MinionMiner mm = (MinionMiner) minion;
            Location l = mm.checkAround(spawn);
            if (l != null) {
                s = l;
                locations.put("PLACE", new CVector(l.toVector()));
            } else {
                Location lo = mm.getAroundRandom(spawn);
                s = lo;
                plugin.getAm().addBlockAnimation(new BlockBreakAnimation(lo.getBlock()));
                locations.put("BREAK", new CVector(lo.toVector()));
            }
        }
        if (minion instanceof MinionFarmer) {
            MinionFarmer mm = (MinionFarmer) minion;
            Location l = mm.checkFamerDirt(spawn);
            if (mm.getPlace().getType().equals(Material.NETHER_WART)) {
                l = null;
            }
            if (l != null) {
                s = l;
                locations.put("DIRT", new CVector(l.toVector()));
            } else {
                Location lo = mm.checkFamerProduct(spawn);
                if (lo != null) {
                    s = lo;
                    locations.put("PRODUCT", new CVector(lo.toVector()));
                    plugin.getAm().addBlockAnimation(new CropsUpgradeAnimation(lo.getBlock()));
                } else {
                    Location mt = mm.getArroundRandomFarmer(spawn);
                    if (mt != null) {
                        s = mt;
                        locations.put("FARMER", new CVector(mt.toVector()));
                    } else {
                        Location mf = mm.getArroundRandomReady(spawn);
                        if (mf != null) {
                            s = mf;
                            locations.put("READY", new CVector(mf.toVector()));
                        }
                    }
                }
            }
        }
        if (s != null) {
            Vector toNpc = s.toVector().subtract(armorC.toVector());
            armorC.setDirection(toNpc);
            armor.teleport(armorC);
        }
        plugin.getAm().execute(minion.getAnimation(), armor);
    }

    public Location getSelected(String fase) {
        if (!locations.containsKey(fase)) return null;
        CVector c = locations.get(fase);
        locations.clear();
        return spawn.clone().zero().add(c.getX(), c.getY(), c.getZ());
    }

    public int getActions() {
        return actions;
    }

    public void setActions(int actions) {
        this.actions = actions;
    }

    public int getDelay() {
        UpgradeFuel uf = getUpgrade().getFuel();
        int delay = minionLevel.getDelay();
        double reduction = 0;
        if (uf != null) {
            if (!plugin.getMem().ended(stat, uf)) {
                reduction = delay * (uf.getPercent() / 100);
            } else {
                getUpgrade().setFuel(null);
            }
        }
        return (int) (delay - reduction);
    }

    public HashMap<ItemStack, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<ItemStack, Integer> items) {
        this.items = items;
    }

    public MinionLevel getMinionLevel() {
        return minionLevel;
    }

    public MinionFace getMinionFace() {
        return minionFace;
    }

    public MinionType getType() {
        return type;
    }

    public void setType(MinionType type) {
        this.type = type;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
        if (armor == null) {
            return;
        }
        if (skin.equals("none")) {
            armor.setHelmet(minionLevel.getHead());
            ItemStack[] ar = ItemBuilder.getArmorMinion(minion.getRed(), minion.getGreen(), minion.getBlue());
            armor.setChestplate(ar[0]);
            armor.setLeggings(ar[1]);
            armor.setBoots(ar[2]);
            return;
        }
        MinionSkin minionSkin = plugin.getSkm().getSkins().get(skin);
        armor.setHelmet(minionSkin.getHead());
        ItemStack[] ar = ItemBuilder.getArmorMinion(minionSkin.getRed(), minionSkin.getGreen(), minionSkin.getBlue());
        armor.setChestplate(ar[0]);
        armor.setLeggings(ar[1]);
        armor.setBoots(ar[2]);
    }

    public PlayerMinionChest getChest() {
        if (!plugin.getCfm().isChestLink()) return null;
        return chest;
    }

    public boolean isChest() {
        if (!plugin.getCfm().isChestLink()) return false;
        return isChest;
    }

    public void setChest(PlayerMinionChest chest) {
        this.chest = chest;
    }

    public void setChest(boolean chest) {
        isChest = chest;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        this.minion = plugin.getMm().getMinion(key);
    }

    public Player getP() {
        return p;
    }

    public PlayerMinionStat getStat() {
        return stat;
    }

    public void setStat(PlayerMinionStat stat) {
        this.stat = stat;
        this.minionLevel = minion.getMinionLevelByLevel(stat.getLevel());
        if (skin.equals("none") && armor != null && loaded) {
            armor.setHelmet(minionLevel.getHead());
        }
    }

    public PlayerMinionUpgrade getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(PlayerMinionUpgrade upgrade) {
        this.upgrade = upgrade;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public ArmorStand getArmor() {
        return armor;
    }

    public Minion getMinion() {
        return minion;
    }

    public int getSpaces() {
        int spaces = minionLevel.getMax() / 64;
        if (spaces < 1) {
            spaces++;
        }
        return spaces;
    }

}