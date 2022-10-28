package io.github.Leonardo0013YT.UltraMinions.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SellAddon {


    float getPrice(Player p, ItemStack item);
}