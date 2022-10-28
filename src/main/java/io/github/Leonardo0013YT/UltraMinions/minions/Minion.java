package io.github.Leonardo0013YT.UltraMinions.minions;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionType;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Minion {

    private final File f;
    public Main plugin;
    private HashMap<Integer, MinionLevel> levels = new HashMap<>();
    private String url, key, title, animation;
    private ItemStack place, handItem;
    private EntityType spawn;
    private int red, blue, green, damage;
    private double priceNormalSell, priceSmeltedSell, priceCompressedSell;
    private boolean compressorEnabled, smeltEnabled, autoSellEnabled, fuelEnabled;
    private MinionItem giveInInv;
    private MinionItem compressor;
    private MinionItem smelt;
    private List<String> lore;
    private MinionType type;

    public Minion(Main plugin, YamlConfiguration minion, String path, File f) {
        this.plugin = plugin;
        this.f = f;
        this.key = minion.getString(path + ".key");
        this.title = minion.getString(path + ".title").replaceAll("&", "§");
        List<String> lore = new ArrayList<>();
        minion.getStringList(path + ".lore").forEach(l -> lore.add(l.replaceAll("&", "§")));
        this.lore = lore;
        this.url = minion.getString(path + ".url");
        this.place = minion.getItemStack(path + ".place");
        this.red = minion.getInt(path + ".red");
        this.blue = minion.getInt(path + ".blue");
        this.green = minion.getInt(path + ".green");
        if (minion.isSet(path + ".spawn")) {
            spawn = EntityType.valueOf(minion.getString(path + ".spawn"));
        }
        if (minion.isSet(path + ".compressor")) {
            ConfigurationSection conf = minion.getConfigurationSection(path + ".compressor");
            ArrayList<ItemStack> items = new ArrayList<>();
            for (String c : conf.getKeys(false)) {
                ItemStack i = minion.getItemStack(path + ".compressor." + c + ".item");
                items.add(i);
                compressor = new MinionItem(items);
            }
        }
        if (minion.isSet(path + ".smelt")) {
            ConfigurationSection conf2 = minion.getConfigurationSection(path + ".smelt");
            ArrayList<ItemStack> items = new ArrayList<>();
            for (String c : conf2.getKeys(false)) {
                ItemStack i = minion.getItemStack(path + ".smelt." + c + ".item");
                items.add(i);
                smelt = new MinionItem(items);
            }
        }
        if (minion.isSet(path + ".giveInInv")) {
            ConfigurationSection conf3 = minion.getConfigurationSection(path + ".giveInInv");
            ArrayList<ItemStack> items = new ArrayList<>();
            for (String c : conf3.getKeys(false)) {
                ItemStack i = minion.getItemStack(path + ".giveInInv." + c + ".item");
                items.add(i);
                giveInInv = new MinionItem(items);
            }
        }
        this.priceNormalSell = minion.getDouble(path + ".priceNormalSell");
        this.priceSmeltedSell = minion.getDouble(path + ".priceSmeltedSell");
        this.priceCompressedSell = minion.getDouble(path + ".priceCompressedSell");
        this.compressorEnabled = minion.getBoolean(path + ".compressorEnabled");
        this.smeltEnabled = minion.getBoolean(path + ".smeltEnabled");
        this.autoSellEnabled = minion.getBoolean(path + ".autoSellEnabled");
        this.fuelEnabled = minion.getBoolean(path + ".fuelEnabled");
        Utils.check(path + ".damage", 5, minion, f);
        this.damage = minion.getInt(path + ".damage", 5);
        Utils.check(path + ".animation", "picar.animc", minion, f);
        this.animation = minion.getString(path + ".animation", "picar.animc");
        this.type = MinionType.valueOf(minion.getString(path + ".type"));
        Utils.check(path + ".handItem", type.getHandItem().name(), minion, f);
        this.handItem = new ItemStack(Material.valueOf(minion.getString(path + ".handItem")));
        ConfigurationSection levels = minion.getConfigurationSection(path + ".levels");
        for (String l : levels.getKeys(false)) {
            int level = Integer.parseInt(l);
            plugin.sendDebugMessage("§dTrying load minion level §a" + level + "§d.");
            this.levels.put(level, new MinionLevel(plugin, this, minion, path + ".levels." + l, f));
            plugin.sendDebugMessage("§dMinion Level §a" + level + "§d has been loaded.");
        }
    }

    public String getUrl() {
        return url;
    }

    public abstract void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action);

    public abstract boolean check(Location spawn);

    public ItemStack getHandItem() {
        return handItem;
    }

    public String getAnimation() {
        return animation;
    }

    public int getDamage() {
        return damage;
    }

    public EntityType getSpawn() {
        return spawn;
    }

    public ItemStack getPlace() {
        if (place == null || place.getType().equals(Material.AIR)) {
            return new ItemStack(Material.STONE);
        }
        return place;
    }

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }

    public List<String> getLore() {
        return lore;
    }

    public MinionType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public MinionLevel getMinionLevelByLevel(int level) {
        return levels.get(level - 1);
    }

    public double getPriceNormalSell() {
        return priceNormalSell;
    }

    public double getPriceSmeltedSell() {
        return priceSmeltedSell;
    }

    public double getPriceCompressedSell() {
        return priceCompressedSell;
    }

    public boolean isCompressorEnabled() {
        return compressorEnabled;
    }

    public boolean isSmeltEnabled() {
        return smeltEnabled;
    }

    public boolean isAutoSellEnabled() {
        return autoSellEnabled;
    }

    public boolean isFuelEnabled() {
        return fuelEnabled;
    }

    public MinionItem getCompressor() {
        if (compressor == null) {
            return new MinionItem(new ArrayList<>());
        }
        return compressor;
    }

    public MinionItem getGiveInInv() {
        if (giveInInv == null) {
            return new MinionItem(new ArrayList<>());
        }
        return giveInInv;
    }

    public MinionItem getSmelt() {
        if (smelt == null) {
            return new MinionItem(new ArrayList<>());
        }
        return smelt;
    }

    public String getTitle() {
        return title;
    }

    public HashMap<Integer, MinionLevel> getLevels() {
        return levels;
    }
}