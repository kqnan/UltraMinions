package io.github.Leonardo0013YT.UltraMinions.database.minion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerMinionChest {

    private Location loc;
    private Inventory inv;
    private Chest chest;

    public PlayerMinionChest(Location loc) {
        this.loc = loc;
        Block b = loc.getBlock();
        if (b.getType().equals(Material.CHEST)) {
            chest = (Chest) b.getState();
            InventoryHolder holder = chest.getInventory().getHolder();
            if (holder instanceof DoubleChest) {
                DoubleChest doubleChest = ((DoubleChest) holder);
                inv = doubleChest.getInventory();
            } else {
                inv = chest.getInventory();
            }
        }
    }

    public Location getLoc() {
        return loc;
    }

    public boolean isChest() {
        return chest != null;
    }

    public boolean addItem(ItemStack i) {
        if (inv.firstEmpty() != -1) {
            inv.addItem(i);
            return true;
        }
        return false;
    }

    public int addItem(List<ItemStack> i, int max) {
        int amount = 0;
        for (ItemStack it : i) {
            if (inv.firstEmpty() != -1) {
                amount += it.getAmount();
                inv.addItem(it);
            }
        }
        return max - amount;
    }

    public boolean isFull() {
        if (inv == null) {
            return false;
        }
        return inv.firstEmpty() == -1;
    }

    public Inventory getInventory() {
        return inv;
    }

}