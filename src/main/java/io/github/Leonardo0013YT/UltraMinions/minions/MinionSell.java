package io.github.Leonardo0013YT.UltraMinions.minions;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class MinionSell {

    private HashMap<ItemStack, Integer> items;
    private ArrayList<ItemStack> sell;
    private int realized;
    private int chest;

    public MinionSell(HashMap<ItemStack, Integer> items, ArrayList<ItemStack> sell, int realized, int chest) {
        this.items = items;
        this.sell = sell;
        this.realized = realized;
        this.chest = chest;
    }

    public HashMap<ItemStack, Integer> getItems() {
        return items;
    }

    public ArrayList<ItemStack> getSell() {
        return sell;
    }

    public int getChest() {
        return chest;
    }

    public int getRealized() {
        return realized;
    }
}