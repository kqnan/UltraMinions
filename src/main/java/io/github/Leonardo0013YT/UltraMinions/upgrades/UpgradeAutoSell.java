package io.github.Leonardo0013YT.UltraMinions.upgrades;

import io.github.Leonardo0013YT.UltraMinions.Main;
import org.bukkit.inventory.ItemStack;

public class UpgradeAutoSell {

    private String name;
    private double sell;
    private ItemStack result;
    private boolean isCraft;

    public UpgradeAutoSell(Main plugin, String path) {
        this.name = plugin.getUpgrades().get(path + ".name");
        this.sell = plugin.getUpgrades().getConfig().getDouble(path + ".sell");
        this.isCraft = plugin.getUpgrades().getBoolean(path + ".isCraft");
        this.result = plugin.getUpgrades().getConfig().getItemStack(path + ".result");
        if (isCraft) {
            plugin.getCm().loadCustomCraft(path, name);
        }
    }

    public boolean isCraft() {
        return isCraft;
    }

    public String getName() {
        return name;
    }

    public double getSell() {
        return sell;
    }

    public ItemStack getResult() {
        return result;
    }

}