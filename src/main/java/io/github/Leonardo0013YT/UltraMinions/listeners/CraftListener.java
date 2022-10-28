package io.github.Leonardo0013YT.UltraMinions.listeners;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionCraftEvent;
import io.github.Leonardo0013YT.UltraMinions.craft.Craft;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CraftListener implements Listener {

    private HashMap<Player, HashMap<Integer, Integer>> players = new HashMap<>();
    private Main plugin;

    public CraftListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getType().equals(InventoryType.WORKBENCH)) {
            Player p = (Player) e.getWhoClicked();
            if (!(e.getClickedInventory() instanceof CraftingInventory)) return;
            CraftingInventory crafting = (CraftingInventory) e.getInventory();
            if (e.getSlot() == 0 && crafting.getItem(0) != null && players.containsKey(p)) {
                ItemStack result = crafting.getItem(0);
                ItemStack cursor = p.getItemOnCursor();
                if (cursor != null && !cursor.getType().equals(Material.AIR)) {
                    if (checkEquals(cursor.clone(), result.clone())) {
                        if (cursor.getAmount() + result.getAmount() <= 64) {
                            ItemStack now = result.clone();
                            now.setAmount(cursor.getAmount() + result.getAmount());
                            p.setItemOnCursor(now);
                        } else {
                            e.setCancelled(true);
                            return;
                        }
                    } else {
                        e.setCancelled(true);
                        return;
                    }
                } else {
                    p.setItemOnCursor(result);
                }
                clear(p, crafting, players.get(p));
                players.remove(p);
            }
            players.remove(p);
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack[] input = new ItemStack[9];
                    for (int i = 0; i < 9; i++) {
                        ItemStack item = crafting.getMatrix()[i];
                        if (item != null && item.getType() != Material.AIR) {
                            input[i] = item;
                        } else {
                            input[i] = new ItemStack(Material.AIR);
                        }
                    }
                    for (Craft r : plugin.getCm().getCrafts()) {
                        if (r.checkRequired(input, (a) -> players.put(p, a))) {
                            if (!r.isCraft()) {
                                players.remove(p);
                                break;
                            }
                            if (r.getPermission() != null && !r.getPermission().equals("none") && !p.hasPermission(r.getPermission())) {
                                players.remove(p);
                                break;
                            }
                            if (r.isMinionCraft()) {
                                Minion minion = plugin.getMm().getMinion(r.getKey());
                                MinionLevel minionLevel = minion.getMinionLevelByLevel(r.getLevel());
                                MinionCraftEvent ce = new MinionCraftEvent(p, minion, minionLevel);
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                    Bukkit.getServer().getPluginManager().callEvent(ce);
                                });
                                if (ce.isCancelled()) {
                                    players.remove(p);
                                    break;
                                }
                                if (!minionLevel.isCraftingTable()) {
                                    players.remove(p);
                                    break;
                                }
                                if (plugin.getCfm().isLevelPermission()) {
                                    if (!p.hasPermission("minions.craft." + r.getKey() + "." + r.getLevel())) {
                                        players.remove(p);
                                        break;
                                    }
                                }
                            }
                            crafting.setResult(r.getResult());
                            p.updateInventory();
                            break;
                        }
                    }
                }
            }.runTaskLaterAsynchronously(plugin, 2L);
        }
    }

    @EventHandler
    public void onCraft(InventoryDragEvent e) {
        if (e.getInventory().getType().equals(InventoryType.WORKBENCH)) {
            Player p = (Player) e.getWhoClicked();
            if (!(e.getInventory() instanceof CraftingInventory)) return;
            CraftingInventory crafting = (CraftingInventory) e.getInventory();
            players.remove(p);
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack[] input = new ItemStack[9];
                    for (int i = 0; i < 9; i++) {
                        ItemStack item = crafting.getMatrix()[i];
                        if (item != null && item.getType() != Material.AIR) {
                            input[i] = item;
                        } else {
                            input[i] = new ItemStack(Material.AIR);
                        }
                    }
                    for (Craft r : plugin.getCm().getCrafts()) {
                        if (r.checkRequired(input, (a) -> players.put(p, a))) {
                            if (!r.isCraft()) {
                                players.remove(p);
                                break;
                            }
                            if (r.getPermission() != null && !r.getPermission().equals("none") && !p.hasPermission(r.getPermission())) {
                                players.remove(p);
                                break;
                            }
                            if (r.isMinionCraft()) {
                                Minion minion = plugin.getMm().getMinion(r.getKey());
                                MinionLevel minionLevel = minion.getMinionLevelByLevel(r.getLevel());
                                MinionCraftEvent ce = new MinionCraftEvent(p, minion, minionLevel);
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                    Bukkit.getServer().getPluginManager().callEvent(ce);
                                });
                                if (ce.isCancelled()) {
                                    players.remove(p);
                                    break;
                                }
                                if (!minionLevel.isCraftingTable()) {
                                    players.remove(p);
                                    break;
                                }
                                if (plugin.getCfm().isLevelPermission()) {
                                    if (!p.hasPermission("minions.craft." + r.getKey() + "." + r.getLevel())) {
                                        players.remove(p);
                                        break;
                                    }
                                }
                            }
                            crafting.setResult(r.getResult());
                            p.updateInventory();
                            break;
                        }
                    }
                }
            }.runTaskLaterAsynchronously(plugin, 2L);
        }
    }

    public void clear(Player p, Inventory inv, HashMap<Integer, Integer> values) {
        for (int slot : values.keySet()) {
            int s = slot + 1;
            int amount = values.get(slot);
            if (amount > 0) {
                ItemStack i = inv.getItem(s);
                i.setAmount(amount);
                inv.setItem(s, i);
            } else {
                inv.setItem(s, null);
            }
        }
        p.updateInventory();
    }

    public boolean checkEquals(ItemStack cursor, ItemStack result) {
        cursor.setAmount(1);
        result.setAmount(1);
        return cursor.equals(result);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        players.remove(p);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        players.remove(p);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getType().equals(InventoryType.WORKBENCH)) {
            Player p = (Player) e.getPlayer();
            players.remove(p);
        }
    }

}