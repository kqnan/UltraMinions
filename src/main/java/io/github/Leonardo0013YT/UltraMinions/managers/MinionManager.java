package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerData;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionUpgrade;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionType;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.minions.types.*;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSell;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSmelt;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeCompressor;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;
import io.github.Leonardo0013YT.UltraMinions.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class MinionManager {

    private Main plugin;
    private Map<Player, PlayerMinion> view = new HashMap<>();
    private Map<String, Minion> minions = new HashMap<>();
    private Map<UUID, PlayerMinion> activeMinions = new HashMap<>();
    private Map<UUID, PlayerMinion> minionsToLoad = new HashMap<>();
    private HashSet<UUID> minionsToRemove = new HashSet<>();
    private Collection<PlayerMinion> toSpawn = new ArrayList<>();

    public MinionManager(Main plugin) {
        this.plugin = plugin;
        loadMinions();
    }

    public static void createMinion(ItemStack item, int level, int generated, int food, int health, int workTime, int sleep, long fueltime, PlayerMinion pm, PlayerMinionUpgrade upgrade, String autosell, Main plugin) {
        PlayerMinionStat pms = new PlayerMinionStat(pm, level, generated, workTime, sleep, food, health, fueltime);
        if (autosell != null) {
            UpgradeAutoSell autoSell = plugin.getUm().getAutoSell(autosell);
            if (autoSell != null) {
                upgrade.setAutoSell(autoSell);
            }
        }
        String autosmelt = NBTEditor.getString(item, "AUTOSMELT");
        if (autosmelt != null) {
            UpgradeAutoSmelt autoSmelt = plugin.getUm().getAutoSmelt(autosmelt);
            if (autoSmelt != null) {
                upgrade.setAutoSmelt(autoSmelt);
            }
        }
        String compressor = NBTEditor.getString(item, "COMPRESSOR");
        if (compressor != null) {
            UpgradeCompressor Compressor = plugin.getUm().getCompressor(compressor);
            if (Compressor != null) {
                upgrade.setCompressor(Compressor);
            }
        }
        String fuel = NBTEditor.getString(item, "FUEL");
        if (fuel != null) {
            UpgradeFuel Fuel = plugin.getUm().getFuel(fuel);
            if (Fuel != null) {
                upgrade.setFuel(Fuel);
            }
        }
        pm.setStat(pms);
        pm.setUpgrade(upgrade);
    }

    public void loadMinions() {
        minions.clear();
        File dataFolder = new File(plugin.getDataFolder(), "minions");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        for (File f : dataFolder.listFiles()) {
            String s = f.getName().replaceAll(".yml", "");
            YamlConfiguration minion = YamlConfiguration.loadConfiguration(f);
            MinionType type = MinionType.valueOf(minion.getString("minions." + s + ".type"));
            String key = minion.getString("minions." + s + ".key");
            plugin.sendDebugMessage("§dTrying load §a" + key + "§d.");
            if (type.equals(MinionType.MINER)) {
                minions.put(key, new MinionMiner(plugin, minion, "minions." + s, f));
            } else if (type.equals(MinionType.FARMER)) {
                minions.put(key, new MinionFarmer(plugin, minion, "minions." + s, f));
            } else if (type.equals(MinionType.FISHER)) {
                minions.put(key, new MinionFisher(plugin, minion, "minions." + s, f));
            } else if (type.equals(MinionType.HUNTER)) {
                minions.put(key, new MinionHunter(plugin, minion, "minions." + s, f));
            } else if (type.equals(MinionType.RANCHER)) {
                minions.put(key, new MinionRancher(plugin, minion, "minions." + s, f));
            } else if (type.equals(MinionType.LUMBERJACK)) {
                minions.put(key, new MinionLumberjack(plugin, minion, "minions." + s, f));
            } else if (type.equals(MinionType.PEASANT)) {
                minions.put(key, new MinionPeasant(plugin, minion, "minions." + s, f));
            } else if (type.equals(MinionType.CACTUSCANE)) {
                minions.put(key, new MinionCactusCane(plugin, minion, "minions." + s, f));
            } else if (type.equals(MinionType.COLLECTOR)) {
                minions.put(key, new MinionCollector(plugin, minion, "minions." + s, f));
            } else if (type.equals(MinionType.SELLER)) {
                minions.put(key, new MinionSeller(plugin, minion, "minions." + s, f));
            }
            plugin.sendDebugMessage("§dMinion §a" + s + "§d has been loaded.");
        }
    }

    public HashSet<UUID> getMinionsToRemove() {
        return minionsToRemove;
    }

    public Map<String, Minion> getMinions() {
        return minions;
    }

    public Minion getMinion(String key) {
        return minions.get(key);
    }

    public void setView(Player p, PlayerMinion pm) {
        view.put(p, pm);
    }

    public PlayerMinion getView(Player p) {
        return view.get(p);
    }

    public void removeView(Player p) {
        view.remove(p);
    }

    public Collection<PlayerMinion> getToSpawn() {
        return toSpawn;
    }

    public void close(PlayerMinion pm) {
        HashMap<Player, PlayerMinion> cloneView = new HashMap<>(view);
        for (Player on : cloneView.keySet()) {
            if (on == null || !on.isOnline()) {
                view.remove(on);
                continue;
            }
            if (cloneView.get(on).equals(pm)) {
                on.closeInventory();
                view.remove(on);
            }
        }
    }

    public Map<UUID, PlayerMinion> getActiveMinions() {
        return activeMinions;
    }

    public Map<UUID, PlayerMinion> getMinionsToLoad() {
        return minionsToLoad;
    }

    public void createIslandMinion(Player p, Location miLoc) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Minion m = plugin.getMm().getMinion(plugin.getCfm().getMinionKey());
                if (m == null) {
                    p.sendMessage("§cThe selected minion does not exist.");
                    return;
                }
                ItemStack item = m.getMinionLevelByLevel(plugin.getCfm().getMinionLevel()).getMinionHead();
                int level = NBTEditor.getInt(item, "LEVEL");
                int generated = NBTEditor.getInt(item, "GENERATED");
                int food = NBTEditor.getInt(item, "FOOD");
                int health = NBTEditor.getInt(item, "HEALTH");
                int workTime = NBTEditor.getInt(item, "WORKTIME");
                int sleep = NBTEditor.getInt(item, "SLEEP");
                long fueltime = NBTEditor.getLong(item, "FUELTIME");
                String skin = NBTEditor.contains(item, "SKIN") ? NBTEditor.getString(item, "SKIN") : "none";
                PlayerMinion pm = new PlayerMinion(miLoc, plugin.getCfm().getMinionKey(), p);
                PlayerMinionUpgrade upgrade = new PlayerMinionUpgrade(pm);
                String autosell = NBTEditor.getString(item, "AUTOSELL");
                createMinion(item, level, generated, food, health, workTime, sleep, fueltime, pm, upgrade, autosell, plugin);
                PlayerData pd = PlayerData.getPlayerData(p);
                pd.addPlayerMinion(pm);
                pm.firstSpawn();
                pm.setSkin(skin);
            }
        }.runTaskLater(plugin, 30);
    }

    public void removeIslandMinion(Entity ent) {
        ArmorStand as = (ArmorStand) ent;
        UUID uuid = as.getUniqueId();
        if (Utils.isMinionUUID(uuid)) {
            if (plugin.getMm().getActiveMinions().containsKey(uuid)) {
                PlayerMinion pm = plugin.getMm().getActiveMinions().get(uuid);
                if (remove(pm)) return;
                plugin.getMm().getActiveMinions().remove(uuid);
            }
            if (plugin.getMm().getMinionsToLoad().containsKey(uuid)) {
                PlayerMinion pm = plugin.getMm().getMinionsToLoad().get(uuid);
                if (remove(pm)) return;
                plugin.getMm().getMinionsToLoad().remove(uuid);
            }
        }
    }

    private boolean remove(PlayerMinion pm) {
        if (pm == null || !pm.getP().isOnline()) return true;
        PlayerData pd = PlayerData.getPlayerUUID(pm.getP().getUniqueId());
        MinionLevel ml = pm.getMinionLevel();
        pm.destroy();
        pd.removePlayerMinion(pm);
        if (pm.getP().getInventory().firstEmpty() == -1) {
            pm.getP().getWorld().dropItem(pm.getP().getLocation(), ml.getMinionHead(pm));
        } else {
            pm.getP().getInventory().addItem(ml.getMinionHead(pm));
        }
        Utils.addItems(pm.getP(), pm.getItems(), pm.getMinionLevel().getMax());
        pm.setActions(0);
        return false;
    }

}