package io.github.Leonardo0013YT.UltraMinions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.Leonardo0013YT.UltraMinions.adapters.InterfaceDataSave;
import io.github.Leonardo0013YT.UltraMinions.adapters.InterfaceMinionSave;
import io.github.Leonardo0013YT.UltraMinions.cmds.SetupCMD;
import io.github.Leonardo0013YT.UltraMinions.database.Database;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerData;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.interfaces.DataSave;
import io.github.Leonardo0013YT.UltraMinions.interfaces.MinionSave;
import io.github.Leonardo0013YT.UltraMinions.listeners.CraftListener;
import io.github.Leonardo0013YT.UltraMinions.listeners.MenuListener;
import io.github.Leonardo0013YT.UltraMinions.listeners.PlayerListener;
import io.github.Leonardo0013YT.UltraMinions.listeners.SetupListener;
import io.github.Leonardo0013YT.UltraMinions.managers.*;
import io.github.Leonardo0013YT.UltraMinions.menus.MinionMenu;
import io.github.Leonardo0013YT.UltraMinions.menus.SetupMenu;
import io.github.Leonardo0013YT.UltraMinions.placeholders.MVdWPlaceholders;
import io.github.Leonardo0013YT.UltraMinions.placeholders.Placeholders;
import io.github.Leonardo0013YT.UltraMinions.utils.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Gson dataSave, dataMinion;
    private boolean stop = false;
    private Settings lang, upgrades, skins, foods, tiers, shop, temp;
    private MinionManager mm;
    private SetupManager sm;
    private UpgradeManager um;
    private ConfigManager cfm;
    private AddonManager adm;
    private CraftManager cm;
    private AnimationManager am;
    private HologramManager hm;
    private FoodManager fm;
    private SkinManager skm;
    private TiersManager tm;
    private SetupMenu sem;
    private MinionMenu mem;
    private ShopManager shm;
    private BukkitTask task;
    private Database db;

    public static Main get() {
        return instance;
    }

    public static String toDataString(DataSave ds) {
        return dataSave.toJson(ds, DataSave.class);
    }

    public static DataSave fromDataString(String data) {
        return dataSave.fromJson(data, DataSave.class);
    }

    public static String toMinionString(MinionSave ms) {
        return dataMinion.toJson(ms, MinionSave.class);
    }

    public static MinionSave fromMinionString(String data) {
        return dataMinion.fromJson(data, MinionSave.class);
    }

    @Override
    public void onEnable() {
        instance = this;
        dataSave = new GsonBuilder().registerTypeAdapter(DataSave.class, new InterfaceDataSave()).create();
        dataMinion = new GsonBuilder().registerTypeAdapter(MinionSave.class, new InterfaceMinionSave()).create();
        setupSounds();
        getConfig().options().copyDefaults(true);
        saveConfig();
        File m = new File(getDataFolder(), "minions");
        saveAnimations();
        if (!m.exists()) {
            m.mkdirs();
            saveMinions();
        }
        hm = new HologramManager(this);
        temp = new Settings(this, "temp", true, false);
        lang = new Settings(this, "lang", true, true);
        foods = new Settings(this, "foods", false, false);
        upgrades = new Settings(this, "upgrades", false, false);
        skins = new Settings(this, "skins", false, false);
        tiers = new Settings(this, "tiers", false, false);
        shop = new Settings(this, "shop", false, false);
        hm.reload();
        db = new Database(this);
        cfm = new ConfigManager(this);
        cm = new CraftManager(this);
        mm = new MinionManager(this);
        sm = new SetupManager();
        um = new UpgradeManager(this);
        adm = new AddonManager(this);
        am = new AnimationManager(this);
        skm = new SkinManager(this);
        fm = new FoodManager(this);
        sem = new SetupMenu(this);
        mem = new MinionMenu(this);
        tm = new TiersManager(this);
        shm = new ShopManager(this);
        shm.loadShop();
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).register();
        }
        if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            new MVdWPlaceholders(this).register();
        }
        getCommand("msetup").setExecutor(new SetupCMD(this));
        getServer().getPluginManager().registerEvents(new SetupListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
        task = new BukkitRunnable() {
            @Override
            public void run() {
                new HashSet<>(PlayerData.getPlayers().values()).forEach(pd -> pd.getMinions().values().forEach(PlayerMinion::update));
            }
        }.runTaskTimer(this, 20, 20);
        if (getCfm().isAutoSaveEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    getDb().autoSave();
                }
            }.runTaskTimer(this, 20L * 60 * getCfm().getMinutesAutoSave(), 20L * 60 * getCfm().getMinutesAutoSave());
        }
        new MetricsLite(this, 9622);
    }

    @Override
    public void onDisable() {
        if (task != null) {
            task.cancel();
        }
        if (!getCfm().isSecureStop()) {
            ArrayList<PlayerData> uuids = new ArrayList<>(PlayerData.getPlayers().values());
            for (PlayerData pd : uuids) {
                getDb().savePlayerSync(pd.getUuid());
            }
        }
        getTemp().set("minionsData", null);
        getTemp().save();
        db.close();
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    private void setupSounds() {
        getConfig().addDefault("sounds.upgrade", "ENTITY_PLAYER_LEVELUP");
        getConfig().addDefault("sounds.noUpgrade", "ENTITY_ENDERMAN_TELEPORT");
    }

    private void saveAnimations() {
        if (checkFile("picar.animc")) {
            saveResource("picar.animc", false);
        }
        if (checkFile("lumberjack.animc")) {
            saveResource("lumberjack.animc", false);
        }
    }

    private void saveMinions() {
        if (checkFile("minions/cactus.yml")) {
            saveResource("minions/cactus.yml", false);
        }
        if (checkFile("minions/cobblestone.yml")) {
            saveResource("minions/cobblestone.yml", false);
        }
        if (checkFile("minions/fisher_minion.yml")) {
            saveResource("minions/fisher_minion.yml", false);
        }
        if (checkFile("minions/iron_ore.yml")) {
            saveResource("minions/iron_ore.yml", false);
        }
        if (checkFile("minions/wheat_minion.yml")) {
            saveResource("minions/wheat_minion.yml", false);
        }
        if (checkFile("minions/sugarcane.yml")) {
            saveResource("minions/sugarcane.yml", false);
        }
        if (checkFile("minions/nether_wart.yml")) {
            saveResource("minions/nether_wart.yml", false);
        }
        if (checkFile("minions/collector.yml")) {
            saveResource("minions/collector.yml", false);
        }
        if (checkFile("minions/seller.yml")) {
            saveResource("minions/seller.yml", false);
        }
        if (checkFile("minions/pig_minion.yml")) {
            saveResource("minions/pig_minion.yml", false);
        }
        if (checkFile("minions/skeleton_minion.yml")) {
            saveResource("minions/skeleton_minion.yml", false);
        }
        if (checkFile("minions/coal.yml")) {
            saveResource("minions/coal.yml", false);
        }
        if (checkFile("minions/cow.yml")) {
            saveResource("minions/cow.yml", false);
        }
        if (checkFile("minions/diamond.yml")) {
            saveResource("minions/diamond.yml", false);
        }
        if (checkFile("minions/emerald.yml")) {
            saveResource("minions/emerald.yml", false);
        }
        if (checkFile("minions/enderman.yml")) {
            saveResource("minions/enderman.yml", false);
        }
        if (checkFile("minions/gold.yml")) {
            saveResource("minions/gold.yml", false);
        }
        if (checkFile("minions/lapis.yml")) {
            saveResource("minions/lapis.yml", false);
        }
        if (checkFile("minions/redstone.yml")) {
            saveResource("minions/redstone.yml", false);
        }
        if (checkFile("minions/sheep.yml")) {
            saveResource("minions/sheep.yml", false);
        }
        if (checkFile("minions/zombie.yml")) {
            saveResource("minions/zombie.yml", false);
        }
    }

    private boolean checkFile(String path) {
        return !new File(getDataFolder(), path).exists();
    }

    public void reload() {
        reloadConfig();
        foods.reload();
        lang.reload();
        upgrades.reload();
        tiers.reload();
        shop.reload();
        cm.reload();
        mm.loadMinions();
        cfm.reload();
        adm.delete();
        adm.reload();
        hm.reload();
        tm.loadTiers();
        um.reload();
        am.reload();
        skm.loadSkins();
        shm.loadShop();
    }

    public void sendLogMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage("§a[§cUltraMinions§a] §e" + msg);
    }

    public void sendDebugMessage(String msg) {
        if (getCfm().isDebugMode()) {
            Bukkit.getConsoleSender().sendMessage("§a[§bUltraMinions Debug§a] §e" + msg);
        }
    }

    public Settings getTemp() {
        return temp;
    }

    public Database getDb() {
        return db;
    }

    public ShopManager getShm() {
        return shm;
    }

    public Settings getTiers() {
        return tiers;
    }

    public TiersManager getTm() {
        return tm;
    }

    public Settings getFoods() {
        return foods;
    }

    public FoodManager getFm() {
        return fm;
    }

    public HologramManager getHm() {
        return hm;
    }

    public MinionMenu getMem() {
        return mem;
    }

    public SkinManager getSkm() {
        return skm;
    }

    public AnimationManager getAm() {
        return am;
    }

    public CraftManager getCm() {
        return cm;
    }

    public AddonManager getAdm() {
        return adm;
    }

    public Settings getUpgrades() {
        return upgrades;
    }

    public Settings getLang() {
        return lang;
    }

    public Settings getShop() {
        return shop;
    }

    public ConfigManager getCfm() {
        return cfm;
    }

    public UpgradeManager getUm() {
        return um;
    }

    public SetupMenu getSem() {
        return sem;
    }

    public MinionManager getMm() {
        return mm;
    }

    public SetupManager getSm() {
        return sm;
    }

    public Settings getSkins() {
        return skins;
    }

}