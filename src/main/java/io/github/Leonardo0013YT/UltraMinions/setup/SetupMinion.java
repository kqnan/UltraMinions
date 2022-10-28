package io.github.Leonardo0013YT.UltraMinions.setup;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionType;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SetupMinion {

    private HashMap<Integer, SetupMinionLevel> levels = new HashMap<>();
    private MinionType type;
    private ItemStack place;
    private EntityType spawn;
    private List<String> lore;
    private List<ItemStack> giveInInv = new ArrayList<>(), compressor = new ArrayList<>(), autoSmelt = new ArrayList<>();
    private String key, title;
    private int red, blue, green;
    private double priceNormalSell, priceSmeltedSell, priceCompressedSell;
    private boolean compressorEnabled, smeltEnabled, autoSellEnabled, fuelEnabled;
    private SetupMinionLevel actual;
    private Main plugin;

    public SetupMinion(Main plugin) {
        this.plugin = plugin;
        this.type = MinionType.MINER;
        this.spawn = EntityType.ZOMBIE;
        this.place = new ItemStack(Material.COBBLESTONE);
        this.key = place.getType().name().toLowerCase();
        this.giveInInv.add(new ItemStack(Material.COBBLESTONE));
        this.compressor.add(new ItemStack(Material.COBBLESTONE));
        this.autoSmelt.add(new ItemStack(Material.STONE));
        this.red = 92;
        this.blue = 92;
        this.green = 92;
        this.priceNormalSell = 2;
        this.priceSmeltedSell = 4;
        this.priceCompressedSell = 2;
        this.compressorEnabled = false;
        this.autoSellEnabled = true;
        this.smeltEnabled = true;
        this.fuelEnabled = true;
        this.lore = new ArrayList<>(Arrays.asList("§7This is a default lore", "§7of Minion.", "§7", "§eRight click to place"));
        this.title = "§8Cobblestone Minion";
    }

    public SetupMinion(Main plugin, Minion m) {
        this.plugin = plugin;
        this.type = m.getType();
        this.spawn = m.getSpawn();
        this.place = m.getPlace();
        this.key = m.getKey();
        this.giveInInv = m.getGiveInInv().getItems();
        this.compressor = m.getCompressor().getItems();
        this.autoSmelt = m.getSmelt().getItems();
        this.red = m.getRed();
        this.blue = m.getBlue();
        this.green = m.getGreen();
        this.priceNormalSell = m.getPriceNormalSell();
        this.priceSmeltedSell = m.getPriceSmeltedSell();
        this.priceCompressedSell = m.getPriceCompressedSell();
        this.compressorEnabled = m.isCompressorEnabled();
        this.autoSellEnabled = m.isAutoSellEnabled();
        this.smeltEnabled = m.isSmeltEnabled();
        this.fuelEnabled = m.isFuelEnabled();
        this.lore = m.getLore();
        this.title = m.getTitle();
        for (MinionLevel ml : m.getLevels().values()) {
            levels.put(ml.getLevel() - 1, new SetupMinionLevel(ml));
        }
    }

    public void saveLevel(Player p) {
        if (actual == null) {
            return;
        }
        levels.put(levels.size(), actual);
        actual = null;
        p.sendMessage(plugin.getLang().get("setup.saveLevel"));
    }

    public void save(Player p) {
        File dataFolder = new File(plugin.getDataFolder(), "minions");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File file = new File(dataFolder, key + ".yml");
        YamlConfiguration minion = YamlConfiguration.loadConfiguration(file);
        minion.set("minions." + key, null);
        minion.set("minions." + key + ".key", key);
        minion.set("minions." + key + ".title", title.replaceAll("§", "&"));
        minion.set("minions." + key + ".place", place);
        minion.set("minions." + key + ".lore", lore);
        minion.set("minions." + key + ".type", type.name());
        minion.set("minions." + key + ".spawn", spawn.name());
        int a = 0;
        for (ItemStack i : compressor) {
            minion.set("minions." + key + ".compressor." + a + ".item", i);
            a++;
        }
        int a1 = 0;
        for (ItemStack i : autoSmelt) {
            minion.set("minions." + key + ".smelt." + a1 + ".item", i);
            a1++;
        }
        int a2 = 0;
        for (ItemStack i : giveInInv) {
            minion.set("minions." + key + ".giveInInv." + a2 + ".item", i);
            a2++;
        }
        minion.set("minions." + key + ".priceNormalSell", priceNormalSell);
        minion.set("minions." + key + ".priceSmeltedSell", priceSmeltedSell);
        minion.set("minions." + key + ".priceCompressedSell", priceCompressedSell);
        minion.set("minions." + key + ".compressorEnabled", compressorEnabled);
        minion.set("minions." + key + ".smeltEnabled", smeltEnabled);
        minion.set("minions." + key + ".autoSellEnabled", autoSellEnabled);
        minion.set("minions." + key + ".fuelEnabled", fuelEnabled);
        minion.set("minions." + key + ".red", red);
        minion.set("minions." + key + ".blue", blue);
        minion.set("minions." + key + ".green", green);
        for (SetupMinionLevel sml : levels.values()) {
            int level = sml.getLevel();
            String path = "minions." + key + ".levels." + (level - 1);
            sml.setLevelTitle(getTitle() + " " + Utils.IntegerToRomanNumeral(level));
            minion.set(path + ".levelTitle", sml.getLevelTitle());
            minion.set(path + ".isCraft", sml.isCraft());
            minion.set(path + ".isLevel", sml.isLevel());
            minion.set(path + ".isCoins", sml.isCoins());
            minion.set(path + ".level", level);
            minion.set(path + ".url", sml.getUrl());
            minion.set(path + ".delay", sml.getDelay());
            minion.set(path + ".max", sml.getMax());
            minion.set(path + ".upgradeCoins", sml.getUpgradeCoins());
            minion.set(path + ".upgradeLevels", sml.getUpgradeLevel());
            minion.set(path + ".food", sml.getFood());
            minion.set(path + ".health", sml.getFood());
            minion.set(path + ".workTime", sml.getWorkTime());
            minion.set(path + ".sleep", sml.getSleep());
            minion.set(path + ".craft", null);
            if (sml.getCraft() != null) {
                if (sml.getCraft().getResult() == null) {
                    minion.set(path + ".craft.result", sml.getUrl());
                } else {
                    minion.set(path + ".craft.result", sml.getCraft().getResult());
                }
                minion.set(path + ".craft.permission", sml.getCraft().getPermission());
                for (int i = 0; i < sml.getCraft().getMatrix().length; i++) {
                    minion.set(path + ".craft.items." + i, sml.getCraft().getMatrix()[i]);
                }
            }
        }
        try {
            minion.save(file);
        } catch (IOException ignored) {
        }
        p.sendMessage(plugin.getLang().get("setup.save"));
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemStack> getAutoSmelt() {
        return autoSmelt;
    }

    public List<ItemStack> getCompressor() {
        return compressor;
    }

    public List<ItemStack> getGiveInInv() {
        return giveInInv;
    }

    public MinionType getType() {
        return type;
    }

    public void setType(MinionType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ItemStack getPlace() {
        return place;
    }

    public void setPlace(ItemStack place) {
        this.place = place;
    }

    public EntityType getSpawn() {
        return spawn;
    }

    public void setSpawn(EntityType spawn) {
        this.spawn = spawn;
    }

    public double getPriceNormalSell() {
        return priceNormalSell;
    }

    public void setPriceNormalSell(double priceNormalSell) {
        this.priceNormalSell = priceNormalSell;
    }

    public double getPriceSmeltedSell() {
        return priceSmeltedSell;
    }

    public void setPriceSmeltedSell(double priceSmeltedSell) {
        this.priceSmeltedSell = priceSmeltedSell;
    }

    public double getPriceCompressedSell() {
        return priceCompressedSell;
    }

    public void setPriceCompressedSell(double priceCompressedSell) {
        this.priceCompressedSell = priceCompressedSell;
    }

    public boolean isCompressorEnabled() {
        return compressorEnabled;
    }

    public void setCompressorEnabled(boolean compressorEnabled) {
        this.compressorEnabled = compressorEnabled;
    }

    public boolean isSmeltEnabled() {
        return smeltEnabled;
    }

    public void setSmeltEnabled(boolean smeltEnabled) {
        this.smeltEnabled = smeltEnabled;
    }

    public boolean isAutoSellEnabled() {
        return autoSellEnabled;
    }

    public void setAutoSellEnabled(boolean autoSellEnabled) {
        this.autoSellEnabled = autoSellEnabled;
    }

    public boolean isFuelEnabled() {
        return fuelEnabled;
    }

    public void setFuelEnabled(boolean fuelEnabled) {
        this.fuelEnabled = fuelEnabled;
    }

    public SetupMinionLevel getActual() {
        return actual;
    }

    public void setActual(SetupMinionLevel actual) {
        this.actual = actual;
    }

    public HashMap<Integer, SetupMinionLevel> getLevels() {
        return levels;
    }

    public List<String> getLore() {
        return lore;
    }
}