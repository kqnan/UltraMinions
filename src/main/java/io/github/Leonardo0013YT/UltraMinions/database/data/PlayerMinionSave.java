package io.github.Leonardo0013YT.UltraMinions.database.data;

import io.github.Leonardo0013YT.UltraMinions.interfaces.MinionSave;

public class PlayerMinionSave implements MinionSave {

    private String type, key, skin = "none", loc, chest, fuel = "none", autoSell = "none", compressor = "none", autoSmelt = "none";
    private boolean isChest;
    private long fuelTime;
    private int actions, level, generated, work, sleep, food, health, totalFuel, fuelAmount;

    @Override
    public int getFuelAmount() {
        return fuelAmount;
    }

    @Override
    public void setFuelAmount(int fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    @Override
    public int getTotalFuel() {
        return totalFuel;
    }

    @Override
    public void setTotalFuel(int totalFuel) {
        this.totalFuel = totalFuel;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getSkin() {
        return skin;
    }

    @Override
    public void setSkin(String skin) {
        this.skin = skin;
    }

    @Override
    public String getLoc() {
        return loc;
    }

    @Override
    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public String getChest() {
        return chest;
    }

    @Override
    public String getFuel() {
        return fuel;
    }

    @Override
    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    @Override
    public String getAutoSell() {
        return autoSell;
    }

    @Override
    public void setAutoSell(String autoSell) {
        this.autoSell = autoSell;
    }

    @Override
    public String getCompressor() {
        return compressor;
    }

    @Override
    public void setCompressor(String compressor) {
        this.compressor = compressor;
    }

    @Override
    public String getAutoSmelt() {
        return autoSmelt;
    }

    @Override
    public void setAutoSmelt(String autoSmelt) {
        this.autoSmelt = autoSmelt;
    }

    @Override
    public boolean isChest() {
        return isChest;
    }

    @Override
    public void setChest(String chest) {
        this.chest = chest;
    }

    @Override
    public void setChest(boolean chest) {
        isChest = chest;
    }

    @Override
    public long getFuelTime() {
        return fuelTime;
    }

    @Override
    public void setFuelTime(long fuelTime) {
        this.fuelTime = fuelTime;
    }

    @Override
    public int getActions() {
        return actions;
    }

    @Override
    public void setActions(int actions) {
        this.actions = actions;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getGenerated() {
        return generated;
    }

    @Override
    public void setGenerated(int generated) {
        this.generated = generated;
    }

    @Override
    public int getWork() {
        return work;
    }

    @Override
    public void setWork(int work) {
        this.work = work;
    }

    @Override
    public int getSleep() {
        return sleep;
    }

    @Override
    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    @Override
    public int getFood() {
        return food;
    }

    @Override
    public void setFood(int food) {
        this.food = food;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }
}