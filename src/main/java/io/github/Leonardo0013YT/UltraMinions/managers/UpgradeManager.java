package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSell;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSmelt;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeCompressor;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class UpgradeManager {

    private Main plugin;
    private HashMap<String, UpgradeAutoSell> autoSell = new HashMap<>();
    private HashMap<String, UpgradeAutoSmelt> autoSmelt = new HashMap<>();
    private HashMap<String, UpgradeCompressor> compressor = new HashMap<>();
    private HashMap<String, UpgradeFuel> fuel = new HashMap<>();

    public UpgradeManager(Main plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        autoSmelt.clear();
        autoSell.clear();
        compressor.clear();
        fuel.clear();
        if (plugin.getUpgrades().isSet("upgrades")) {
            if (plugin.getUpgrades().isSet("upgrades.autoSell")) {
                ConfigurationSection conf = plugin.getUpgrades().getConfig().getConfigurationSection("upgrades.autoSell");
                for (String c : conf.getKeys(false)) {
                    autoSell.put(c, new UpgradeAutoSell(plugin, "upgrades.autoSell." + c));
                    Bukkit.getConsoleSender().sendMessage("§b[UltraMinions] §5Upgrade AutoSell §a" + c + "§5 has been loaded.");
                }
            }
            if (plugin.getUpgrades().isSet("upgrades.autoSmelt")) {
                ConfigurationSection conf = plugin.getUpgrades().getConfig().getConfigurationSection("upgrades.autoSmelt");
                for (String c : conf.getKeys(false)) {
                    autoSmelt.put(c, new UpgradeAutoSmelt(plugin, "upgrades.autoSmelt." + c));
                    Bukkit.getConsoleSender().sendMessage("§b[UltraMinions] §5Upgrade AutoSmelt §a" + c + "§5 has been loaded.");
                }
            }
            if (plugin.getUpgrades().isSet("upgrades.compressor")) {
                ConfigurationSection conf = plugin.getUpgrades().getConfig().getConfigurationSection("upgrades.compressor");
                for (String c : conf.getKeys(false)) {
                    compressor.put(c, new UpgradeCompressor(plugin, "upgrades.compressor." + c));
                    Bukkit.getConsoleSender().sendMessage("§b[UltraMinions] §5Upgrade Compressor §a" + c + "§5 has been loaded.");
                }
            }
            if (plugin.getUpgrades().isSet("upgrades.fuel")) {
                ConfigurationSection conf = plugin.getUpgrades().getConfig().getConfigurationSection("upgrades.fuel");
                for (String c : conf.getKeys(false)) {
                    fuel.put(c, new UpgradeFuel(plugin, "upgrades.fuel." + c));
                    Bukkit.getConsoleSender().sendMessage("§b[UltraMinions] §5Upgrade Fuel §a" + c + "§5 has been loaded.");
                }
            }
        }
    }

    public UpgradeAutoSell getAutoSell(ItemStack item) {
        if (item == null) {
            return null;
        }
        for (UpgradeAutoSell uf : autoSell.values()) {
            if (uf.getResult() == null) continue;
            if (Utils.isSimilar(uf.getResult(), item)) {
                return uf;
            }
        }
        return null;
    }

    public UpgradeAutoSmelt getAutoSmelt(ItemStack item) {
        if (item == null) {
            return null;
        }
        for (UpgradeAutoSmelt uf : autoSmelt.values()) {
            if (uf.getResult() == null) continue;
            if (Utils.isSimilar(uf.getResult(), item)) {
                return uf;
            }
        }
        return null;
    }

    public UpgradeCompressor getCompressor(ItemStack item) {
        if (item == null) {
            return null;
        }
        for (UpgradeCompressor uf : compressor.values()) {
            if (uf.getResult() == null) continue;
            if (Utils.isSimilar(uf.getResult(), item)) {
                return uf;
            }
        }
        return null;
    }

    public UpgradeFuel getFuel(boolean minion, ItemStack item) {
        if (item == null) {
            return null;
        }
        for (UpgradeFuel uf : fuel.values()) {
            ItemStack r = uf.getResult(minion);
            if (r == null) continue;
            ItemStack i = item.clone();
            i.setAmount(r.getAmount());
            if (Utils.isSimilar(r, i)) {
                return uf;
            }
        }
        return null;
    }

    public UpgradeAutoSell getAutoSell(String key) {
        return autoSell.get(key);
    }

    public UpgradeAutoSmelt getAutoSmelt(String key) {
        return autoSmelt.get(key);
    }

    public UpgradeCompressor getCompressor(String key) {
        return compressor.get(key);
    }

    public UpgradeFuel getFuel(String key) {
        return fuel.get(key);
    }

    public HashMap<String, UpgradeFuel> getFuel() {
        return fuel;
    }

    public HashMap<String, UpgradeAutoSell> getAutoSell() {
        return autoSell;
    }

    public HashMap<String, UpgradeCompressor> getCompressor() {
        return compressor;
    }

    public HashMap<String, UpgradeAutoSmelt> getAutoSmelt() {
        return autoSmelt;
    }

}