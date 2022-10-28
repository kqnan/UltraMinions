package io.github.Leonardo0013YT.UltraMinions.upgrades;

import io.github.Leonardo0013YT.UltraMinions.Main;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UpgradeFuel {

    private double percent;
    private long duration;
    private String name;
    private ItemStack result;
    private boolean isCraft, unlimited = false;
    private Main plugin;

    public UpgradeFuel(Main plugin, String path) {
        this.plugin = plugin;
        this.name = plugin.getUpgrades().get(path + ".name");
        this.duration = plugin.getUpgrades().getInt(path + ".duration") * 1000;
        if (duration < 0) {
            unlimited = true;
        }
        this.percent = plugin.getUpgrades().getConfig().getDouble(path + ".percent", 5);
        this.isCraft = plugin.getUpgrades().getBoolean(path + ".isCraft");
        this.result = plugin.getUpgrades().getConfig().getItemStack(path + ".result");
        if (isCraft) {
            plugin.getCm().loadCustomCraft(path, name);
        }
    }

    public double getPercent() {
        return percent;
    }

    public long getDuration() {
        if (unlimited) return Long.MAX_VALUE;
        return duration;
    }

    public boolean isCraft() {
        return isCraft;
    }

    public String getName() {
        return name;
    }

    public ItemStack getResult(boolean minion) {
        if (result == null) {
            return null;
        }
        ItemStack now = result.clone();
        if (now.getItemMeta() == null) return now;
        if (now.getItemMeta().getLore() == null) return now;
        ItemMeta nowM = now.getItemMeta();
        List<String> lore = new ArrayList<>();
        for (String s : nowM.getLore()) {
            lore.add(s.replaceAll("<status>", (minion) ? (unlimited) ? plugin.getLang().get("menus.upgrades.fuel.status.unlimited") : plugin.getLang().get("menus.upgrades.fuel.status.minion") : plugin.getLang().get("menus.upgrades.fuel.status.item")).replaceAll("<time>", plugin.getLang().get("menus.upgrades.fuel.time")));
        }
        nowM.setLore(lore);
        now.setItemMeta(nowM);
        return now;
    }

    public ItemStack getResult(boolean minion, String time) {
        if (result == null) {
            return null;
        }
        ItemStack now = result.clone();
        if (now.getItemMeta() == null) return now;
        if (now.getItemMeta().getLore() == null) return now;
        ItemMeta nowM = now.getItemMeta();
        List<String> lore = new ArrayList<>();
        for (String s : nowM.getLore()) {
            lore.add(s.replaceAll("<status>", (minion) ? (unlimited) ? plugin.getLang().get("menus.upgrades.fuel.status.unlimited") : plugin.getLang().get("menus.upgrades.fuel.status.minion") : plugin.getLang().get("menus.upgrades.fuel.status.item")).replaceAll("<time>", time));
        }
        nowM.setLore(lore);
        now.setItemMeta(nowM);
        return now;
    }

}