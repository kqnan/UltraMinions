package io.github.Leonardo0013YT.UltraMinions.listeners;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionCollectEvent;
import io.github.Leonardo0013YT.UltraMinions.craft.Craft;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerData;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.minions.skins.MinionSkin;
import io.github.Leonardo0013YT.UltraMinions.shop.ShopItem;
import io.github.Leonardo0013YT.UltraMinions.tiers.Tier;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSell;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSmelt;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeCompressor;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;
import io.github.Leonardo0013YT.UltraMinions.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraMinions.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuListener implements Listener {

    private Main plugin;

    public MenuListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.minion.adminTitle"))) {
            e.setCancelled(true);
            p.sendMessage(plugin.getLang().get("messages.noInteractAdmin"));
            return;
        }
        if (e.getView().getTitle().equals((plugin.getLang().get("menus.shop.title").length() > 32) ? plugin.getLang().get("menus.shop.title").substring(0, 32) : plugin.getLang().get("menus.shop.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.last.nameItem"))) {
                plugin.getMem().removePage(p);
                plugin.getMem().createShopMenu(p);
            }
            if (display.equals(plugin.getLang().get("menus.next.nameItem"))) {
                plugin.getMem().addPage(p);
                plugin.getMem().createShopMenu(p);
            }
            if (display.equals(plugin.getLang().get("menus.close.nameItem"))) {
                p.closeInventory();
            }
            String key = NBTEditor.getString(item, "UltraMinions", "MINION", "SHOP", "KEY");
            if (key == null) return;
            ShopItem si = plugin.getShm().getShop().get(key);
            if (si == null) return;

            if (plugin.getAdm().getCoins(p) >= si.getPrice()) {
                plugin.getAdm().removeCoins(p, si.getPrice());
                MinionLevel ml = si.getMinion().getMinionLevelByLevel(1);
                Utils.addItems(p, ml.getMinionHead());
                p.sendMessage(plugin.getLang().get("messages.purchased").replaceAll("<title>", ml.getLevelTitle()).replaceAll("<coins>", String.valueOf(si.getPrice())));
            } else {
                p.sendMessage(plugin.getLang().get("messages.noMoney"));
            }
        }
        if (plugin.getMm().getView(p) == null) {
            return;
        }
        PlayerMinion pm = plugin.getMm().getView(p);
        if (e.getView().getTitle().equals((plugin.getLang().get("menus.layout.title").length() > 32) ? plugin.getLang().get("menus.layout.title").substring(0, 32) : plugin.getLang().get("menus.layout.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.back.nameItem"))) {
                plugin.getMem().createMinionMenu(p, pm);
            }
            if (display.equals(plugin.getLang().get("menus.close.nameItem"))) {
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.stat.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            String display = item.getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get("menus.stat.chest.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    if (pm.getChest() == null) {
                        p.sendMessage(plugin.getLang().get("messages.noLink"));
                        return;
                    }
                    if (!pm.getChest().isChest()) {
                        p.sendMessage(plugin.getLang().get("messages.linkBroken"));
                    }
                    pm.setChest(null);
                    pm.setChest(false);
                    p.sendMessage(plugin.getLang().get("messages.unlinked"));
                } else {
                    if (pm.getChest() != null) {
                        p.sendMessage(plugin.getLang().get("messages.alreadyLink"));
                        return;
                    }
                    plugin.getSm().setSetupChest(p, pm);
                    p.sendMessage(plugin.getLang().get("messages.link"));
                }
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.craft.title").replaceAll("<title>", pm.getMinion().getTitle()))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            String display = item.getItemMeta().getDisplayName();
            MinionLevel next = pm.getMinion().getMinionLevelByLevel(pm.getStat().getLevel() + 1);
            if (next == null) {
                return;
            }
            if (display.equals(plugin.getLang().get("menus.craft.upgrade.nameItem"))) {
                updateMinionLevel(p, pm, next);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.upgrade.title").replaceAll("<title>", pm.getMinion().getTitle()))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            ItemStack cursor = p.getItemOnCursor();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            String display = item.getItemMeta().getDisplayName();
            MinionLevel next = pm.getMinion().getMinionLevelByLevel(pm.getStat().getLevel() + 1);
            if (display.equals(plugin.getLang().get("menus.upgrade.level.nameItem"))) {
                if (p.getLevel() < next.getUpgradeLevels()) {
                    p.playSound(p.getLocation(), plugin.getCfm().getNoUpgrade(), 1.0f, 1.0f);
                    p.sendMessage(plugin.getLang().get("messages.noLevels"));
                    return;
                }
                p.setLevel(p.getLevel() - next.getUpgradeLevels());
                PlayerMinionStat stat = pm.getStat();
                stat.setLevel(stat.getLevel() + 1);
                pm.setStat(stat);
                p.playSound(p.getLocation(), plugin.getCfm().getUpgrade(), 1.0f, 1.0f);
                p.sendMessage(plugin.getLang().get("messages.upgradeLevels").replaceAll("<title>", pm.getMinion().getTitle()).replaceAll("<level>", String.valueOf(next.getUpgradeLevels())));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get("menus.upgrade.coins.nameItem"))) {
                if (!plugin.getAdm().hasEconomyPlugin()) {
                    p.sendMessage(plugin.getLang().get("messages.noEconomyPlugin"));
                    return;
                }
                if (plugin.getAdm().getCoins(p) < next.getUpgradeCoins()) {
                    p.playSound(p.getLocation(), plugin.getCfm().getNoUpgrade(), 1.0f, 1.0f);
                    p.sendMessage(plugin.getLang().get("messages.noCoins"));
                    return;
                }
                plugin.getAdm().removeCoins(p, next.getUpgradeCoins());
                PlayerMinionStat stat = pm.getStat();
                stat.setLevel(stat.getLevel() + 1);
                pm.setStat(stat);
                p.playSound(p.getLocation(), plugin.getCfm().getUpgrade(), 1.0f, 1.0f);
                p.sendMessage(plugin.getLang().get("messages.upgradeCoins").replaceAll("<title>", pm.getMinion().getTitle()).replaceAll("<coins>", String.valueOf(next.getUpgradeCoins())));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get("menus.upgrade.craft.nameItem"))) {
                if (e.getClick().equals(ClickType.DOUBLE_CLICK)) {
                    updateMinionLevel(p, pm, next);
                    return;
                }
                if (e.getClick().equals(ClickType.LEFT)) {
                    if (cursor == null || cursor.getType().equals(Material.AIR)) {
                        p.sendMessage(plugin.getLang().get("setup.onCursor"));
                        return;
                    }
                    if (next.getCraft().getResult().equals(cursor)) {
                        PlayerMinionStat stat = pm.getStat();
                        stat.setLevel(stat.getLevel() + 1);
                        pm.setStat(stat);
                        p.playSound(p.getLocation(), plugin.getCfm().getUpgrade(), 1.0f, 1.0f);
                    } else {
                        p.playSound(p.getLocation(), plugin.getCfm().getNoUpgrade(), 1.0f, 1.0f);
                        p.sendMessage(plugin.getLang().get("messages.noCraft"));
                    }
                } else {
                    plugin.getMem().createPreviewCraftMenu(p, pm, next);
                }
            }
        }
        if (e.getView().getTitle().equals((pm.getMinionLevel().getLevelTitle().length() > 32) ? pm.getMinionLevel().getLevelTitle().substring(0, 32) : pm.getMinionLevel().getLevelTitle())) {
            if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
            if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                if (e.isShiftClick() || e.getClick().equals(ClickType.DOUBLE_CLICK)) {
                    e.setCancelled(true);
                }
                return;
            }
            if (plugin.getMem().isInv(e.getSlot())) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (item.getType().equals(Material.PLAYER_HEAD) && e.getSlot() == 4) {
                e.setCancelled(true);
                if (plugin.getCfm().isAnyStat()) {
                    p.sendMessage(plugin.getLang().get("messages.anyStat"));
                    return;
                }
                plugin.getMem().createMinionStat(p, pm);
                return;
            }
            ItemStack cursor = p.getItemOnCursor();
            if (plugin.getMem().isInv(e.getSlot())) {
                e.setCancelled(true);
                p.sendMessage(plugin.getLang().get("messages.noCollect"));
                return;
            }
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            String display = item.getItemMeta().getDisplayName();
            if (display.equals("§7")) {
                e.setCancelled(true);
            }
            if (display.equals(plugin.getLang().get("menus.minion.quick.nameItem"))) {
                e.setCancelled(true);
                MinionLevel next = pm.getMinion().getMinionLevelByLevel(pm.getStat().getLevel() + 1);
                updateMinionLevel(p, pm, next);
            }
            if (display.equals(plugin.getLang().get("menus.minion.blockedSlot.nameItem"))) {
                e.setCancelled(true);
            }
            if (display.equals(plugin.getLang().get("menus.minion.layout.nameItem"))) {
                e.setCancelled(true);
                plugin.getMem().createIdealLayout(p, pm);
            }
            if (display.equals(plugin.getLang().get("menus.minion.info.nameItem").replaceAll("<type>", pm.getMinionLevel().getLevelTitle()))) {
                e.setCancelled(true);
                plugin.getMem().createMinionStat(p, pm);
            }
            if (display.equals(plugin.getLang().get("menus.minion.collect.nameItem"))) {
                e.setCancelled(true);
                HashMap<ItemStack, Integer> items = pm.getItems();
                if (items.isEmpty()) {
                    p.sendMessage(plugin.getLang().get("messages.noMaterials"));
                    return;
                }
                MinionCollectEvent ec = new MinionCollectEvent(new HashMap<>(items), p);
                Bukkit.getPluginManager().callEvent(ec);
                Utils.addItems(p, ec.getItems(), pm.getMinionLevel().getMax());
                pm.setFull(false);
                pm.setActions(0);
                if (plugin.getAdm().hasHologram(pm)) {
                    plugin.getAdm().deleteHologram(pm);
                }
                pm.getItems().clear();
                p.sendMessage(plugin.getLang().get("messages.collect"));
                plugin.getMem().createMinionMenu(p, pm);
            }
            if (display.equals(plugin.getLang().get("menus.minion.next.nameItem"))) {
                e.setCancelled(true);
                MinionLevel ml = pm.getMinion().getMinionLevelByLevel(pm.getStat().getLevel() + 1);
                if (ml == null) {
                    p.sendMessage(plugin.getLang().get("messages.maxLevel"));
                    return;
                }
                plugin.getMem().createMinionLevelMenu(p, pm, ml);
            }
            if (display.equals(plugin.getLang().get("menus.minion.pickup.nameItem"))) {
                e.setCancelled(true);
                p.closeInventory();
                if (plugin.getAdm().isAddon() && plugin.getAdm().checkMember(p) && plugin.getCfm().isRemoveMinion()) {
                    placeMinion(p, pm);
                } else {
                    if (pm.getP().getUniqueId().equals(p.getUniqueId())) {
                        placeMinion(p, pm);
                    } else {
                        p.sendMessage(plugin.getLang().get("messages.noDestroyYour"));
                    }
                }
            }
            if (e.getSlot() == 10) {
                if (e.isShiftClick()) {
                    e.setCancelled(true);
                    return;
                }
                if (display.equals(plugin.getLang().get("menus.minion.skin.nameItem"))) {
                    if (p.getItemOnCursor() != null) {
                        MinionSkin skin = plugin.getSkm().getMinionSkinByName(cursor);
                        if (skin != null) {
                            pm.setSkin(skin.getName());
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.setItemOnCursor(null);
                                }
                            }.runTaskLater(plugin, 1);
                        } else {
                            e.setCancelled(true);
                            p.sendMessage(plugin.getLang().get("messages.noSkin"));
                        }
                    }
                } else {
                    if (p.getItemOnCursor() != null && !p.getItemOnCursor().getType().equals(Material.AIR)) {
                        e.setCancelled(true);
                        return;
                    }
                    pm.setSkin("none");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ItemStack skin = ItemBuilder.item(Material.GREEN_STAINED_GLASS_PANE, 1, (short) 5, plugin.getLang().get("menus.minion.skin.nameItem"), plugin.getLang().get("menus.minion.skin.loreItem"));
                            e.getInventory().setItem(10, skin);
                        }
                    }.runTaskLater(plugin, 1);
                }
            }
            if (e.getSlot() == 19) {
                if (e.isShiftClick()) {
                    e.setCancelled(true);
                    return;
                }
                if (!pm.getMinion().isFuelEnabled()) {
                    e.setCancelled(true);
                    return;
                }
                if (display.equals(plugin.getLang().get("menus.minion.fuel.nameItem"))) {
                    if (cursor == null) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get("messages.noCursor"));
                        return;
                    }
                    UpgradeFuel uf = plugin.getUm().getFuel(false, cursor);
                    if (uf == null) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get("messages.noUpgradeFuel"));
                        return;
                    }
                    pm.getUpgrade().setFuel(uf);
                    pm.getStat().setFuel(System.currentTimeMillis());
                    pm.getStat().setAmountFuel(cursor.getAmount());
                    updateMinionInv(e, p, pm);
                } else {
                    if (cursor != null && !cursor.getType().equals(Material.AIR)) {
                        UpgradeFuel uf = plugin.getUm().getFuel(false, cursor);
                        if (uf == null) {
                            e.setCancelled(true);
                            p.sendMessage(plugin.getLang().get("messages.inCursor"));
                            return;
                        }
                        if (uf.isUnlimited()) {
                            e.setCancelled(true);
                            p.sendMessage(plugin.getLang().get("messages.inCursor"));
                            return;
                        }
                        String keyNow = NBTEditor.getString(item, "MINION", "FUEL", "TIME");
                        UpgradeFuel key = plugin.getUm().getFuel(keyNow);
                        if (!key.getName().equals(uf.getName())) {
                            e.setCancelled(true);
                            p.sendMessage(plugin.getLang().get("messages.noFuelEquals"));
                        } else {
                            e.setCancelled(true);
                            int a = pm.getStat().getAmountFuel(uf) + cursor.getAmount();
                            if (a > 64) {
                                p.sendMessage(plugin.getLang().get("messages.noMore64Fuel"));
                                return;
                            }
                            pm.getStat().setAmountFuel(a);
                            updateMinionInv(e, p, pm);
                        }
                        return;
                    }
                    String key = NBTEditor.getString(item, "MINION", "FUEL", "TIME");
                    if (key != null) {
                        UpgradeFuel uf = plugin.getUm().getFuel(key);
                        if (uf != null) {
                            e.setCancelled(true);
                            if (!uf.isUnlimited()) {
                                p.setItemOnCursor(null);
                            } else {
                                Utils.addItems(p, uf.getResult(false));
                            }
                        }
                    }
                    pm.getStat().setTotalFuel(0);
                    pm.getStat().setAmountFuel(0);
                    pm.getStat().setFuel(0);
                    pm.getUpgrade().setFuel(null);
                    p.sendMessage(plugin.getLang().get("messages.removedFuel"));
                    MinionLevel ml = pm.getMinionLevel();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ItemStack info = ItemBuilder.createSkull(plugin.getLang().get("menus.minion.info.nameItem").replaceAll("<type>", ml.getLevelTitle()), getInfoLore(pm), ml.getUrl());
                            ItemStack fuel = ItemBuilder.item(Material.ORANGE_STAINED_GLASS_PANE, 1, (short) 1, plugin.getLang().get("menus.minion.fuel.nameItem"), plugin.getLang().get("menus.minion.fuel.loreItem"));
                            e.getInventory().setItem(19, fuel);
                            e.getInventory().setItem(4, info);
                        }
                    }.runTaskLater(plugin, 1);
                }
            }
            if (e.getSlot() == 28) {
                if (e.isShiftClick()) {
                    e.setCancelled(true);
                    return;
                }
                if (!pm.getMinion().isAutoSellEnabled()) {
                    e.setCancelled(true);
                    return;
                }
                if (display.equals(plugin.getLang().get("menus.minion.shipping.nameItem"))) {
                    if (cursor == null) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get("messages.noCursor"));
                        return;
                    }
                    UpgradeAutoSell uf = plugin.getUm().getAutoSell(cursor);
                    if (uf == null) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get("messages.noUpgradeAutoSell"));
                        return;
                    }
                    pm.getUpgrade().setAutoSell(uf);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.setItemOnCursor(null);
                        }
                    }.runTaskLater(plugin, 1);
                } else {
                    if (p.getItemOnCursor() != null && !p.getItemOnCursor().getType().equals(Material.AIR)) {
                        e.setCancelled(true);
                        return;
                    }
                    pm.getUpgrade().setAutoSell(null);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ItemStack shipping = ItemBuilder.item(Material.BLUE_STAINED_GLASS_PANE, 1, (short) 11, plugin.getLang().get("menus.minion.shipping.nameItem"), plugin.getLang().get("menus.minion.shipping.loreItem"));
                            e.getInventory().setItem(28, shipping);
                        }
                    }.runTaskLater(plugin, 1);
                }
            }
            if (e.getSlot() == 37) {
                if (e.isShiftClick()) {
                    e.setCancelled(true);
                    return;
                }
                if (!pm.getMinion().isCompressorEnabled()) {
                    e.setCancelled(true);
                    return;
                }
                if (display.equals(plugin.getLang().get("menus.minion.update.nameItem"))) {
                    if (cursor == null) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get("messages.noCursor"));
                        return;
                    }
                    UpgradeCompressor uc = plugin.getUm().getCompressor(cursor);
                    if (uc == null) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get("messages.noUpgradeCompressor"));
                        return;
                    }
                    if (uc != null) {
                        pm.getUpgrade().setCompressor(uc);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.setItemOnCursor(null);
                        }
                    }.runTaskLater(plugin, 1);
                } else {
                    if (p.getItemOnCursor() != null && !p.getItemOnCursor().getType().equals(Material.AIR)) {
                        e.setCancelled(true);
                        return;
                    }
                    UpgradeCompressor uc = plugin.getUm().getCompressor(item);
                    if (uc != null) {
                        pm.getUpgrade().setCompressor(null);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ItemStack update = ItemBuilder.item(Material.YELLOW_STAINED_GLASS_PANE, 1, (short) 4, plugin.getLang().get("menus.minion.update.nameItem"), plugin.getLang().get("menus.minion.update.loreItem"));
                            e.getInventory().setItem(37, update);
                        }
                    }.runTaskLater(plugin, 1);
                }
            }
            if (e.getSlot() == 46) {
                if (e.isShiftClick()) {
                    e.setCancelled(true);
                    return;
                }
                if (!pm.getMinion().isSmeltEnabled()) {
                    e.setCancelled(true);
                    return;
                }
                if (display.equals(plugin.getLang().get("menus.minion.update.nameItem"))) {
                    if (cursor == null) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get("messages.noCursor"));
                        return;
                    }
                    UpgradeAutoSmelt ua = plugin.getUm().getAutoSmelt(cursor);
                    if (ua == null) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get("messages.noUpgradeAutoSmelt"));
                        return;
                    }
                    if (ua != null) {
                        pm.getUpgrade().setAutoSmelt(ua);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.setItemOnCursor(null);
                        }
                    }.runTaskLater(plugin, 1);
                } else {
                    if (p.getItemOnCursor() != null && !p.getItemOnCursor().getType().equals(Material.AIR)) {
                        e.setCancelled(true);
                        return;
                    }
                    UpgradeAutoSmelt ua = plugin.getUm().getAutoSmelt(item);
                    if (ua != null) {
                        pm.getUpgrade().setAutoSmelt(null);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ItemStack update = ItemBuilder.item(Material.YELLOW_STAINED_GLASS_PANE, 1, (short) 4, plugin.getLang().get("menus.minion.update.nameItem"), plugin.getLang().get("menus.minion.update.loreItem"));
                            e.getInventory().setItem(46, update);
                        }
                    }.runTaskLater(plugin, 1);
                }
            }
        }
    }

    private void updateMinionLevel(Player p, PlayerMinion pm, MinionLevel next) {
        boolean has = false;
        Craft craft = next.getCraft();
        if (craft.getPermission() != null && !craft.getPermission().equals("none") && !p.hasPermission(craft.getPermission())) {
            return;
        }
        if (plugin.getCfm().isLevelPermission()) {
            if (!p.hasPermission("minions.craft." + craft.getKey() + "." + craft.getLevel())) {
                return;
            }
        }
        for (ItemStack i : craft.getAtLeast().keySet()) {
            if (i == null || i.getType().equals(Material.AIR)) continue;
            int amo = craft.getAtLeast().get(i);
            if (p.getInventory().containsAtLeast(i, amo)) {
                has = true;
            } else {
                has = false;
                break;
            }
        }
        if (has) {
            plugin.getMm().close(pm);
            for (ItemStack i : craft.getAtLeast().keySet()) {
                if (i == null || i.getType().equals(Material.AIR)) continue;
                int amo = craft.getAtLeast().get(i);
                i.setAmount(amo);
                removeItem(p.getInventory(), i);
            }
            PlayerMinionStat stat = pm.getStat();
            stat.setLevel(stat.getLevel() + 1);
            pm.setStat(stat);
            p.playSound(p.getLocation(), plugin.getCfm().getUpgrade(), 1.0f, 1.0f);
            p.closeInventory();
            p.sendMessage(plugin.getLang().get("messages.upgradeCraft").replaceAll("<title>", pm.getMinion().getTitle()));
            String key = pm.getKey();
            int level = stat.getLevel();
            PlayerData pd = PlayerData.getPlayerData(p);
            if (plugin.getCfm().isUnlockingTiers()) {
                if (pd.isNewUnlocked(key, level)) {
                    pd.setLevel(key, level);
                    Tier now = plugin.getTm().getTierByUnlocked(pd.getUnlocked());
                    Tier tnext = plugin.getTm().getTierByUnlocked(pd.getUnlocked() + 1);
                    pd.setUnlocked(pd.getUnlocked() + 1);
                    if (tnext != null) {
                        if (!now.equals(tnext)) {
                            p.sendMessage(plugin.getAdm().getTier(p).getMsg().replaceAll("<now>", String.valueOf(pd.getMinionSize())).replaceAll("<max>", String.valueOf(plugin.getAdm().getMaxMinion(p))));
                        } else {
                            Tier nt = plugin.getTm().getNextTier(tnext);
                            p.sendMessage(plugin.getLang().get("messages.newMinion").replaceAll("<title>", pm.getMinionLevel().getLevelTitle()).replaceAll("<tier>", String.valueOf(nt.getRequired() - pd.getUnlocked())));
                        }
                    }
                }
            }
            plugin.getMm().removeView(p);
        } else {
            p.sendMessage(plugin.getLang().get("messages.noHasMaterials"));
        }
    }

    private void updateMinionInv(InventoryClickEvent e, Player p, PlayerMinion pm) {
        MinionLevel ml = pm.getMinionLevel();
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack info = ItemBuilder.createSkull(plugin.getLang().get("menus.minion.info.nameItem").replaceAll("<type>", ml.getLevelTitle()), getInfoLore(pm), ml.getUrl());
                p.setItemOnCursor(null);
                ItemStack i = NBTEditor.set(pm.getUpgrade().getFuel().getResult(true, getTime(pm.getStat(), pm.getUpgrade().getFuel())), pm.getUpgrade().getFuel().getName(), "MINION", "FUEL", "TIME");
                i.setAmount(pm.getStat().getAmountFuel(pm.getUpgrade().getFuel()));
                e.getInventory().setItem(19, i);
                e.getInventory().setItem(4, info);
            }
        }.runTaskLater(plugin, 1);
    }

    private void placeMinion(Player p, PlayerMinion pm) {
        PlayerData pd = PlayerData.getPlayerUUID(pm.getP().getUniqueId());
        Minion m = plugin.getMm().getMinion(pm.getKey());
        MinionLevel ml = m.getMinionLevelByLevel(pm.getStat().getLevel());
        plugin.getMm().getActiveMinions().remove(pm.getArmor().getUniqueId());
        MinionCollectEvent ec = new MinionCollectEvent(new HashMap<>(pm.getItems()), p);
        Bukkit.getPluginManager().callEvent(ec);
        Utils.addItems(p, ec.getItems(), pm.getMinionLevel().getMax());
        pm.setActions(0);
        pm.destroy();
        pm.setFull(false);
        pm.getItems().clear();
        pd.removePlayerMinion(pm);
        Utils.addItems(p, ml.getMinionHead(pm));
        if (pm.getUpgrade().getFuel() != null) {
            UpgradeFuel uf = pm.getUpgrade().getFuel();
            if (uf.isUnlimited()) {
                Utils.addItems(p, uf.getResult(false));
            }
        }
        p.sendMessage(plugin.getLang().get("messages.removedMinion").replaceAll("<min>", String.valueOf(pd.getMinionSize())).replaceAll("<max>", String.valueOf(plugin.getAdm().getMaxMinion(p))));
    }

    private void removeItem(PlayerInventory pi, ItemStack item) {
        int remove = item.getAmount();
        for (int i = 0; i < 36; i++) {
            ItemStack Item = pi.getItem(i);
            if (Item == null || Item.getType().equals(Material.AIR)) {
                continue;
            }
            if (Item.getType().equals(item.getType()) && Item.getItemMeta().equals(item.getItemMeta()) && Item.getDurability() == item.getDurability()) {
                if (remove - Item.getAmount() <= 0) {
                    if (remove - Item.getAmount() == 0) {
                        pi.setItem(i, null);
                        break;
                    }
                    pi.getItem(i).setAmount(Math.abs(remove - Item.getAmount()));
                    break;
                } else {
                    remove = remove - Item.getAmount();
                    pi.setItem(i, null);
                }
            }
        }
    }

    public String getTime(PlayerMinionStat stat, UpgradeFuel uf) {
        long passed = System.currentTimeMillis() - stat.getFuel();
        long restant = (uf.getDuration() * stat.getAmountFuel(uf)) - passed;
        int seconds = (int) restant / 1000;
        return Utils.convertTime(seconds);
    }

    private List<String> getInfoLore(PlayerMinion pm) {
        Minion m = pm.getMinion();
        MinionLevel ml = pm.getMinionLevel();
        List<String> lore = new ArrayList<>();
        for (String s : plugin.getLang().get("menus.minion.info.loreItem").split("\\n")) {
            if (s.equalsIgnoreCase("<lore>")) {
                m.getLore().forEach(l -> lore.add(l.replaceAll("<type>", ml.getLevelTitle()).replaceAll("<generated>", String.valueOf(pm.getStat().getGenerated())).replaceAll("<max>", String.valueOf(ml.getMax())).replaceAll("<time>", String.valueOf(pm.getDelay()))));
            } else {
                lore.add(s.replaceAll("<type>", ml.getLevelTitle()).replaceAll("<generated>", String.valueOf(pm.getStat().getGenerated())).replaceAll("<max>", String.valueOf(ml.getMax())).replaceAll("<time>", String.valueOf(pm.getDelay())));
            }
        }
        return lore;
    }

}