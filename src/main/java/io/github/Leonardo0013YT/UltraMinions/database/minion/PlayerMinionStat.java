package io.github.Leonardo0013YT.UltraMinions.database.minion;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;

public class PlayerMinionStat {

    private long fuel;
    private int level;
    private int generated;
    private int work;
    private int sleep;
    private int food;
    private int health, amountFuel, totalFuel;
    private PlayerMinion pm;

    public PlayerMinionStat(PlayerMinion pm, int level, int generated, int work, int sleep, int food, int health, long fuel) {
        this.pm = pm;
        this.level = level;
        this.generated = generated;
        this.work = work;
        this.sleep = sleep;
        this.food = food;
        this.health = health;
        this.fuel = fuel;
    }

    public int getAmountFuel() {
        return amountFuel;
    }

    public void setAmountFuel(int amountFuel) {
        this.amountFuel = amountFuel;
        this.totalFuel = amountFuel;
    }

    public int getTotalFuel() {
        return totalFuel;
    }

    public void setTotalFuel(int totalFuel) {
        this.totalFuel = totalFuel;
    }

    public int getAmountFuel(UpgradeFuel uf) {
        if (amountFuel > 0) {
            long passed = System.currentTimeMillis() - fuel;
            int cicles = (int) (passed / uf.getDuration());
            amountFuel = totalFuel - cicles;
        }
        return amountFuel;
    }

    public long getFuel() {
        return fuel;
    }

    public void setFuel(long fuel) {
        this.fuel = fuel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGenerated() {
        return generated;
    }

    public void setGenerated(int generated) {
        this.generated = generated;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void addFood(int food) {
        this.food = Math.min(this.food + food, pm.getMinionLevel().getFood());
    }

    public void removeFood(int food) {
        this.food = Math.max(this.food - food, 0);
        if (this.food == 0) {
            if (Main.get().getCfm().isHealth()) {
                removeHealth(1);
            }
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void addHealth(int health) {
        this.health = Math.min(this.health + health, pm.getMinionLevel().getHealth());
    }

    public void removeHealth(int health) {
        this.health = Math.max(this.health - health, 0);
    }

}