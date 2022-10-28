package io.github.Leonardo0013YT.UltraMinions.upgrades;

import io.github.Leonardo0013YT.UltraMinions.Main;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class UpgradeCompressor {

    private String name;
    private ItemStack result;
    private int amount;
    private boolean isCraft;
    private HashMap<String, Integer> specialAmounts = new HashMap<>();

    public UpgradeCompressor(Main plugin, String path) {
        this.name = plugin.getUpgrades().get(path + ".name");
        this.isCraft = plugin.getUpgrades().getBoolean(path + ".isCraft");
        this.amount = plugin.getUpgrades().getInt(path + ".amount");
        if (plugin.getUpgrades().isSet(path + ".special_amounts")) {
            for (String special : plugin.getUpgrades().getConfig().getConfigurationSection(path + ".special_amounts").getKeys(false)) {
                specialAmounts.put(special, plugin.getUpgrades().getInt(path + ".special_amounts." + special));
            }
        }
        this.result = plugin.getUpgrades().getConfig().getItemStack(path + ".result");
        if (isCraft) {
            plugin.getCm().loadCustomCraft(path, name);
        }
    }

    public boolean isCraft() {
        return isCraft;
    }

    public int getAmount(String key) {
        if (specialAmounts.containsKey(key)) {
            return specialAmounts.get(key);
        }
        return amount;
    }

    public String getName() {
        return name;
    }

    public ItemStack getResult() {
        return result;
    }

}