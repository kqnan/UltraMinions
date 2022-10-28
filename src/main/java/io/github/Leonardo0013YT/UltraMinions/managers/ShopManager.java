package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.shop.ShopItem;

import java.util.HashMap;

public class ShopManager {

    private Main plugin;
    private int lastPage = 0;
    private HashMap<String, ShopItem> shop = new HashMap<>();

    public ShopManager(Main plugin) {
        this.plugin = plugin;
    }

    public void loadShop() {
        shop.clear();
        if (!plugin.getShop().isSet("shop")) return;
        for (String s : plugin.getShop().getConfig().getConfigurationSection("shop").getKeys(false)) {
            ShopItem si = new ShopItem(plugin, "shop." + s);
            shop.put(si.getKey(), si);
            if (lastPage < si.getPage()) {
                lastPage = si.getPage();
            }
        }
    }

    public HashMap<String, ShopItem> getShop() {
        return shop;
    }

    public int getLastPage() {
        return lastPage;
    }
}