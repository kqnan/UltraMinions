package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.minions.skins.MinionSkin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SkinManager {

    private Main plugin;
    private HashMap<String, MinionSkin> skins = new HashMap<>();

    public SkinManager(Main plugin) {
        this.plugin = plugin;
        loadSkins();
    }

    public void loadSkins() {
        skins.clear();
        ConfigurationSection conf = plugin.getSkins().getConfig().getConfigurationSection("skins");
        for (String s : conf.getKeys(false)) {
            skins.put(s.toLowerCase(), new MinionSkin(plugin, s));
        }
    }

    public HashMap<String, MinionSkin> getSkins() {
        return skins;
    }

    public MinionSkin getMinionSkinByName(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return null;
        }
        for (MinionSkin s : skins.values()) {
            if (s.getCustomname().equals(item.getItemMeta().getDisplayName().replaceFirst(plugin.getLang().get("items.minionSkin.nameItem") + " ", ""))) {
                return s;
            }
        }
        return null;
    }

}