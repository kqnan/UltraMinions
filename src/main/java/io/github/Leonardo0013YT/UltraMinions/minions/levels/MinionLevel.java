package io.github.Leonardo0013YT.UltraMinions.minions.levels;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.craft.Craft;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraMinions.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MinionLevel {

    private int level, health, food, workTime, sleep, upgradeCoins, upgradeLevels, max, delay, upgrades = 0;
    private boolean isCraft, isLevel, isCoins, isCraftingTable;
    private String levelTitle, url;
    private Main plugin;
    private Minion minion;
    private Craft craft;

    public MinionLevel(Main plugin, Minion minion, YamlConfiguration config, String path, File f) {
        this.plugin = plugin;
        this.minion = minion;
        this.level = config.getInt(path + ".level");
        if (!config.isSet(path + ".url")) {
            config.set(path + ".url", minion.getUrl());
            try {
                config.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.url = config.getString(path + ".url");
        this.levelTitle = config.getString(path + ".levelTitle").replaceAll("&", "§");
        this.health = config.getInt(path + ".health");
        this.food = config.getInt(path + ".food");
        this.workTime = config.getInt(path + ".workTime");
        this.sleep = config.getInt(path + ".sleep");
        this.upgradeCoins = config.getInt(path + ".upgradeCoins");
        this.upgradeLevels = config.getInt(path + ".upgradeLevels");
        this.max = config.getInt(path + ".max");
        this.delay = config.getInt(path + ".delay");
        this.isCraft = config.getBoolean(path + ".isCraft");
        this.isLevel = config.getBoolean(path + ".isLevel");
        this.isCoins = config.getBoolean(path + ".isCoins");
        Utils.check(path + ".isCraftingTable", true, config, f);
        this.isCraftingTable = config.getBoolean(path + ".isCraftingTable", true);
        if (isCraft) {
            upgrades++;
        }
        if (isLevel) {
            upgrades++;
        }
        if (isCoins) {
            upgrades++;
        }
        if (isCraft) {
            craft = plugin.getCm().loadMinionCustomCraft(this, path, minion.getKey(), level, b -> {
                isCraft = b;
            });
        }
    }

    public String getUrl() {
        return url;
    }

    public ItemStack getHead() {
        return ItemBuilder.createSkull("", "", url);
    }

    // Level, Key
    public ItemStack getMinionHead(String url) {
        ItemStack head = NBTEditor.getHead(url);
        ItemMeta headM = head.getItemMeta();
        headM.setDisplayName(levelTitle);
        List<String> lore = new ArrayList<>();
        for (String l : minion.getLore()) {
            lore.add(l.replaceAll("<time>", "" + delay).replaceAll("<max>", "" + max).replaceAll("<generated>", "" + 0));
        }
        headM.setLore(lore);
        head.setItemMeta(headM);
        head = NBTEditor.set(head, level, "LEVEL");
        head = NBTEditor.set(head, minion.getKey(), "KEY");
        head = NBTEditor.set(head, food, "FOOD");
        head = NBTEditor.set(head, health, "HEALTH");
        head = NBTEditor.set(head, workTime, "WORKTIME");
        head = NBTEditor.set(head, sleep, "SLEEP");
        head = NBTEditor.set(head, (long) 0, "FUELTIME");
        head = NBTEditor.set(head, "none", "SKIN");
        return head;
    }

    // Level, Key
    public ItemStack getMinionHead() {
        ItemStack head = NBTEditor.getHead(url);
        ItemMeta headM = head.getItemMeta();
        headM.setDisplayName(levelTitle);
        List<String> lore = new ArrayList<>();
        for (String l : minion.getLore()) {
            lore.add(l.replaceAll("<time>", "" + delay).replaceAll("<max>", "" + max).replaceAll("<generated>", "" + 0));
        }
        headM.setLore(lore);
        head.setItemMeta(headM);
        head = NBTEditor.set(head, level, "LEVEL");
        head = NBTEditor.set(head, minion.getKey(), "KEY");
        head = NBTEditor.set(head, food, "FOOD");
        head = NBTEditor.set(head, health, "HEALTH");
        head = NBTEditor.set(head, workTime, "WORKTIME");
        head = NBTEditor.set(head, sleep, "SLEEP");
        head = NBTEditor.set(head, (long) 0, "FUELTIME");
        head = NBTEditor.set(head, "none", "SKIN");
        return head;
    }

    public ItemStack getMinionHead(PlayerMinion pm) {
        ItemStack head = NBTEditor.getHead(url);
        ItemMeta headM = head.getItemMeta();
        headM.setDisplayName(levelTitle);
        List<String> lore = new ArrayList<>();
        for (String l : minion.getLore()) {
            lore.add(l.replaceAll("<time>", "" + pm.getDelay()).replaceAll("<max>", "" + max).replaceAll("<generated>", "" + pm.getStat().getGenerated()));
        }
        headM.setLore(lore);
        head.setItemMeta(headM);
        head = NBTEditor.set(head, pm.getStat().getLevel(), "LEVEL");
        head = NBTEditor.set(head, minion.getKey(), "KEY");
        head = NBTEditor.set(head, pm.getStat().getFood(), "FOOD");
        head = NBTEditor.set(head, pm.getStat().getHealth(), "HEALTH");
        head = NBTEditor.set(head, pm.getStat().getWork(), "WORKTIME");
        head = NBTEditor.set(head, pm.getStat().getSleep(), "SLEEP");
        head = NBTEditor.set(head, pm.getStat().getFuel(), "FUELTIME");
        head = NBTEditor.set(head, pm.getSkin(), "SKIN");
        if (pm.getUpgrade().getAutoSell() != null) {
            head = NBTEditor.set(head, pm.getUpgrade().getAutoSell().getName(), "AUTOSELL");
        }
        if (pm.getUpgrade().getAutoSmelt() != null) {
            head = NBTEditor.set(head, pm.getUpgrade().getAutoSmelt().getName(), "AUTOSMELT");
        }
        if (pm.getUpgrade().getCompressor() != null) {
            head = NBTEditor.set(head, pm.getUpgrade().getCompressor().getName(), "COMPRESSOR");
        }
        if (pm.getUpgrade().getFuel() != null) {
            head = NBTEditor.set(head, pm.getUpgrade().getFuel().getName(), "FUEL");
        }
        return head;
    }

    public Minion getMinion() {
        return minion;
    }

    public Craft getCraft() {
        if (craft == null) return null;
        return craft.clone();
    }

    public int getUpgrades() {
        return upgrades;
    }

    public int getLevel() {
        return level;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public int getHealth() {
        return health;
    }

    public int getFood() {
        return food;
    }

    public int getWorkTime() {
        return workTime;
    }

    public int getSleep() {
        return sleep;
    }

    public int getUpgradeCoins() {
        return upgradeCoins;
    }

    public int getUpgradeLevels() {
        return upgradeLevels;
    }

    public int getMax() {
        return max;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isCraft() {
        if (craft == null || craft.getResult() == null || craft.getMatrix() == null) {
            return false;
        }
        return isCraft;
    }

    public boolean isCraftingTable() {
        return isCraftingTable;
    }

    public boolean isLevel() {
        return isLevel;
    }

    public boolean isCoins() {
        return isCoins;
    }

}