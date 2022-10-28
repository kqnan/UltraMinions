package io.github.Leonardo0013YT.UltraMinions.upgrades;

import io.github.Leonardo0013YT.UltraMinions.Main;
import org.bukkit.inventory.ItemStack;

public class UpgradeAutoSmelt {

    private String name;
    private ItemStack result;
    private double percent;
    private boolean isCraft;

    public UpgradeAutoSmelt(Main plugin, String path) {
        this.name = plugin.getUpgrades().get(path + ".name");
        this.percent = plugin.getUpgrades().getConfig().getDouble(path + ".percent");
        this.isCraft = plugin.getUpgrades().getBoolean(path + ".isCraft");
        this.result = plugin.getUpgrades().getConfig().getItemStack(path + ".result");
        if (isCraft) {
            plugin.getCm().loadCustomCraft(path, name);
        }
    }

    public double getPercent() {
        return percent;
    }

    public boolean isCraft() {
        return isCraft;
    }

    public String getName() {
        return name;
    }

    public ItemStack getResult() {
        return result;
    }

}