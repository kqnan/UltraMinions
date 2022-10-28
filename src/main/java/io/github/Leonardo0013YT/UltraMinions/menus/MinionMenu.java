package io.github.Leonardo0013YT.UltraMinions.menus;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.craft.Craft;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionType;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.shop.ShopItem;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;
import io.github.Leonardo0013YT.UltraMinions.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraMinions.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MinionMenu {

    private final ArrayList<Integer> inv = new ArrayList<>(Arrays.asList(21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 39, 40, 41, 42, 43));
    private final ArrayList<Integer> fill = new ArrayList<>(Arrays.asList(0, 1, 2, 6, 7, 8, 9, 18, 27, 36, 45, 17, 26, 35, 44, 53, 46, 47, 48, 49, 50, 51, 52, 11, 12, 13, 14, 15, 16, 20, 29, 38));
    private final Collection<Integer> workbench = new ArrayList<>(Arrays.asList(12, 13, 14, 21, 22, 23, 30, 31, 32));
    private final HashMap<MinionType, ArrayList<int[]>> layouts = new HashMap<>();
    private final int[] m1m1m1 = new int[3], m1m1 = new int[2], m1 = new int[1];
    private final Main plugin;
    private HashMap<Player, Integer> pages = new HashMap<>();

    public MinionMenu(Main plugin) {
        this.plugin = plugin;
        String s3 = plugin.getConfig().getString("slots.upgrade.1m1m1m");
        String[] s3m = s3.split(";");
        m1m1m1[0] = Integer.parseInt(s3m[0]);
        m1m1m1[1] = Integer.parseInt(s3m[1]);
        m1m1m1[2] = Integer.parseInt(s3m[2]);
        String s2 = plugin.getConfig().getString("slots.upgrade.1m1m");
        String[] s2m = s2.split(";");
        m1m1[0] = Integer.parseInt(s2m[0]);
        m1m1[1] = Integer.parseInt(s2m[1]);
        String s1 = plugin.getConfig().getString("slots.upgrade.1m");
        String[] s1m = s1.split(";");
        m1[0] = Integer.parseInt(s1m[0]);
        // 0 is the white slots.
        // 1 Black slots.
        // 2 Fences
        layouts.put(MinionType.MINER, new ArrayList<>());
        layouts.get(MinionType.MINER).add(new int[]{2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42});
        layouts.get(MinionType.MINER).add(new int[]{0, 1, 9, 10, 18, 19, 27, 28, 36, 37, 45, 46, 47, 50, 51, 7, 8, 16, 17, 25, 26, 34, 35, 43, 44, 52, 53});
        layouts.put(MinionType.FISHER, new ArrayList<>());
        layouts.get(MinionType.FISHER).add(new int[]{2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42});
        layouts.get(MinionType.FISHER).add(new int[]{0, 1, 9, 10, 18, 19, 27, 28, 36, 37, 45, 46, 47, 50, 51, 7, 8, 16, 17, 25, 26, 34, 35, 43, 44, 52, 53});
        layouts.put(MinionType.LUMBERJACK, new ArrayList<>());
        layouts.get(MinionType.LUMBERJACK).add(new int[]{2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42});
        layouts.get(MinionType.LUMBERJACK).add(new int[]{0, 1, 9, 10, 18, 19, 27, 28, 36, 37, 45, 46, 47, 50, 51, 7, 8, 16, 17, 25, 26, 34, 35, 43, 44, 52, 53});
        layouts.put(MinionType.FARMER, new ArrayList<>());
        layouts.get(MinionType.FARMER).add(new int[]{2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42});
        layouts.get(MinionType.FARMER).add(new int[]{0, 1, 9, 10, 18, 19, 27, 28, 36, 37, 45, 46, 47, 50, 51, 7, 8, 16, 17, 25, 26, 34, 35, 43, 44, 52, 53});
        layouts.put(MinionType.PEASANT, new ArrayList<>());
        layouts.get(MinionType.PEASANT).add(new int[]{2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42});
        layouts.get(MinionType.PEASANT).add(new int[]{0, 1, 9, 10, 18, 19, 27, 28, 36, 37, 45, 46, 47, 50, 51, 7, 8, 16, 17, 25, 26, 34, 35, 43, 44, 52, 53});
        layouts.put(MinionType.HUNTER, new ArrayList<>());
        layouts.get(MinionType.HUNTER).add(new int[]{2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42});
        layouts.get(MinionType.HUNTER).add(new int[]{0, 9, 18, 27, 36, 45, 8, 17, 26, 35, 44, 53});
        layouts.get(MinionType.HUNTER).add(new int[]{1, 10, 19, 28, 37, 46, 47, 50, 51, 52, 43, 34, 25, 16, 7});
        layouts.put(MinionType.RANCHER, new ArrayList<>());
        layouts.get(MinionType.RANCHER).add(new int[]{2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42});
        layouts.get(MinionType.RANCHER).add(new int[]{0, 9, 18, 27, 36, 45, 8, 17, 26, 35, 44, 53});
        layouts.get(MinionType.RANCHER).add(new int[]{1, 10, 19, 28, 37, 46, 47, 50, 51, 52, 43, 34, 25, 16, 7});
        layouts.put(MinionType.CACTUSCANE, new ArrayList<>());
        layouts.get(MinionType.CACTUSCANE).add(new int[]{2, 4, 6, 12, 13, 14, 20, 21, 23, 24, 30, 31, 32, 38, 40, 42});
        layouts.get(MinionType.CACTUSCANE).add(new int[]{0, 1, 9, 10, 18, 19, 27, 28, 36, 37, 45, 46, 47, 50, 51, 7, 8, 16, 17, 25, 26, 34, 35, 43, 44, 52, 53});
        layouts.get(MinionType.CACTUSCANE).add(new int[]{3, 5, 11, 15, 29, 33, 39, 41});
        layouts.put(MinionType.COLLECTOR, new ArrayList<>());
        layouts.get(MinionType.COLLECTOR).add(new int[]{2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42});
        layouts.get(MinionType.COLLECTOR).add(new int[]{0, 1, 9, 10, 18, 19, 27, 28, 36, 37, 45, 46, 47, 50, 51, 7, 8, 16, 17, 25, 26, 34, 35, 43, 44, 52, 53});
    }


    public void createShopMenu(Player p) {
        int page = pages.get(p);
        Inventory inv = Bukkit.createInventory(null, 54, (plugin.getLang().get("menus.shop.title").length() > 32) ? plugin.getLang().get("menus.shop.title").substring(0, 32) : plugin.getLang().get("menus.shop.title"));
        ItemStack close = ItemBuilder.item(Material.BARRIER, plugin.getLang().get("menus.close.nameItem"), plugin.getLang().get("menus.close.loreItem"));
        ItemStack next = ItemBuilder.item(Material.ARROW, plugin.getLang().get("menus.next.nameItem"), plugin.getLang().get("menus.next.loreItem"));
        ItemStack last = ItemBuilder.item(Material.ARROW, plugin.getLang().get("menus.last.nameItem"), plugin.getLang().get("menus.last.loreItem"));
        for (ShopItem si : plugin.getShm().getShop().values()) {
            if (page != si.getPage()) continue;
            inv.setItem(si.getSlot(), si.toIcon(p));
        }
        if (page > 1) {
            inv.setItem(45, last);
        }
        if (page < plugin.getShm().getLastPage()) {
            inv.setItem(53, next);
        }
        inv.setItem(49, close);
        p.openInventory(inv);
    }

    public void createIdealLayout(Player p, PlayerMinion pm) {
        MinionType type = pm.getType();
        Inventory inv = Bukkit.createInventory(null, 54, (plugin.getLang().get("menus.layout.title").length() > 32) ? plugin.getLang().get("menus.layout.title").substring(0, 32) : plugin.getLang().get("menus.layout.title"));
        ItemStack back = ItemBuilder.item(Material.ARROW, plugin.getLang().get("menus.back.nameItem"), plugin.getLang().get("menus.back.loreItem").replaceAll("<destiny>", pm.getMinionLevel().getLevelTitle()));
        ItemStack close = ItemBuilder.item(Material.BARRIER, plugin.getLang().get("menus.close.nameItem"), plugin.getLang().get("menus.close.loreItem"));
        ItemStack black = ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE, "§7", "§7");
        ItemStack fence = ItemBuilder.item(Material.OAK_FENCE, plugin.getLang().get("menus.layout.fence.nameItem"), plugin.getLang().get("menus.layout.fence.loreItem"));
        if (type.equals(MinionType.COLLECTOR) || type.equals(MinionType.SELLER)) {
            ItemStack nothing = ItemBuilder.item(Material.WHITE_STAINED_GLASS_PANE, plugin.getLang().get("menus.layout.nothing.nameItem"), plugin.getLang().get("menus.layout.nothing.loreItem"));
            for (int i : layouts.get(MinionType.COLLECTOR).get(0)) {
                inv.setItem(i, nothing);
            }
            for (int i : layouts.get(MinionType.COLLECTOR).get(1)) {
                inv.setItem(i, black);
            }
        }
        if (type.equals(MinionType.FISHER)) {
            ItemStack water = ItemBuilder.item(Material.WATER_BUCKET, plugin.getLang().get("menus.layout.water.nameItem"), plugin.getLang().get("menus.layout.water.loreItem"));
            for (int i : layouts.get(MinionType.FISHER).get(0)) {
                inv.setItem(i, water);
            }
            for (int i : layouts.get(MinionType.FISHER).get(1)) {
                inv.setItem(i, black);
            }
        }
        if (type.equals(MinionType.MINER)) {
            ItemStack air = ItemBuilder.item(Material.WHITE_STAINED_GLASS_PANE, plugin.getLang().get("menus.layout.air.nameItem"), plugin.getLang().get("menus.layout.air.loreItem"));
            for (int i : layouts.get(MinionType.MINER).get(0)) {
                inv.setItem(i, air);
            }
            for (int i : layouts.get(MinionType.MINER).get(1)) {
                inv.setItem(i, black);
            }
        }
        if (type.equals(MinionType.CACTUSCANE)) {
            ItemStack grass = ItemBuilder.item(Material.GRASS_BLOCK, plugin.getLang().get("menus.layout.grass.nameItem"), plugin.getLang().get("menus.layout.grass.loreItem"));
            ItemStack sand = ItemBuilder.item(Material.SAND, plugin.getLang().get("menus.layout.sand.nameItem"), plugin.getLang().get("menus.layout.sand.loreItem"));
            ItemStack water = ItemBuilder.item(Material.WATER_BUCKET, plugin.getLang().get("menus.layout.water.nameItem"), plugin.getLang().get("menus.layout.water.loreItem"));
            if (pm.getMinion().getPlace().getType().equals(Material.CACTUS)) {
                for (int i : layouts.get(MinionType.CACTUSCANE).get(0)) {
                    inv.setItem(i, sand);
                }
                for (int i : layouts.get(MinionType.CACTUSCANE).get(2)) {
                    inv.setItem(i, sand);
                }
            } else {
                for (int i : layouts.get(MinionType.CACTUSCANE).get(0)) {
                    inv.setItem(i, grass);
                }
                for (int i : layouts.get(MinionType.CACTUSCANE).get(2)) {
                    inv.setItem(i, water);
                }
            }
            for (int i : layouts.get(MinionType.CACTUSCANE).get(1)) {
                inv.setItem(i, black);
            }
        }
        if (type.equals(MinionType.LUMBERJACK)) {
            ItemStack grass = ItemBuilder.item(Material.GRASS_BLOCK, plugin.getLang().get("menus.layout.grass.nameItem"), plugin.getLang().get("menus.layout.grass.loreItem"));
            for (int i : layouts.get(MinionType.LUMBERJACK).get(0)) {
                inv.setItem(i, grass);
            }
            for (int i : layouts.get(MinionType.LUMBERJACK).get(1)) {
                inv.setItem(i, black);
            }
        }
        if (type.equals(MinionType.FARMER)) {
            ItemStack grass = ItemBuilder.item(Material.GRASS_BLOCK, plugin.getLang().get("menus.layout.grass.nameItem"), plugin.getLang().get("menus.layout.grass.loreItem"));
            ItemStack soulsand = ItemBuilder.item(Material.SOUL_SAND, plugin.getLang().get("menus.layout.soulsand.nameItem"), plugin.getLang().get("menus.layout.soulsand.loreItem"));
            if (pm.getMinion().getPlace().getType().equals(Material.NETHER_WART)) {
                for (int i : layouts.get(MinionType.FARMER).get(0)) {
                    inv.setItem(i, soulsand);
                }
            } else {
                for (int i : layouts.get(MinionType.FARMER).get(0)) {
                    inv.setItem(i, grass);
                }
            }
            for (int i : layouts.get(MinionType.FARMER).get(1)) {
                inv.setItem(i, black);
            }
        }
        if (type.equals(MinionType.PEASANT)) {
            ItemStack grass = ItemBuilder.item(Material.GRASS_BLOCK, plugin.getLang().get("menus.layout.grass.nameItem"), plugin.getLang().get("menus.layout.grass.loreItem"));
            for (int i : layouts.get(MinionType.PEASANT).get(0)) {
                inv.setItem(i, grass);
            }
            for (int i : layouts.get(MinionType.PEASANT).get(1)) {
                inv.setItem(i, black);
            }
        }
        if (type.equals(MinionType.HUNTER)) {
            ItemStack grass = ItemBuilder.item(Material.GRASS_BLOCK, plugin.getLang().get("menus.layout.air.nameItem"), plugin.getLang().get("menus.layout.air.loreItem"));
            for (int i : layouts.get(MinionType.HUNTER).get(0)) {
                inv.setItem(i, grass);
            }
            for (int i : layouts.get(MinionType.HUNTER).get(1)) {
                inv.setItem(i, black);
            }
            for (int i : layouts.get(MinionType.HUNTER).get(2)) {
                inv.setItem(i, fence);
            }
        }
        if (type.equals(MinionType.RANCHER)) {
            ItemStack grass = ItemBuilder.item(Material.GRASS_BLOCK, plugin.getLang().get("menus.layout.air.nameItem"), plugin.getLang().get("menus.layout.air.loreItem"));
            for (int i : layouts.get(MinionType.RANCHER).get(0)) {
                inv.setItem(i, grass);
            }
            for (int i : layouts.get(MinionType.RANCHER).get(1)) {
                inv.setItem(i, black);
            }
            for (int i : layouts.get(MinionType.RANCHER).get(2)) {
                inv.setItem(i, fence);
            }
        }
        inv.setItem(22, pm.getMinionLevel().getMinionHead());
        inv.setItem(48, back);
        inv.setItem(49, close);
        p.openInventory(inv);
    }

    public void createAdminMinionMenu(Player p, PlayerMinion pm) {
        Minion m = pm.getMinion();
        MinionLevel ml = pm.getMinionLevel();
        MinionLevel mn = ml;
        if (m.getMinionLevelByLevel(pm.getStat().getLevel() + 1) != null) {
            mn = m.getMinionLevelByLevel(pm.getStat().getLevel() + 1);
        }
        int slot = Utils.getMaxSlots(pm);
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.minion.adminTitle"));
        fillInventoryMinion(p, pm, m, ml, mn, slot, inv);
    }

    public void createMinionMenu(Player p, PlayerMinion pm) {
        Minion m = pm.getMinion();
        MinionLevel ml = pm.getMinionLevel();
        MinionLevel mn = ml;
        if (m.getMinionLevelByLevel(pm.getStat().getLevel() + 1) != null) {
            mn = m.getMinionLevelByLevel(pm.getStat().getLevel() + 1);
        }
        int slot = Utils.getMaxSlots(pm);
        String level = pm.getMinionLevel().getLevelTitle();
        Inventory inv = Bukkit.createInventory(null, 54, (level.length() > 32) ? level.substring(0, 32) : level);
        fillInventoryMinion(p, pm, m, ml, mn, slot, inv);
    }

    private void fillInventoryMinion(Player p, PlayerMinion pm, Minion m, MinionLevel ml, MinionLevel mn, int slot, Inventory inv) {
        ItemStack black = ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE, 1, (short) 15, "§7", "§7");
        ItemStack white = ItemBuilder.item(Material.WHITE_STAINED_GLASS_PANE, 1, (short) 0, plugin.getLang().get("menus.minion.blockedSlot.nameItem"), plugin.getLang().get("menus.minion.blockedSlot.loreItem"));
        ItemStack chest = ItemBuilder.item(Material.WHITE_STAINED_GLASS_PANE, 1, (short) 0, plugin.getLang().get("menus.minion.chestLinked.nameItem"), plugin.getLang().get("menus.minion.chestLinked.loreItem"));
        ItemStack layout = ItemBuilder.item(Material.REDSTONE_TORCH, 1, (short) 0, plugin.getLang().get("menus.minion.layout.nameItem"), plugin.getLang().get("menus.minion.layout.loreItem"));
        ItemStack info = ItemBuilder.createSkull(plugin.getLang().get("menus.minion.info.nameItem").replaceAll("<type>", pm.getMinionLevel().getLevelTitle()), getInfoLore(pm), ml.getUrl());
        ItemStack next = ItemBuilder.item(Material.GOLD_INGOT, 1, (short) 0, plugin.getLang().get("menus.minion.next.nameItem"), plugin.getLang().get("menus.minion.next.loreItem").replaceAll("<slotsNow>", String.valueOf(ml.getMax())).replaceAll("<timeNow>", String.valueOf(ml.getDelay())).replaceAll("<slotsNext>", (mn.equals(ml)) ? "§cMax" : String.valueOf(mn.getMax())).replaceAll("<timeNext>", (mn.equals(ml)) ? "§cMax" : String.valueOf(mn.getDelay())).replaceAll("<level>", (mn.equals(ml)) ? "§cMax" : Utils.IntegerToRomanNumeral(mn.getLevel())));
        ItemStack skin = ItemBuilder.item(Material.GREEN_STAINED_GLASS_PANE, 1, (short) 5, plugin.getLang().get("menus.minion.skin.nameItem"), plugin.getLang().get("menus.minion.skin.loreItem"));
        ItemStack fuel = ItemBuilder.item(Material.ORANGE_STAINED_GLASS_PANE, 1, (short) 1, plugin.getLang().get("menus.minion.fuel.nameItem"), plugin.getLang().get("menus.minion.fuel.loreItem"));
        ItemStack shipping = ItemBuilder.item(Material.BLUE_STAINED_GLASS_PANE, 1, (short) 11, plugin.getLang().get("menus.minion.shipping.nameItem"), plugin.getLang().get("menus.minion.shipping.loreItem"));
        ItemStack update = ItemBuilder.item(Material.YELLOW_STAINED_GLASS_PANE, 1, (short) 4, plugin.getLang().get("menus.minion.update.nameItem"), plugin.getLang().get("menus.minion.update.loreItem"));
        ItemStack collect = ItemBuilder.item(Material.CHEST, 1, (short) 0, plugin.getLang().get("menus.minion.collect.nameItem"), plugin.getLang().get("menus.minion.collect.loreItem"));
        ItemStack pickup = ItemBuilder.item(Material.BEDROCK, 1, (short) 0, plugin.getLang().get("menus.minion.pickup.nameItem"), plugin.getLang().get("menus.minion.pickup.loreItem"));
        fill.forEach(f -> inv.setItem(f, black));
        if (!ml.equals(mn)) {
            boolean has = false;
            Craft craft = mn.getCraft();
            if (craft != null) {
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
                ItemStack quick = ItemBuilder.item(Material.DIAMOND, 1, (short) 0, plugin.getLang().get("menus.minion.quick.nameItem"), plugin.getLang().get("menus.minion.quick.loreItem").replaceAll("<status>", (has) ? plugin.getLang().get("menus.minion.quick.has") : plugin.getLang().get("menus.minion.quick.noHas")).replaceAll("<slotsNow>", String.valueOf(mn.getMax())).replaceAll("<timeNow>", String.valueOf(mn.getDelay())).replaceAll("<slotsNext>", (mn.equals(ml)) ? "§cMax" : String.valueOf(ml.getMax())).replaceAll("<timeNext>", (mn.equals(ml)) ? "§cMax" : String.valueOf(ml.getDelay())));
                inv.setItem(50, quick);
            }
        }
        boolean special = pm.getType().equals(MinionType.COLLECTOR) || pm.getType().equals(MinionType.SELLER);
        if (!special) {
            for (int i = 0; i < this.inv.size(); i++) {
                if (i >= slot) {
                    inv.setItem(this.inv.get(i), white);
                }
            }
        } else {
            for (int integer : this.inv) {
                inv.setItem(integer, chest);
            }
        }
        if (!pm.getSkin().equals("none") && plugin.getSkm().getSkins().get(pm.getSkin()) != null) {
            inv.setItem(10, plugin.getSkm().getSkins().get(pm.getSkin()).getHead());
        } else {
            inv.setItem(10, skin);
        }
        if (!m.isFuelEnabled()) {
            inv.setItem(19, black);
        } else if (pm.getUpgrade().getFuel() != null) {
            boolean ended = ended(pm.getStat(), pm.getUpgrade().getFuel());
            if (ended) {
                inv.setItem(19, fuel);
            } else {
                ItemStack i = NBTEditor.set(pm.getUpgrade().getFuel().getResult(true, getTime(pm.getStat(), pm.getUpgrade().getFuel())), pm.getUpgrade().getFuel().getName(), "MINION", "FUEL", "TIME");
                i.setAmount(pm.getStat().getAmountFuel(pm.getUpgrade().getFuel()));
                inv.setItem(19, i);
            }
        } else {
            inv.setItem(19, fuel);
        }
        if (!m.isAutoSellEnabled()) {
            inv.setItem(28, black);
        } else if (pm.getUpgrade().getAutoSell() != null) {
            inv.setItem(28, pm.getUpgrade().getAutoSell().getResult());
        } else {
            inv.setItem(28, shipping);
        }
        if (!m.isCompressorEnabled()) {
            inv.setItem(37, black);
        } else if (pm.getUpgrade().getCompressor() != null) {
            inv.setItem(37, pm.getUpgrade().getCompressor().getResult());
        } else {
            inv.setItem(37, update);
        }
        if (!m.isSmeltEnabled()) {
            inv.setItem(46, black);
        } else if (pm.getUpgrade().getAutoSmelt() != null) {
            inv.setItem(46, pm.getUpgrade().getAutoSmelt().getResult());
        } else {
            inv.setItem(46, update);
        }
        inv.setItem(3, layout);
        inv.setItem(4, info);
        inv.setItem(5, next);
        inv.setItem(48, collect);
        inv.setItem(53, pickup);
        if (!special) {
            for (Map.Entry<ItemStack, Integer> entry : pm.getItems().entrySet()) {
                if (entry.getKey() == null || entry.getKey().getType().equals(Material.AIR)) continue;
                int stack = entry.getValue() / 64;
                int rest = entry.getValue() % 64;
                for (int i = 0; i < stack; i++) {
                    ItemStack item = entry.getKey().clone();
                    item.setAmount(64);
                    inv.addItem(item);
                }
                if (rest > 0) {
                    ItemStack add = entry.getKey().clone();
                    add.setAmount(rest);
                    inv.addItem(add);
                }
            }
        }
        p.openInventory(inv);
    }

    public void createMinionLevelMenu(Player p, PlayerMinion pm, MinionLevel next) {
        Inventory inv = Bukkit.createInventory(null, 27, plugin.getLang().get("menus.upgrade.title").replaceAll("<title>", pm.getMinion().getTitle()));
        ItemStack level = ItemBuilder.item(Material.EXPERIENCE_BOTTLE, plugin.getLang().get("menus.upgrade.level.nameItem"), plugin.getLang().get("menus.upgrade.level.loreItem").replaceAll("<amount>", String.valueOf(next.getUpgradeLevels())).replaceAll("<level>", Utils.IntegerToRomanNumeral(next.getLevel())));
        ItemStack coins = ItemBuilder.item(Material.SUNFLOWER, plugin.getLang().get("menus.upgrade.coins.nameItem"), plugin.getLang().get("menus.upgrade.coins.loreItem").replaceAll("<amount>", String.valueOf(next.getUpgradeCoins())).replaceAll("<level>", Utils.IntegerToRomanNumeral(next.getLevel())));
        ItemStack craft = ItemBuilder.item(Material.CRAFTING_TABLE, plugin.getLang().get("menus.upgrade.craft.nameItem"), plugin.getLang().get("menus.upgrade.craft.loreItem"));
        int u = next.getUpgrades();
        if (u == 3) {
            inv.setItem(m1m1m1[0], level);
            inv.setItem(m1m1m1[1], coins);
            inv.setItem(m1m1m1[2], craft);
        } else if (u == 2) {
            if (next.isCoins() && next.isLevel()) {
                inv.setItem(m1m1[0], coins);
                inv.setItem(m1m1[1], level);
            } else if (next.isCoins() && next.isCraft()) {
                inv.setItem(m1m1[0], coins);
                inv.setItem(m1m1[1], craft);
            } else {
                inv.setItem(m1m1[0], level);
                inv.setItem(m1m1[1], craft);
            }
        } else {
            if (next.isCoins()) {
                inv.setItem(m1[0], coins);
            } else if (next.isLevel()) {
                inv.setItem(m1[0], level);
            } else {
                inv.setItem(m1[0], craft);
            }
        }
        p.openInventory(inv);
    }

    public void createPreviewCraftMenu(final Player p, final PlayerMinion pm, MinionLevel l) {
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.craft.title").replaceAll("<title>", pm.getMinion().getTitle()));
        ItemStack orange = ItemBuilder.item(Material.ORANGE_STAINED_GLASS_PANE, 1, (short) 15, "§7", "§7");
        ItemStack upgrade = ItemBuilder.item(Material.SUNFLOWER, 1, (short) 0, plugin.getLang().get("menus.craft.upgrade.nameItem"), plugin.getLang().get("menus.craft.upgrade.loreItem"));
        Craft craft = l.getCraft();
        for (int i = 0; i < 54; i++) {
            if (!workbench.contains(i)) {
                inv.setItem(i, orange);
            }
        }
        ItemStack[] m = craft.getMatrix();
        inv.setItem(12, m[0]);
        inv.setItem(13, m[1]);
        inv.setItem(14, m[2]);
        inv.setItem(21, m[3]);
        inv.setItem(22, m[4]);
        inv.setItem(23, m[5]);
        inv.setItem(30, m[6]);
        inv.setItem(31, m[7]);
        inv.setItem(32, m[8]);
        inv.setItem(49, upgrade);
        p.openInventory(inv);
    }

    public void createMinionStat(Player p, PlayerMinion pm) {
        Inventory inv = Bukkit.createInventory(null, 27, plugin.getLang().get("menus.stat.title"));
        ItemStack chest;
        if (pm.getChest() != null) {
            chest = ItemBuilder.item(Material.CHEST, plugin.getLang().get("menus.stat.chest.nameItem"), plugin.getLang().get("menus.stat.chest.loreItem").replaceAll("<chest>", Utils.getFormatedLocation(pm.getChest().getLoc())));
        } else {
            chest = ItemBuilder.item(Material.CHEST, plugin.getLang().get("menus.stat.chest.nameItem"), plugin.getLang().get("menus.stat.chest.loreItem").replaceAll("<chest>", "§cNot set!"));
        }
        if (plugin.getCfm().isFood()) {
            inv.setItem(11, ItemBuilder.item(Material.BREAD, plugin.getLang().get("menus.stat.food.nameItem"), plugin.getLang().get("menus.stat.food.loreItem").replaceAll("<max>", String.valueOf(pm.getMinionLevel().getFood())).replaceAll("<food>", String.valueOf(pm.getStat().getFood()))));
        }
        if (plugin.getCfm().isHealth()) {
            inv.setItem(15, ItemBuilder.item(Material.APPLE, plugin.getLang().get("menus.stat.health.nameItem"), plugin.getLang().get("menus.stat.health.loreItem").replaceAll("<max>", String.valueOf(pm.getMinionLevel().getHealth())).replaceAll("<health>", String.valueOf(pm.getStat().getHealth()))));
        }
        if (plugin.getCfm().isChestLink()) {
            inv.setItem(13, chest);
        }
        p.openInventory(inv);
    }

    public boolean isInv(int slot) {
        return inv.contains(slot);
    }

    public String getTime(PlayerMinionStat stat, UpgradeFuel uf) {
        long passed = System.currentTimeMillis() - stat.getFuel();
        long restant = (uf.getDuration() * stat.getAmountFuel(uf)) - passed;
        int seconds = (int) restant / 1000;
        return Utils.convertTime(seconds);
    }

    public boolean ended(PlayerMinionStat stat, UpgradeFuel uf) {
        long passed = System.currentTimeMillis() - stat.getFuel();
        long restant = (uf.getDuration() * stat.getAmountFuel(uf)) - passed;
        return restant < 0;
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

    public HashMap<Player, Integer> getPages() {
        return pages;
    }

    public void addPage(Player p) {
        pages.put(p, pages.get(p) + 1);
    }

    public void removePage(Player p) {
        pages.put(p, pages.get(p) - 1);
    }

}