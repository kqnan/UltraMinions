package io.github.Leonardo0013YT.UltraMinions.shop;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.utils.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopItem {

    private String key, name;
    private int price, slot, page;
    private List<String> lore;
    private Main plugin;
    private Minion minion;

    public ShopItem(Main plugin, String path) {
        this.plugin = plugin;
        this.key = plugin.getShop().get(path + ".key");
        this.name = plugin.getShop().get(path + ".name");
        this.price = plugin.getShop().getInt(path + ".price");
        this.slot = plugin.getShop().getInt(path + ".slot");
        this.page = plugin.getShop().getInt(path + ".page");
        this.lore = plugin.getShop().getList(path + ".lore");
        this.minion = plugin.getMm().getMinion(key);
    }

    public int getPage() {
        return page;
    }

    public int getSlot() {
        return slot;
    }

    public int getPrice() {
        return price;
    }

    public String getKey() {
        return key;
    }

    public Minion getMinion() {
        return minion;
    }

    public ItemStack toIcon(Player p) {
        if (minion == null) {
            return new ItemStack(Material.AIR);
        }
        MinionLevel ml = minion.getMinionLevelByLevel(1);
        List<String> lore = new ArrayList<>();
        for (String l : this.lore) {
            lore.add(l.replaceAll("&", "§").replaceAll("<status>", (plugin.getAdm().getCoins(p) >= price) ? plugin.getShop().get("hasMoney") : plugin.getShop().get("noMoney")).replaceAll("<title>", ml.getLevelTitle()).replaceAll("<price>", "" + getPrice()));
        }
        ItemStack head = NBTEditor.getHead(ml.getUrl());
        ItemMeta headM = head.getItemMeta();
        headM.setDisplayName(name.replaceAll("<title>", ml.getLevelTitle()));
        headM.setLore(lore);
        head.setItemMeta(headM);
        head = NBTEditor.set(head, key, "UltraMinions", "MINION", "SHOP", "KEY");
        return head;
    }

}