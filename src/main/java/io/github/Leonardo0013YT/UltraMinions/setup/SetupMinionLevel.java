package io.github.Leonardo0013YT.UltraMinions.setup;

import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SetupMinionLevel {

    private boolean isLevel, isCoins, isCraft;
    private int delay, max, upgradeLevel, upgradeCoins, health, food, workTime, sleep;
    private ItemStack item;
    private String levelTitle, url;
    private int level;
    private SetupCraft craft;

    public SetupMinionLevel(SetupMinion sm, int level) {
        this.level = level;
        this.isCoins = true;
        this.isLevel = false;
        this.isCraft = false;
        this.delay = 30;
        this.max = 64;
        this.url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMxNzU0ODUxZTM2N2U4YmViYTJhNmQ4ZjdjMmZlZGU4N2FlNzkzYWM1NDZiMGYyOTlkNjczMjE1YjI5MyJ9fX0=";
        this.upgradeCoins = 500;
        this.upgradeLevel = 10;
        this.health = 20;
        this.food = 100;
        this.workTime = 3600;
        this.sleep = 600;
        this.item = new ItemStack(Material.COBBLESTONE);
        this.levelTitle = sm.getTitle() + " " + Utils.IntegerToRomanNumeral(level);
    }

    public SetupMinionLevel(MinionLevel ml) {
        this.level = ml.getLevel();
        this.isCoins = ml.isCoins();
        this.isLevel = ml.isLevel();
        this.isCraft = ml.isCraft();
        this.delay = ml.getDelay();
        this.max = ml.getMax();
        this.upgradeCoins = ml.getUpgradeCoins();
        this.url = ml.getUrl();
        this.upgradeLevel = ml.getUpgradeLevels();
        this.health = ml.getHealth();
        this.food = ml.getFood();
        this.workTime = ml.getWorkTime();
        this.sleep = ml.getSleep();
        this.item = ml.getMinionHead();
        this.levelTitle = ml.getLevelTitle();
        if (isCraft) {
            this.craft = new SetupCraft(ml.getCraft());
        }
    }

    public boolean isLevel() {
        return isLevel;
    }

    public boolean isCoins() {
        return isCoins;
    }

    public void setCoins(boolean coins) {
        isCoins = coins;
    }

    public boolean isCraft() {
        return isCraft;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(int upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public int getUpgradeCoins() {
        return upgradeCoins;
    }

    public void setUpgradeCoins(int upgradeCoins) {
        this.upgradeCoins = upgradeCoins;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public void setLevelTitle(String levelTitle) {
        this.levelTitle = levelTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(boolean level) {
        isLevel = level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public SetupCraft getCraft() {
        return craft;
    }

    public void setCraft(boolean craft) {
        isCraft = craft;
    }

    public void setCraft(SetupCraft craft) {
        this.craft = craft;
    }
}