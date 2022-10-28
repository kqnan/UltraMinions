package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.craft.Craft;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;

public class CraftManager {

    private ArrayList<Craft> crafts = new ArrayList<>();
    private Main plugin;

    public CraftManager(Main plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        crafts.clear();
    }

    public Craft loadMinionCustomCraft(MinionLevel ml, String c, String key, int level, CallBackAPI<Boolean> loaded) {
        YamlConfiguration co = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/minions", key + ".yml"));
        if (!co.isSet(c + ".craft")) {
            loaded.done(false);
            return null;
        }
        String s;
        ItemStack result;
        if (co.get(c + ".craft.result") instanceof ItemStack) {
            result = co.getItemStack(c + ".craft.result");
            s = "";
        } else {
            result = ItemBuilder.createSkull(ml.getLevelTitle(), ml.getMinion().getLore(), co.getString(c + ".craft.result"));
            s = co.getString(c + ".craft.result");
        }
        ItemStack[] items = {getItemStack(co, c + ".craft.items.0"), getItemStack(co, c + ".craft.items.1"), getItemStack(co, c + ".craft.items.2"),
                getItemStack(co, c + ".craft.items.3"), getItemStack(co, c + ".craft.items.4"), getItemStack(co, c + ".craft.items.5"),
                getItemStack(co, c + ".craft.items.6"), getItemStack(co, c + ".craft.items.7"), getItemStack(co, c + ".craft.items.8")};
        Craft craft = new Craft(items, result, key, co.getString(c + ".craft.permission"), level, true, co.getBoolean(c + ".isCraft"), s);
        crafts.add(craft);
        Bukkit.getConsoleSender().sendMessage("§b[UltraMinions] §6Craft of Minion §a" + key + "§6 has been loaded.");
        loaded.done(true);
        return craft;
    }

    public ItemStack getItemStack(YamlConfiguration co, String path) {
        if (co.isSet(path)) {
            return co.getItemStack(path);
        }
        return new ItemStack(Material.AIR);
    }

    public void loadCustomCraft(String c, String key) {
        YamlConfiguration co = plugin.getUpgrades().getConfig();
        ItemStack result = co.getItemStack(c + ".result");
        ItemStack[] items = {co.getItemStack(c + ".craft.items.0"), co.getItemStack(c + ".craft.items.1"), co.getItemStack(c + ".craft.items.2"),
                co.getItemStack(c + ".craft.items.3"), co.getItemStack(c + ".craft.items.4"), co.getItemStack(c + ".craft.items.5"),
                co.getItemStack(c + ".craft.items.6"), co.getItemStack(c + ".craft.items.7"), co.getItemStack(c + ".craft.items.8")};
        crafts.add(new Craft(items, result, "", co.getString(c + ".craft.permission"), 0, false, co.getBoolean(c + ".isCraft"), ""));
        Bukkit.getConsoleSender().sendMessage("§b[UltraMinions] §6Custom Craft §a" + key + "§6 has been loaded.");
    }

    public ArrayList<Craft> getCrafts() {
        return crafts;
    }
}