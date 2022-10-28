package io.github.Leonardo0013YT.UltraMinions.listeners;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionChest;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionType;
import io.github.Leonardo0013YT.UltraMinions.food.Food;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.setup.*;
import io.github.Leonardo0013YT.UltraMinions.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SetupListener implements Listener {

    private Main plugin;

    public SetupListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (plugin.getSm().isSetupChest(p)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Block b = e.getClickedBlock();
                if (b.getType().equals(Material.CHEST)) {
                    e.setCancelled(true);
                    PlayerMinion pm = plugin.getSm().getSetupChest(p);
                    pm.setChest(true);
                    pm.setChest(new PlayerMinionChest(b.getLocation()));
                    p.sendMessage(plugin.getLang().get("messages.linked"));
                    plugin.getSm().removeSetupChest(p);
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (plugin.getSm().isSetupName(p)) {
            String type = plugin.getSm().getSetupName(p);
            SetupMinion sm = plugin.getSm().getSetupMinion(p);
            if (type.equals("minionPriceCompressor")) {
                double price;
                try {
                    price = Double.parseDouble(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sm.setPriceCompressedSell(price);
                p.sendMessage(plugin.getLang().get("setup.setCompressorPrice"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionPriceSmelt")) {
                double price;
                try {
                    price = Double.parseDouble(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sm.setPriceSmeltedSell(price);
                p.sendMessage(plugin.getLang().get("setup.setSmeltPrice"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionPriceNormal")) {
                double price;
                try {
                    price = Double.parseDouble(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sm.setPriceNormalSell(price);
                p.sendMessage(plugin.getLang().get("setup.setNormalPrice"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionDelay")) {
                SetupMinionLevel sml = sm.getActual();
                int delay;
                try {
                    delay = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sml.setDelay(delay);
                p.sendMessage(plugin.getLang().get("setup.setDelay"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionLevelMenu(p, sml));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionMax")) {
                SetupMinionLevel sml = sm.getActual();
                int max;
                try {
                    max = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sml.setMax(max);
                p.sendMessage(plugin.getLang().get("setup.setMax"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionLevelMenu(p, sml));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionLevel")) {
                SetupMinionLevel sml = sm.getActual();
                int level;
                try {
                    level = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sml.setUpgradeLevel(level);
                p.sendMessage(plugin.getLang().get("setup.setLevel"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionLevelMenu(p, sml));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionCoins")) {
                SetupMinionLevel sml = sm.getActual();
                int coins;
                try {
                    coins = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sml.setUpgradeCoins(coins);
                p.sendMessage(plugin.getLang().get("setup.setCoins"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionLevelMenu(p, sml));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("craftPermission")) {
                SetupMinionLevel sml = sm.getActual();
                SetupCraft sc = sml.getCraft();
                sc.setPermission(e.getMessage());
                p.sendMessage(plugin.getLang().get("setup.setPermission"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupCraftMenu(p));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionHealth")) {
                SetupMinionLevel sml = sm.getActual();
                int health;
                try {
                    health = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sml.setHealth(health);
                p.sendMessage(plugin.getLang().get("setup.setHealth"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionLevelMenu(p, sml));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionFood")) {
                SetupMinionLevel sml = sm.getActual();
                int food;
                try {
                    food = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sml.setFood(food);
                p.sendMessage(plugin.getLang().get("setup.setFood"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionLevelMenu(p, sml));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionWorkTime")) {
                SetupMinionLevel sml = sm.getActual();
                int workTime;
                try {
                    workTime = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sml.setWorkTime(workTime);
                p.sendMessage(plugin.getLang().get("setup.setWorkTime"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionLevelMenu(p, sml));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionSleep")) {
                SetupMinionLevel sml = sm.getActual();
                int sleep;
                try {
                    sleep = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sml.setSleep(sleep);
                p.sendMessage(plugin.getLang().get("setup.setSleep"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionLevelMenu(p, sml));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionTitle")) {
                sm.setTitle(e.getMessage().replaceAll("&", "§"));
                p.sendMessage(plugin.getLang().get("setup.setTitle"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionRed")) {
                int red;
                try {
                    red = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sm.setRed(red);
                p.sendMessage(plugin.getLang().get("setup.setRed"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionBlue")) {
                int red;
                try {
                    red = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sm.setBlue(red);
                p.sendMessage(plugin.getLang().get("setup.setBlue"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionGreen")) {
                int green;
                try {
                    green = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sm.setGreen(green);
                p.sendMessage(plugin.getLang().get("setup.setGreen"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("autoSellPercent")) {
                SetupAutoSell sas = plugin.getSm().getSetupAutoSell(p);
                double percent;
                try {
                    percent = Double.parseDouble(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sas.setSell(percent);
                p.sendMessage(plugin.getLang().get("setup.setPercent"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupAutoSellMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("autoSellCraftPermission")) {
                SetupAutoSell sas = plugin.getSm().getSetupAutoSell(p);
                SetupCraft sc = sas.getCraft();
                sc.setPermission(e.getMessage());
                p.sendMessage(plugin.getLang().get("setup.setPermission"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupAutoSellCraftMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("autoSmeltCraftPermission")) {
                SetupAutoSmelt sas = plugin.getSm().getSetupAutoSmelt(p);
                SetupCraft sc = sas.getCraft();
                sc.setPermission(e.getMessage());
                p.sendMessage(plugin.getLang().get("setup.setPermission"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupAutoSmeltCraftMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("compressorCraftPermission")) {
                SetupCompressor sas = plugin.getSm().getSetupCompressor(p);
                SetupCraft sc = sas.getCraft();
                sc.setPermission(e.getMessage());
                p.sendMessage(plugin.getLang().get("setup.setPermission"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupCompressorCraftMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("autoSellCraftPermission")) {
                SetupFuel sas = plugin.getSm().getSetupFuel(p);
                SetupCraft sc = sas.getCraft();
                sc.setPermission(e.getMessage());
                p.sendMessage(plugin.getLang().get("setup.setPermission"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupFuelCraftMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionKey")) {
                sm.setKey(e.getMessage());
                p.sendMessage(plugin.getLang().get("setup.setPermission"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionLore")) {
                sm.getLore().add(e.getMessage());
                p.sendMessage(plugin.getLang().get("setup.addLore"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("autoSmeltPercent")) {
                SetupAutoSmelt sas = plugin.getSm().getSetupAutoSmelt(p);
                double percent;
                try {
                    percent = Double.parseDouble(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sas.setPercent(percent);
                p.sendMessage(plugin.getLang().get("setup.autosmelt.setPercent"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupAutoSmeltMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("autoSellPercent")) {
                SetupAutoSell sas = plugin.getSm().getSetupAutoSell(p);
                double percent;
                try {
                    percent = Double.parseDouble(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sas.setSell(percent);
                p.sendMessage(plugin.getLang().get("setup.autosell.setPercent"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupAutoSellMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("compressorAmount")) {
                SetupCompressor sas = plugin.getSm().getSetupCompressor(p);
                int amount;
                try {
                    amount = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sas.setAmount(amount);
                p.sendMessage(plugin.getLang().get("setup.compressor.setAmount"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupCompressorMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("fuelPercent")) {
                SetupFuel sas = plugin.getSm().getSetupFuel(p);
                double percent;
                try {
                    percent = Double.parseDouble(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sas.setPercent(percent);
                p.sendMessage(plugin.getLang().get("setup.fuel.setPercent"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupFuelMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("fuelDuration")) {
                SetupFuel sas = plugin.getSm().getSetupFuel(p);
                int seconds;
                try {
                    seconds = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sas.setDuration(seconds);
                p.sendMessage(plugin.getLang().get("setup.fuel.setPercent"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupFuelMenu(p, sas));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionType")) {
                List<String> types = new ArrayList<>();
                for (MinionType value : MinionType.values()) {
                    types.add(value.name());
                }
                if (!types.contains(e.getMessage().toUpperCase())) {
                    p.sendMessage(plugin.getLang().get("setup.thisNotWork"));
                    return;
                }
                sm.setType(MinionType.valueOf(e.getMessage().toUpperCase()));
                p.sendMessage(plugin.getLang().get("setup.setType"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("minionSpawn")) {
                List<String> types = new ArrayList<>();
                for (EntityType value : EntityType.values()) {
                    types.add(value.name());
                }
                if (!types.contains(e.getMessage().toUpperCase())) {
                    p.sendMessage(plugin.getLang().get("setup.thisNotType"));
                    return;
                }
                sm.setSpawn(EntityType.valueOf(e.getMessage().toUpperCase()));
                p.sendMessage(plugin.getLang().get("setup.setEntity"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupMinionMenu(p, sm));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
            if (type.equals("foodAmount")) {
                SetupFood sf = plugin.getSm().getSetupFood(p);
                int amount;
                try {
                    amount = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException ex) {
                    p.sendMessage(plugin.getLang().get("setup.writeNumber"));
                    return;
                }
                sf.setAmount(amount);
                p.sendMessage(plugin.getLang().get("setup.setupFood.setAmount"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getSem().createSetupFoodMenu(p, sf));
                e.setCancelled(true);
                plugin.getSm().removeSetupName(p);
            }
        }
    }

    @EventHandler
    public void onMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setupFood.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupFood sf = plugin.getSm().getSetupFood(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setupFood.food.nameItem"))) {
                e.setCancelled(true);
                ItemStack cursor = p.getItemInHand();
                if (cursor == null || cursor.getType().equals(Material.AIR)) {
                    p.sendMessage(plugin.getLang().get("setup.onHand"));
                    return;
                }
                sf.setFood(cursor);
                p.sendMessage(plugin.getLang().get("setup.setFoodItem"));
            }
            if (display.equals(plugin.getLang().get("menus.setupFood.amount.nameItem"))) {
                e.setCancelled(true);
                plugin.getSm().setSetupName(p, "foodAmount");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.setupFood.amount"));
            }
            if (display.equals(plugin.getLang().get("menus.setupFood.save.nameItem"))) {
                e.setCancelled(true);
                sf.save(p);
                plugin.getFm().loadFoods();
                plugin.getSm().removeSetupFood(p);
                p.sendMessage(plugin.getLang().get("setup.setupFood.save"));
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setupMainFood.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.next.nameItem"))) {
                plugin.getSem().addPage(p);
                plugin.getSem().createSetupMainFoodMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get("menus.last.nameItem"))) {
                plugin.getSem().removePage(p);
                plugin.getSem().createSetupMainFoodMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get("menus.setupMainFood.add.nameItem"))) {
                if (!plugin.getSm().isSetupFood(p)) {
                    plugin.getSm().setSetupFood(p, new SetupFood(plugin));
                }
                plugin.getSem().createSetupFoodMenu(p, plugin.getSm().getSetupFood(p));
                return;
            }
            String id = NBTEditor.getString(item, "FOOD", "KEY");
            Food food = plugin.getFm().getFoodByKey(id);
            if (food != null) {
                if (!plugin.getSm().isSetupFood(p)) {
                    plugin.getSm().setSetupFood(p, new SetupFood(plugin, food));
                }
                plugin.getSem().createSetupFoodMenu(p, plugin.getSm().getSetupFood(p));
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.setupFuel.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupFuel sas = plugin.getSm().getSetupFuel(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setup.setupFuel.result.nameItem"))) {
                ItemStack cursor = p.getItemInHand();
                if (cursor == null || cursor.getType().equals(Material.AIR)) {
                    p.sendMessage(plugin.getLang().get("setup.onHand"));
                    return;
                }
                sas.setResult(cursor);
                p.sendMessage(plugin.getLang().get("setup.setFuelItem"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupFuel.percent.nameItem"))) {
                plugin.getSm().setSetupName(p, "fuelPercent");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.fuel.percent"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupFuel.amount.nameItem"))) {
                plugin.getSm().setSetupName(p, "fuelDuration");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.fuel.duration"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupFuel.setCraft.nameItem"))) {
                plugin.getSem().createSetupFuelCraftMenu(p, sas);
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupFuel.isCraft.nameItem"))) {
                sas.setCraft(!sas.isCraft());
                p.sendMessage(plugin.getLang().get("setup.setFuelCraft").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
                plugin.getSem().updateSetupFuelMenu(sas, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupFuel.save.nameItem"))) {
                sas.save(b -> {
                    if (b) {
                        p.sendMessage(plugin.getLang().get("setup.saveFuel"));
                        p.closeInventory();
                        plugin.getSm().removeSetupFuel(p);
                        plugin.getUm().reload();
                    } else {
                        p.sendMessage(plugin.getLang().get("setup.fuel.setResult"));
                        p.closeInventory();
                    }
                });
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.setupCompressor.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupCompressor sas = plugin.getSm().getSetupCompressor(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setup.setupCompressor.result.nameItem"))) {
                ItemStack cursor = p.getItemInHand();
                if (cursor == null || cursor.getType().equals(Material.AIR)) {
                    p.sendMessage(plugin.getLang().get("setup.onHand"));
                    return;
                }
                sas.setResult(cursor);
                p.sendMessage(plugin.getLang().get("setup.setCompressorItem"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupCompressor.amount.nameItem"))) {
                plugin.getSm().setSetupName(p, "compressorAmount");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.compressor.amount"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupCompressor.setCraft.nameItem"))) {
                plugin.getSem().createSetupCompressorCraftMenu(p, sas);
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupCompressor.isCraft.nameItem"))) {
                sas.setCraft(!sas.isCraft());
                p.sendMessage(plugin.getLang().get("setup.setCompressorCraft").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
                plugin.getSem().updateSetupCompressorMenu(sas, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupCompressor.save.nameItem"))) {
                sas.save(b -> {
                    if (b) {
                        p.sendMessage(plugin.getLang().get("setup.saveCompressor"));
                        p.closeInventory();
                        plugin.getSm().removeSetupCompressor(p);
                        plugin.getUm().reload();
                    } else {
                        p.sendMessage(plugin.getLang().get("setup.compressor.setResult"));
                        p.closeInventory();
                    }
                });
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.setupAutoSmelt.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupAutoSmelt sas = plugin.getSm().getSetupAutoSmelt(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSmelt.result.nameItem"))) {
                ItemStack cursor = p.getItemInHand();
                if (cursor == null || cursor.getType().equals(Material.AIR)) {
                    p.sendMessage(plugin.getLang().get("setup.onHand"));
                    return;
                }
                sas.setResult(cursor);
                p.sendMessage(plugin.getLang().get("setup.setAutoSmeltItem"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSmelt.amount.nameItem"))) {
                plugin.getSm().setSetupName(p, "autoSmeltPercent");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.autosmelt.percent"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSmelt.setCraft.nameItem"))) {
                plugin.getSem().createSetupAutoSmeltCraftMenu(p, sas);
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSmelt.isCraft.nameItem"))) {
                sas.setCraft(!sas.isCraft());
                p.sendMessage(plugin.getLang().get("setup.setAutoSmeltCraft").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
                plugin.getSem().updateSetupAutoSmeltMenu(sas, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSmelt.save.nameItem"))) {
                sas.save(b -> {
                    if (b) {
                        p.sendMessage(plugin.getLang().get("setup.saveAutoSmelt"));
                        p.closeInventory();
                        plugin.getSm().removeSetupAutoSmelt(p);
                        plugin.getUm().reload();
                    } else {
                        p.sendMessage(plugin.getLang().get("setup.autosmelt.setResult"));
                        p.closeInventory();
                    }
                });
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.fuelCraft.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupFuel sas = plugin.getSm().getSetupFuel(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals("§7")) {
                e.setCancelled(true);
            }
            if (display.equals(plugin.getLang().get("menus.setup.fuelCraft.permission.nameItem"))) {
                e.setCancelled(true);
                plugin.getSm().setSetupName(p, "fuelCraftPermission");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.permission"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.fuelCraft.save.nameItem"))) {
                e.setCancelled(true);
                if (sas.getResult() == null && (e.getView().getItem(25) == null || e.getView().getItem(25).getType().equals(Material.AIR))) {
                    p.sendMessage(plugin.getLang().get("setup.noResult"));
                    return;
                }
                SetupCraft sc = sas.getCraft();
                ItemStack[] matrix = new ItemStack[9];
                matrix[0] = (e.getView().getItem(12) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(12);
                matrix[1] = (e.getView().getItem(13) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(13);
                matrix[2] = (e.getView().getItem(14) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(14);
                matrix[3] = (e.getView().getItem(21) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(21);
                matrix[4] = (e.getView().getItem(22) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(22);
                matrix[5] = (e.getView().getItem(23) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(23);
                matrix[6] = (e.getView().getItem(30) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(30);
                matrix[7] = (e.getView().getItem(31) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(31);
                matrix[8] = (e.getView().getItem(32) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(32);
                sc.setMatrix(matrix);
                if (e.getView().getItem(25) == null || e.getView().getItem(25).getType().equals(Material.AIR)) {
                    sc.setResult(sas.getResult());
                } else {
                    sc.setResult(e.getView().getItem(25));
                    sas.setResult(e.getView().getItem(25));
                }
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.compressorCraft.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupCompressor sas = plugin.getSm().getSetupCompressor(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals("§7")) {
                e.setCancelled(true);
            }
            if (display.equals(plugin.getLang().get("menus.setup.compressorCraft.permission.nameItem"))) {
                e.setCancelled(true);
                plugin.getSm().setSetupName(p, "compressorCraftPermission");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.permission"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.compressorCraft.save.nameItem"))) {
                e.setCancelled(true);
                if (sas.getResult() == null && (e.getView().getItem(25) == null || e.getView().getItem(25).getType().equals(Material.AIR))) {
                    p.sendMessage(plugin.getLang().get("setup.noResult"));
                    return;
                }
                SetupCraft sc = sas.getCraft();
                ItemStack[] matrix = new ItemStack[9];
                matrix[0] = (e.getView().getItem(12) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(12);
                matrix[1] = (e.getView().getItem(13) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(13);
                matrix[2] = (e.getView().getItem(14) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(14);
                matrix[3] = (e.getView().getItem(21) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(21);
                matrix[4] = (e.getView().getItem(22) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(22);
                matrix[5] = (e.getView().getItem(23) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(23);
                matrix[6] = (e.getView().getItem(30) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(30);
                matrix[7] = (e.getView().getItem(31) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(31);
                matrix[8] = (e.getView().getItem(32) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(32);
                sc.setMatrix(matrix);
                if (e.getView().getItem(25) == null || e.getView().getItem(25).getType().equals(Material.AIR)) {
                    sc.setResult(sas.getResult());
                } else {
                    sc.setResult(e.getView().getItem(25));
                    sas.setResult(e.getView().getItem(25));
                }
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.autoSmeltCraft.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupAutoSmelt sas = plugin.getSm().getSetupAutoSmelt(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals("§7")) {
                e.setCancelled(true);
            }
            if (display.equals(plugin.getLang().get("menus.setup.autoSmeltCraft.permission.nameItem"))) {
                e.setCancelled(true);
                plugin.getSm().setSetupName(p, "autoSmeltCraftPermission");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.permission"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.autoSmeltCraft.save.nameItem"))) {
                e.setCancelled(true);
                if (sas.getResult() == null && (e.getView().getItem(25) == null || e.getView().getItem(25).getType().equals(Material.AIR))) {
                    p.sendMessage(plugin.getLang().get("setup.noResult"));
                    return;
                }
                SetupCraft sc = sas.getCraft();
                ItemStack[] matrix = new ItemStack[9];
                matrix[0] = (e.getView().getItem(12) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(12);
                matrix[1] = (e.getView().getItem(13) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(13);
                matrix[2] = (e.getView().getItem(14) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(14);
                matrix[3] = (e.getView().getItem(21) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(21);
                matrix[4] = (e.getView().getItem(22) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(22);
                matrix[5] = (e.getView().getItem(23) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(23);
                matrix[6] = (e.getView().getItem(30) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(30);
                matrix[7] = (e.getView().getItem(31) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(31);
                matrix[8] = (e.getView().getItem(32) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(32);
                sc.setMatrix(matrix);
                if (e.getView().getItem(25) == null || e.getView().getItem(25).getType().equals(Material.AIR)) {
                    sc.setResult(sas.getResult());
                } else {
                    sc.setResult(e.getView().getItem(25));
                    sas.setResult(e.getView().getItem(25));
                }
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.autoSellCraft.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupAutoSell sas = plugin.getSm().getSetupAutoSell(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals("§7")) {
                e.setCancelled(true);
            }
            if (display.equals(plugin.getLang().get("menus.setup.autoSellCraft.permission.nameItem"))) {
                e.setCancelled(true);
                plugin.getSm().setSetupName(p, "autoSellCraftPermission");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.permission"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.autoSellCraft.save.nameItem"))) {
                e.setCancelled(true);
                if (sas.getResult() == null && (e.getView().getItem(25) == null || e.getView().getItem(25).getType().equals(Material.AIR))) {
                    p.sendMessage(plugin.getLang().get("setup.noResult"));
                    return;
                }
                SetupCraft sc = sas.getCraft();
                ItemStack[] matrix = new ItemStack[9];
                matrix[0] = (e.getView().getItem(12) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(12);
                matrix[1] = (e.getView().getItem(13) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(13);
                matrix[2] = (e.getView().getItem(14) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(14);
                matrix[3] = (e.getView().getItem(21) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(21);
                matrix[4] = (e.getView().getItem(22) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(22);
                matrix[5] = (e.getView().getItem(23) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(23);
                matrix[6] = (e.getView().getItem(30) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(30);
                matrix[7] = (e.getView().getItem(31) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(31);
                matrix[8] = (e.getView().getItem(32) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(32);
                sc.setMatrix(matrix);
                if (e.getView().getItem(25) == null || e.getView().getItem(25).getType().equals(Material.AIR)) {
                    sc.setResult(sas.getResult());
                } else {
                    sc.setResult(e.getView().getItem(25));
                    sas.setResult(e.getView().getItem(25));
                }
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.setupAutoSell.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupAutoSell sas = plugin.getSm().getSetupAutoSell(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSell.result.nameItem"))) {
                ItemStack cursor = p.getItemInHand();
                if (cursor == null || cursor.getType().equals(Material.AIR)) {
                    p.sendMessage(plugin.getLang().get("setup.onHand"));
                    return;
                }
                sas.setResult(cursor);
                p.sendMessage(plugin.getLang().get("setup.setAutoSellItem"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSell.amount.nameItem"))) {
                plugin.getSm().setSetupName(p, "autoSellPercent");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.autosell.percent"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSell.setCraft.nameItem"))) {
                plugin.getSem().createSetupAutoSellCraftMenu(p, sas);
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSell.isCraft.nameItem"))) {
                sas.setCraft(!sas.isCraft());
                p.sendMessage(plugin.getLang().get("setup.setAutoSellCraft").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
                plugin.getSem().updateSetupAutoSellMenu(sas, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.setupAutoSell.save.nameItem"))) {
                sas.save(b -> {
                    if (b) {
                        p.sendMessage(plugin.getLang().get("setup.saveAutoSell"));
                        p.closeInventory();
                        plugin.getSm().removeSetupAutoSell(p);
                        plugin.getUm().reload();
                    } else {
                        p.sendMessage(plugin.getLang().get("setup.autosell.setResult"));
                        p.closeInventory();
                    }
                });
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.craft.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupMinion sm = plugin.getSm().getSetupMinion(p);
            SetupMinionLevel sml = sm.getActual();
            String display = item.getItemMeta().getDisplayName();
            if (display.equals("§7")) {
                e.setCancelled(true);
            }
            if (display.equals(plugin.getLang().get("menus.setup.craft.permission.nameItem"))) {
                e.setCancelled(true);
                plugin.getSm().setSetupName(p, "craftPermission");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.permission"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.craft.save.nameItem"))) {
                e.setCancelled(true);
                if (e.getView().getItem(25) == null) {
                    if (e.getClick().equals(ClickType.DOUBLE_CLICK)) {
                        p.sendMessage(plugin.getLang().get("setup.changeURL"));
                    } else {
                        p.sendMessage(plugin.getLang().get("setup.noCraft"));
                        return;
                    }
                }
                SetupCraft sc = sml.getCraft();
                ItemStack[] matrix = new ItemStack[9];
                matrix[0] = (e.getView().getItem(12) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(12);
                matrix[1] = (e.getView().getItem(13) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(13);
                matrix[2] = (e.getView().getItem(14) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(14);
                matrix[3] = (e.getView().getItem(21) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(21);
                matrix[4] = (e.getView().getItem(22) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(22);
                matrix[5] = (e.getView().getItem(23) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(23);
                matrix[6] = (e.getView().getItem(30) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(30);
                matrix[7] = (e.getView().getItem(31) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(31);
                matrix[8] = (e.getView().getItem(32) == null) ? new ItemStack(Material.AIR) : e.getView().getItem(32);
                sc.setMatrix(matrix);
                if (e.getView().getItem(25) == null || e.getView().getItem(25).getType().equals(Material.AIR)) {
                    sc.setResult(null);
                } else {
                    sc.setResult(e.getView().getItem(25));
                }
                p.closeInventory();
                plugin.getSem().createSetupMinionLevelMenu(p, sml);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.minionLevel.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupMinion sm = plugin.getSm().getSetupMinion(p);
            SetupMinionLevel sml = sm.getActual();
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.delay.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionDelay");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.delay"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.max.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionMax");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.max"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.upgradeLevel.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionLevel");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.level"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.upgradeCoins.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionCoins");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.coins"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.health.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionHealth");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.health"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.food.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionFood");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.food"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.workTime.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionWorkTime");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.workTime"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.sleep.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionSleep");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.sleep"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.isCoins.nameItem"))) {
                sml.setCoins(!sml.isCoins());
                p.sendMessage(plugin.getLang().get("setup.setIsCoins").replaceAll("<state>", Utils.parseBoolean(sml.isCoins())));
                plugin.getSem().updateSetupMinionLevelMenu(sml, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.isLevel.nameItem"))) {
                sml.setLevel(!sml.isLevel());
                p.sendMessage(plugin.getLang().get("setup.setIsLevel").replaceAll("<state>", Utils.parseBoolean(sml.isLevel())));
                plugin.getSem().updateSetupMinionLevelMenu(sml, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.isCraft.nameItem"))) {
                sml.setCraft(!sml.isCraft());
                p.sendMessage(plugin.getLang().get("setup.setIsCraft").replaceAll("<state>", Utils.parseBoolean(sml.isCraft())));
                plugin.getSem().updateSetupMinionLevelMenu(sml, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.setCraft.nameItem"))) {
                if (!sml.isCraft()) {
                    p.sendMessage(plugin.getLang().get("setup.craftNotEnabled"));
                    return;
                }
                if (sml.getCraft() == null) {
                    sml.setCraft(new SetupCraft());
                }
                plugin.getSem().createSetupCraftMenu(p);
            }
            if (display.equals(plugin.getLang().get("menus.setup.minionLevel.save.nameItem"))) {
                p.closeInventory();
                sm.saveLevel(p);
                plugin.getSem().createSetupMinionMenu(p, sm);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.next.nameItem"))) {
                e.setCancelled(true);
                plugin.getSem().addPage(p);
                plugin.getSem().createSetupMainMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get("menus.last.nameItem"))) {
                e.setCancelled(true);
                plugin.getSem().removePage(p);
                plugin.getSem().createSetupMainMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get("menus.setup.main.add.nameItem"))) {
                e.setCancelled(true);
                if (!plugin.getSm().isSetupMinion(p)) {
                    plugin.getSm().setSetupMinion(p, new SetupMinion(plugin));
                }
                SetupMinion sm = plugin.getSm().getSetupMinion(p);
                plugin.getSem().createSetupMinionMenu(p, sm);
                return;
            }
            String key = Utils.ObjectOrDefaultString(NBTEditor.getString(item, "KEY"), "NONE");
            if (key.equals("NONE")) {
                return;
            }
            e.setCancelled(true);
            Minion m = plugin.getMm().getMinion(key);
            SetupMinion sm = new SetupMinion(plugin, m);
            plugin.getSm().setSetupMinion(p, sm);
            plugin.getSem().createSetupMinionMenu(p, sm);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.minion.title"))) {
            if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                return;
            }
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupMinion sm = plugin.getSm().getSetupMinion(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setup.minion.entity.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionSpawn");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.entity"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.type.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionType");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.type"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.levels.nameItem"))) {
                if (sm.getActual() == null) {
                    sm.setActual(new SetupMinionLevel(sm, sm.getLevels().size() + 1));
                }
                SetupMinionLevel sml = sm.getActual();
                plugin.getSem().createSetupMinionLevelMenu(p, sml);
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.lore.nameItem"))) {
                if (e.getClick().equals(ClickType.LEFT)) {
                    plugin.getSm().setSetupName(p, "minionLore");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get("setup.lore"));
                } else {
                    if (sm.getLore().isEmpty()) {
                        p.sendMessage(plugin.getLang().get("setup.noLine"));
                        return;
                    }
                    sm.getLore().remove(sm.getLore().size() - 1);
                    plugin.getSem().createSetupMinionMenu(p, sm);
                    p.sendMessage(plugin.getLang().get("setup.removeLast"));
                }
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.key.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionKey");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.key"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.red.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionRed");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.red"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.blue.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionBlue");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.blue"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.green.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionGreen");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.green"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.minionTitle.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionTitle");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.title"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.priceCompressor.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionPriceCompressor");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.priceCompressor"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.priceSmelt.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionPriceSmelt");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.priceSmelt"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.priceNormal.nameItem"))) {
                plugin.getSm().setSetupName(p, "minionPriceNormal");
                p.closeInventory();
                p.sendMessage(plugin.getLang().get("setup.priceNormal"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.fuelEnabled.nameItem"))) {
                sm.setFuelEnabled(!sm.isFuelEnabled());
                p.sendMessage(plugin.getLang().get("setup.fuelUpgrade").replaceAll("<state>", Utils.parseBoolean(sm.isFuelEnabled())));
                plugin.getSem().updateSetupMinionMenu(sm, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.autoSellEnabled.nameItem"))) {
                sm.setAutoSellEnabled(!sm.isAutoSellEnabled());
                p.sendMessage(plugin.getLang().get("setup.autoSellUpgrade").replaceAll("<state>", Utils.parseBoolean(sm.isAutoSellEnabled())));
                plugin.getSem().updateSetupMinionMenu(sm, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.smeltEnabled.nameItem"))) {
                sm.setSmeltEnabled(!sm.isSmeltEnabled());
                p.sendMessage(plugin.getLang().get("setup.smeltUpgrade").replaceAll("<state>", Utils.parseBoolean(sm.isSmeltEnabled())));
                plugin.getSem().updateSetupMinionMenu(sm, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.compressorEnabled.nameItem"))) {
                sm.setCompressorEnabled(!sm.isCompressorEnabled());
                p.sendMessage(plugin.getLang().get("setup.compressorUpgrade").replaceAll("<state>", Utils.parseBoolean(sm.isCompressorEnabled())));
                plugin.getSem().updateSetupMinionMenu(sm, e.getClickedInventory());
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.place.nameItem"))) {
                ItemStack cursor = p.getItemInHand();
                if (cursor == null || cursor.getType().equals(Material.AIR)) {
                    p.sendMessage(plugin.getLang().get("setup.onHand"));
                    return;
                }
                if (sm.getType().equals(MinionType.LUMBERJACK) && !MinionType.LUMBERJACK.check(cursor.getType())) {
                    p.sendMessage(plugin.getLang().get("setup.onlyAccept").replaceAll("<works>", MinionType.LUMBERJACK.toString()));
                    return;
                }
                if (sm.getType().equals(MinionType.PEASANT) && !MinionType.PEASANT.check(cursor.getType())) {
                    p.sendMessage(plugin.getLang().get("setup.onlyAccept").replaceAll("<works>", MinionType.PEASANT.toString()));
                    return;
                }
                sm.setPlace(cursor);
                p.sendMessage(plugin.getLang().get("setup.setPlace"));
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.give.nameItem"))) {
                plugin.getSem().createAddGiveInInvItems(p, sm);
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.compressor.nameItem"))) {
                plugin.getSem().createAddCompressorItems(p, sm);
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.smelt.nameItem"))) {
                plugin.getSem().createAddSmeltItems(p, sm);
            }
            if (display.equals(plugin.getLang().get("menus.setup.minion.save.nameItem"))) {
                if (sm.getLevels().size() < 1) {
                    p.sendMessage(plugin.getLang().get("setup.oneLevel"));
                    return;
                }
                sm.save(p);
                plugin.getSm().removeSetupMinion(p);
                p.closeInventory();
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.reload(), 2L);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.giveInInv.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupMinion sm = plugin.getSm().getSetupMinion(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setup.giveInInv.save.nameItem"))) {
                e.setCancelled(true);
                sm.getGiveInInv().clear();
                for (int i = 0; i < 27; i++) {
                    ItemStack it = e.getView().getItem(i);
                    if (it == null || it.getType().equals(Material.AIR)) {
                        continue;
                    }
                    sm.getGiveInInv().add(it);
                }
                p.sendMessage(plugin.getLang().get("setup.giveInInvSave"));
                plugin.getSem().createSetupMinionMenu(p, sm);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.smelt.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupMinion sm = plugin.getSm().getSetupMinion(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setup.smelt.save.nameItem"))) {
                e.setCancelled(true);
                sm.getAutoSmelt().clear();
                for (int i = 0; i < 27; i++) {
                    ItemStack it = e.getView().getItem(i);
                    if (it == null || it.getType().equals(Material.AIR)) {
                        continue;
                    }
                    sm.getAutoSmelt().add(it);
                }
                p.sendMessage(plugin.getLang().get("setup.smeltSave"));
                plugin.getSem().createSetupMinionMenu(p, sm);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.compressor.title"))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            SetupMinion sm = plugin.getSm().getSetupMinion(p);
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.setup.compressor.save.nameItem"))) {
                e.setCancelled(true);
                sm.getCompressor().clear();
                for (int i = 0; i < 27; i++) {
                    ItemStack it = e.getView().getItem(i);
                    if (it == null || it.getType().equals(Material.AIR)) {
                        continue;
                    }
                    sm.getCompressor().add(it);
                }
                p.sendMessage(plugin.getLang().get("setup.smeltSave"));
                plugin.getSem().createSetupMinionMenu(p, sm);
            }
        }
    }

}