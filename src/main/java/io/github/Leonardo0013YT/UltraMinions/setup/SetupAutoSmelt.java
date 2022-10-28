package io.github.Leonardo0013YT.UltraMinions.setup;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetupAutoSmelt {

    private Player p;
    private String name;
    private SetupCraft craft;
    private ItemStack result;
    private boolean isCraft;
    private double percent;

    public SetupAutoSmelt(Player p, String name) {
        this.p = p;
        this.name = name;
        this.percent = 100;
        this.craft = new SetupCraft();
    }

    public void save(CallBackAPI<Boolean> backAPI) {
        Main plugin = Main.get();
        String pt = "upgrades.autoSmelt." + name;
        plugin.getUpgrades().set(pt + ".name", name);
        plugin.getUpgrades().set(pt + ".percent", percent);
        plugin.getUpgrades().set(pt + ".isCraft", isCraft);
        plugin.getUpgrades().set(pt + ".result", result);
        backAPI.done(result != null);
        if (isCraft) {
            plugin.getUpgrades().set(pt + ".craft.permission", craft.getPermission());
            for (int i = 0; i < 9; i++) {
                plugin.getUpgrades().set(pt + ".craft.items." + i, craft.getMatrix()[i]);
            }
        }
        plugin.getUpgrades().save();
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public boolean isCraft() {
        return isCraft;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
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