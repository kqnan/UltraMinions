package io.github.Leonardo0013YT.UltraMinions.minions.skins;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public class MinionSkin {

    private String customname, name;
    private ItemStack head;
    private int red, green, blue;

    public MinionSkin(Main plugin, String name) {
        this.name = name;
        this.customname = plugin.getSkins().get("skins." + name + ".name");
        this.head = ItemBuilder.createSkull(plugin.getLang().get("items.minionSkin.nameItem") + " " + customname, plugin.getLang().get("items.minionSkin.loreItem"), plugin.getSkins().get("skins." + name + ".uri"));
        this.red = plugin.getSkins().getInt("skins." + name + ".armor.red");
        this.green = plugin.getSkins().getInt("skins." + name + ".armor.green");
        this.blue = plugin.getSkins().getInt("skins." + name + ".armor.blue");
    }

    public String getCustomname() {
        return customname;
    }

    public String getName() {
        return name;
    }

    public ItemStack getHead() {
        return head;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}