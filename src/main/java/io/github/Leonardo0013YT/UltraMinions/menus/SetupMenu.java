package io.github.Leonardo0013YT.UltraMinions.menus;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionType;
import io.github.Leonardo0013YT.UltraMinions.food.Food;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.setup.*;
import io.github.Leonardo0013YT.UltraMinions.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraMinions.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SetupMenu {

    private Collection<Integer> workbench = new ArrayList<>(Arrays.asList(12, 13, 14, 21, 22, 23, 30, 31, 32, 25));
    private HashMap<UUID, Integer> pages = new HashMap<>();
    private Main plugin;

    public SetupMenu(Main plugin) {
        this.plugin = plugin;
    }

    public void createSetupMainFoodMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.setupMainFood.title"));
        int page = pages.getOrDefault(p.getUniqueId(), 1);
        int itt = 0, counter = 0;
        for (Food food : plugin.getFm().getFoods().values()) {
            counter++;
            if (counter < (page - 1) * 36) {
                continue;
            }
            itt++;
            inv.addItem(NBTEditor.set(food.getFood(), food.getId(), "FOOD", "KEY"));
            if (itt == 36) {
                break;
            }
        }
        ItemStack next = ItemBuilder.item(Material.ARROW, plugin.getLang().get("menus.next.nameItem"), plugin.getLang().get("menus.next.loreItem"));
        ItemStack last = ItemBuilder.item(Material.ARROW, plugin.getLang().get("menus.last.nameItem"), plugin.getLang().get("menus.last.loreItem"));
        if (page > 1) {
            inv.setItem(45, last);
        }
        if (page < Utils.getMaxPages(plugin.getFm().getFoods().size(), 36)) {
            inv.setItem(53, next);
        }
        ItemStack add = ItemBuilder.item(Material.EMERALD, plugin.getLang().get("menus.setupMainFood.add.nameItem"), plugin.getLang().get("menus.setupMainFood.add.loreItem"));
        inv.setItem(49, add);
        p.openInventory(inv);
    }

    public HashMap<UUID, Integer> getPages() {
        return pages;
    }

    public void createSetupFoodMenu(Player p, SetupFood sf) {
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.setupFood.title"));
        ItemStack food = ItemBuilder.item(Material.COOKED_BEEF, plugin.getLang().get("menus.setupFood.food.nameItem"), plugin.getLang().get("menus.setupFood.food.loreItem"));
        ItemStack amount = ItemBuilder.item(Material.GOLD_NUGGET, plugin.getLang().get("menus.setupFood.amount.nameItem"), plugin.getLang().get("menus.setupFood.amount.loreItem").replaceAll("<amount>", String.valueOf(sf.getAmount())));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setupFood.save.nameItem"), plugin.getLang().get("menus.setupFood.save.loreItem"));
        inv.setItem(12, food);
        inv.setItem(14, amount);
        inv.setItem(31, save);
        p.openInventory(inv);
    }

    public void createSetupFuelMenu(Player p, SetupFuel sas) {
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.setup.setupFuel.title"));
        ItemStack name = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.setupFuel.name.nameItem"), plugin.getLang().get("menus.setup.setupFuel.name.loreItem").replaceAll("<name>", sas.getName()));
        ItemStack setCraft = ItemBuilder.item(Material.valueOf("CRAFTING_TABLE"), plugin.getLang().get("menus.setup.setupFuel.setCraft.nameItem"), plugin.getLang().get("menus.setup.setupFuel.setCraft.loreItem"));
        ItemStack isCraft = ItemBuilder.item(Material.ENDER_CHEST, plugin.getLang().get("menus.setup.setupFuel.isCraft.nameItem"), plugin.getLang().get("menus.setup.setupFuel.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
        ItemStack result = ItemBuilder.item(Material.HOPPER, plugin.getLang().get("menus.setup.setupFuel.result.nameItem"), plugin.getLang().get("menus.setup.setupFuel.result.loreItem"));
        ItemStack amount = ItemBuilder.item(Material.valueOf("SUNFLOWER"), plugin.getLang().get("menus.setup.setupFuel.amount.nameItem"), plugin.getLang().get("menus.setup.setupFuel.amount.loreItem").replaceAll("<time>", String.valueOf(sas.getDuration())));
        ItemStack percent = ItemBuilder.item(Material.valueOf("CLOCK"), plugin.getLang().get("menus.setup.setupFuel.percent.nameItem"), plugin.getLang().get("menus.setup.setupFuel.percent.loreItem").replaceAll("<percent>", String.valueOf(sas.getPercent())));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.setupFuel.save.nameItem"), plugin.getLang().get("menus.setup.setupFuel.save.loreItem"));
        inv.setItem(10, name);
        inv.setItem(12, setCraft);
        inv.setItem(13, isCraft);
        inv.setItem(14, result);
        inv.setItem(16, amount);
        inv.setItem(22, percent);
        inv.setItem(31, save);
        p.openInventory(inv);
    }

    public void updateSetupFuelMenu(SetupFuel sas, Inventory inv) {
        ItemStack isCraft = ItemBuilder.item(Material.ENDER_CHEST, plugin.getLang().get("menus.setup.setupFuel.isCraft.nameItem"), plugin.getLang().get("menus.setup.setupFuel.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
        inv.setItem(13, isCraft);
    }

    public void createSetupCompressorMenu(Player p, SetupCompressor sas) {
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.setup.setupCompressor.title"));
        ItemStack name = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.setupCompressor.name.nameItem"), plugin.getLang().get("menus.setup.setupCompressor.name.loreItem").replaceAll("<name>", sas.getName()));
        ItemStack setCraft = ItemBuilder.item(Material.valueOf("CRAFTING_TABLE"), plugin.getLang().get("menus.setup.setupCompressor.setCraft.nameItem"), plugin.getLang().get("menus.setup.setupCompressor.setCraft.loreItem"));
        ItemStack isCraft = ItemBuilder.item(Material.ENDER_CHEST, plugin.getLang().get("menus.setup.setupCompressor.isCraft.nameItem"), plugin.getLang().get("menus.setup.setupCompressor.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
        ItemStack result = ItemBuilder.item(Material.HOPPER, plugin.getLang().get("menus.setup.setupCompressor.result.nameItem"), plugin.getLang().get("menus.setup.setupCompressor.result.loreItem"));
        ItemStack amount = ItemBuilder.item(Material.valueOf("SUNFLOWER"), plugin.getLang().get("menus.setup.setupCompressor.amount.nameItem"), plugin.getLang().get("menus.setup.setupCompressor.amount.loreItem").replaceAll("<amount>", String.valueOf(sas.getAmount())));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.setupCompressor.save.nameItem"), plugin.getLang().get("menus.setup.setupCompressor.save.loreItem"));
        inv.setItem(10, name);
        inv.setItem(12, setCraft);
        inv.setItem(13, isCraft);
        inv.setItem(14, result);
        inv.setItem(16, amount);
        inv.setItem(31, save);
        p.openInventory(inv);
    }

    public void updateSetupCompressorMenu(SetupCompressor sas, Inventory inv) {
        ItemStack isCraft = ItemBuilder.item(Material.ENDER_CHEST, plugin.getLang().get("menus.setup.setupCompressor.isCraft.nameItem"), plugin.getLang().get("menus.setup.setupCompressor.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
        inv.setItem(13, isCraft);
    }

    public void createSetupAutoSmeltMenu(Player p, SetupAutoSmelt sas) {
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.setup.setupAutoSmelt.title"));
        ItemStack name = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.setupAutoSmelt.name.nameItem"), plugin.getLang().get("menus.setup.setupAutoSmelt.name.loreItem").replaceAll("<name>", sas.getName()));
        ItemStack setCraft = ItemBuilder.item(Material.valueOf("CRAFTING_TABLE"), plugin.getLang().get("menus.setup.setupAutoSmelt.setCraft.nameItem"), plugin.getLang().get("menus.setup.setupAutoSmelt.setCraft.loreItem"));
        ItemStack isCraft = ItemBuilder.item(Material.ENDER_CHEST, plugin.getLang().get("menus.setup.setupAutoSmelt.isCraft.nameItem"), plugin.getLang().get("menus.setup.setupAutoSmelt.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
        ItemStack result = ItemBuilder.item(Material.HOPPER, plugin.getLang().get("menus.setup.setupAutoSmelt.result.nameItem"), plugin.getLang().get("menus.setup.setupAutoSmelt.result.loreItem"));
        ItemStack amount = ItemBuilder.item(Material.valueOf("SUNFLOWER"), plugin.getLang().get("menus.setup.setupAutoSmelt.amount.nameItem"), plugin.getLang().get("menus.setup.setupAutoSmelt.amount.loreItem").replaceAll("<percent>", String.valueOf(sas.getPercent())));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.setupAutoSmelt.save.nameItem"), plugin.getLang().get("menus.setup.setupAutoSmelt.save.loreItem"));
        inv.setItem(10, name);
        inv.setItem(12, setCraft);
        inv.setItem(13, isCraft);
        inv.setItem(14, result);
        inv.setItem(16, amount);
        inv.setItem(31, save);
        p.openInventory(inv);
    }

    public void updateSetupAutoSmeltMenu(SetupAutoSmelt sas, Inventory inv) {
        ItemStack isCraft = ItemBuilder.item(Material.ENDER_CHEST, plugin.getLang().get("menus.setup.setupAutoSmelt.isCraft.nameItem"), plugin.getLang().get("menus.setup.setupAutoSmelt.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
        inv.setItem(13, isCraft);
    }

    public void createSetupAutoSellMenu(Player p, SetupAutoSell sas) {
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.setup.setupAutoSell.title"));
        ItemStack name = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.setupAutoSell.name.nameItem"), plugin.getLang().get("menus.setup.setupAutoSell.name.loreItem").replaceAll("<name>", sas.getName()));
        ItemStack setCraft = ItemBuilder.item(Material.valueOf("CRAFTING_TABLE"), plugin.getLang().get("menus.setup.setupAutoSell.setCraft.nameItem"), plugin.getLang().get("menus.setup.setupAutoSell.setCraft.loreItem"));
        ItemStack isCraft = ItemBuilder.item(Material.ENDER_CHEST, plugin.getLang().get("menus.setup.setupAutoSell.isCraft.nameItem"), plugin.getLang().get("menus.setup.setupAutoSell.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
        ItemStack result = ItemBuilder.item(Material.HOPPER, plugin.getLang().get("menus.setup.setupAutoSell.result.nameItem"), plugin.getLang().get("menus.setup.setupAutoSell.result.loreItem"));
        ItemStack amount = ItemBuilder.item(Material.valueOf("SUNFLOWER"), plugin.getLang().get("menus.setup.setupAutoSell.amount.nameItem"), plugin.getLang().get("menus.setup.setupAutoSell.amount.loreItem").replaceAll("<percent>", String.valueOf(sas.getSell())));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.setupAutoSell.save.nameItem"), plugin.getLang().get("menus.setup.setupAutoSell.save.loreItem"));
        inv.setItem(10, name);
        inv.setItem(12, setCraft);
        inv.setItem(13, isCraft);
        inv.setItem(14, result);
        inv.setItem(16, amount);
        inv.setItem(31, save);
        p.openInventory(inv);
    }

    public void updateSetupAutoSellMenu(SetupAutoSell sas, Inventory inv) {
        ItemStack isCraft = ItemBuilder.item(Material.ENDER_CHEST, plugin.getLang().get("menus.setup.setupAutoSell.isCraft.nameItem"), plugin.getLang().get("menus.setup.setupAutoSell.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sas.isCraft())));
        inv.setItem(13, isCraft);
    }

    public void createSetupFuelCraftMenu(Player p, SetupFuel sas) {
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.setup.fuelCraft.title"));
        ItemStack black = ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE, "§7", "§7");
        ItemStack perm = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.fuelCraft.permission.nameItem"), plugin.getLang().get("menus.setup.fuelCraft.permission.loreItem"));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.fuelCraft.save.nameItem"), plugin.getLang().get("menus.setup.fuelCraft.save.loreItem"));
        fillCrafting(p, inv, black, perm, save);
        inv.setItem(25, sas.getResult());
    }

    public void createSetupCompressorCraftMenu(Player p, SetupCompressor sas) {
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.setup.compressorCraft.title"));
        ItemStack black = ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE, "§7", "§7");
        ItemStack perm = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.compressorCraft.permission.nameItem"), plugin.getLang().get("menus.setup.compressorCraft.permission.loreItem"));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.compressorCraft.save.nameItem"), plugin.getLang().get("menus.setup.compressorCraft.save.loreItem"));
        fillCrafting(p, inv, black, perm, save);
        inv.setItem(25, sas.getResult());
    }

    public void createSetupAutoSmeltCraftMenu(Player p, SetupAutoSmelt sas) {
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.setup.autoSmeltCraft.title"));
        ItemStack black = ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE, "§7", "§7");
        ItemStack perm = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.autoSmeltCraft.permission.nameItem"), plugin.getLang().get("menus.setup.autoSmeltCraft.permission.loreItem"));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.autoSmeltCraft.save.nameItem"), plugin.getLang().get("menus.setup.autoSmeltCraft.save.loreItem"));
        fillCrafting(p, inv, black, perm, save);
        inv.setItem(25, sas.getResult());
    }

    public void createSetupAutoSellCraftMenu(Player p, SetupAutoSell sas) {
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.setup.autoSellCraft.title"));
        ItemStack black = ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE, "§7", "§7");
        ItemStack perm = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.autoSellCraft.permission.nameItem"), plugin.getLang().get("menus.setup.autoSellCraft.permission.loreItem"));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.autoSellCraft.save.nameItem"), plugin.getLang().get("menus.setup.autoSellCraft.save.loreItem"));
        fillCrafting(p, inv, black, perm, save);
        inv.setItem(25, sas.getResult());
    }

    public void createSetupMainMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.setup.title"));
        int page = pages.getOrDefault(p.getUniqueId(), 1);
        int itt = 0, counter = 0;
        for (Minion m : plugin.getMm().getMinions().values()) {
            counter++;
            if (counter < (page - 1) * 36) {
                continue;
            }
            itt++;
            inv.addItem(m.getMinionLevelByLevel(1).getMinionHead());
            if (itt == 36) {
                break;
            }
        }
        ItemStack next = ItemBuilder.item(Material.ARROW, plugin.getLang().get("menus.next.nameItem"), plugin.getLang().get("menus.next.loreItem"));
        ItemStack last = ItemBuilder.item(Material.ARROW, plugin.getLang().get("menus.last.nameItem"), plugin.getLang().get("menus.last.loreItem"));
        if (page > 1) {
            inv.setItem(45, last);
        }
        if (page < Utils.getMaxPages(plugin.getMm().getMinions().values().size(), 36)) {
            inv.setItem(53, next);
        }
        ItemStack add = ItemBuilder.item(Material.EMERALD, plugin.getLang().get("menus.setup.main.add.nameItem"), plugin.getLang().get("menus.setup.main.add.loreItem"));
        inv.setItem(49, add);
        p.openInventory(inv);
    }

    public void createSetupMinionMenu(Player p, SetupMinion sm) {
        Inventory inv = Bukkit.createInventory(null, 45, plugin.getLang().get("menus.setup.minion.title"));
        ItemStack type = ItemBuilder.item(Material.ARMOR_STAND, plugin.getLang().get("menus.setup.minion.type.nameItem"), plugin.getLang().get("menus.setup.minion.type.loreItem").replaceAll("<type>", plugin.getLang().get(sm.getType().name())));
        ItemStack key = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.minion.key.nameItem"), plugin.getLang().get("menus.setup.minion.key.loreItem").replaceAll("<key>", sm.getKey()));
        ItemStack lore = ItemBuilder.item(Material.MAP, 1, (short) 0, plugin.getLang().get("menus.setup.minion.lore.nameItem"), getLore(sm));
        ItemStack levels = ItemBuilder.item(Material.EXPERIENCE_BOTTLE, plugin.getLang().get("menus.setup.minion.levels.nameItem"), plugin.getLang().get("menus.setup.minion.levels.loreItem"));
        ItemStack place = ItemBuilder.item(Material.STONE, plugin.getLang().get("menus.setup.minion.place.nameItem"), plugin.getLang().get("menus.setup.minion.place.loreItem"));
        ItemStack entity = ItemBuilder.item(Material.CREEPER_SPAWN_EGG, 1, (short) 50, plugin.getLang().get("menus.setup.minion.entity.nameItem"), plugin.getLang().get("menus.setup.minion.entity.loreItem"));
        ItemStack give = ItemBuilder.item(Material.CHEST, plugin.getLang().get("menus.setup.minion.give.nameItem"), plugin.getLang().get("menus.setup.minion.give.loreItem"));
        ItemStack compressor = ItemBuilder.item(Material.ANVIL, plugin.getLang().get("menus.setup.minion.compressor.nameItem"), plugin.getLang().get("menus.setup.minion.compressor.loreItem"));
        ItemStack smelt = ItemBuilder.item(Material.FURNACE, plugin.getLang().get("menus.setup.minion.smelt.nameItem"), plugin.getLang().get("menus.setup.minion.smelt.loreItem"));
        ItemStack fuelEnabled = ItemBuilder.item(Material.BREAD, plugin.getLang().get("menus.setup.minion.fuelEnabled.nameItem"), plugin.getLang().get("menus.setup.minion.fuelEnabled.loreItem").replaceAll("<state>", Utils.parseBoolean(sm.isFuelEnabled())));
        ItemStack autoSellEnabled = ItemBuilder.item(Material.valueOf("SUNFLOWER"), plugin.getLang().get("menus.setup.minion.autoSellEnabled.nameItem"), plugin.getLang().get("menus.setup.minion.autoSellEnabled.loreItem").replaceAll("<state>", Utils.parseBoolean(sm.isAutoSellEnabled())));
        ItemStack smeltEnabled = ItemBuilder.item(Material.valueOf("FIRE_CHARGE"), plugin.getLang().get("menus.setup.minion.smeltEnabled.nameItem"), plugin.getLang().get("menus.setup.minion.smeltEnabled.loreItem").replaceAll("<state>", Utils.parseBoolean(sm.isSmeltEnabled())));
        ItemStack compressorEnabled = ItemBuilder.item(Material.DISPENSER, plugin.getLang().get("menus.setup.minion.compressorEnabled.nameItem"), plugin.getLang().get("menus.setup.minion.compressorEnabled.loreItem").replaceAll("<state>", Utils.parseBoolean(sm.isCompressorEnabled())));
        ItemStack priceNormal = ItemBuilder.item(Material.IRON_INGOT, plugin.getLang().get("menus.setup.minion.priceNormal.nameItem"), plugin.getLang().get("menus.setup.minion.priceNormal.loreItem").replaceAll("<price>", String.valueOf(sm.getPriceNormalSell())));
        ItemStack priceSmelt = ItemBuilder.item(Material.GOLD_INGOT, plugin.getLang().get("menus.setup.minion.priceSmelt.nameItem"), plugin.getLang().get("menus.setup.minion.priceSmelt.loreItem").replaceAll("<price>", String.valueOf(sm.getPriceSmeltedSell())));
        ItemStack priceCompressor = ItemBuilder.item(Material.REDSTONE, plugin.getLang().get("menus.setup.minion.priceCompressor.nameItem"), plugin.getLang().get("menus.setup.minion.priceCompressor.loreItem").replaceAll("<price>", String.valueOf(sm.getPriceCompressedSell())));
        ItemStack title = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.minion.minionTitle.nameItem"), plugin.getLang().get("menus.setup.minion.minionTitle.loreItem").replaceAll("<title>", sm.getTitle()));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.minion.save.nameItem"), plugin.getLang().get("menus.setup.minion.save.loreItem"));
        ItemStack red = ItemBuilder.item(Material.RED_WOOL, 1, (short) 1, plugin.getLang().get("menus.setup.minion.red.nameItem"), plugin.getLang().get("menus.setup.minion.red.loreItem").replaceAll("<amount>", String.valueOf(sm.getRed())));
        ItemStack blue = ItemBuilder.item(Material.BLUE_WOOL, 1, (short) 4, plugin.getLang().get("menus.setup.minion.blue.nameItem"), plugin.getLang().get("menus.setup.minion.blue.loreItem").replaceAll("<amount>", String.valueOf(sm.getBlue())));
        ItemStack yellow = ItemBuilder.item(Material.GREEN_WOOL, 1, (short) 10, plugin.getLang().get("menus.setup.minion.green.nameItem"), plugin.getLang().get("menus.setup.minion.green.loreItem").replaceAll("<amount>", String.valueOf(sm.getGreen())));
        if (sm.getType().equals(MinionType.CACTUSCANE) || sm.getType().equals(MinionType.MINER) || sm.getType().equals(MinionType.FARMER) || sm.getType().equals(MinionType.LUMBERJACK) || sm.getType().equals(MinionType.PEASANT)) {
            inv.setItem(0, place);
        } else {
            inv.setItem(0, entity);
        }
        inv.setItem(1, give);
        inv.setItem(2, compressor);
        inv.setItem(3, smelt);
        inv.setItem(5, fuelEnabled);
        inv.setItem(6, autoSellEnabled);
        inv.setItem(7, smeltEnabled);
        inv.setItem(8, compressorEnabled);
        inv.setItem(12, priceNormal);
        inv.setItem(13, priceSmelt);
        inv.setItem(14, priceCompressor);
        inv.setItem(21, levels);
        inv.setItem(22, type);
        inv.setItem(23, key);
        inv.setItem(30, red);
        inv.setItem(31, blue);
        inv.setItem(32, yellow);
        inv.setItem(39, title);
        inv.setItem(40, save);
        inv.setItem(41, lore);
        p.openInventory(inv);
    }

    public void updateSetupMinionMenu(SetupMinion sm, Inventory inv) {
        ItemStack place = ItemBuilder.item(Material.STONE, plugin.getLang().get("menus.setup.minion.place.nameItem"), plugin.getLang().get("menus.setup.minion.place.loreItem"));
        ItemStack entity = ItemBuilder.item(Material.valueOf("CREEPER_SPAWN_EGG"), 1, (short) 50, plugin.getLang().get("menus.setup.minion.entity.nameItem"), plugin.getLang().get("menus.setup.minion.entity.loreItem"));
        ItemStack fuelEnabled = ItemBuilder.item(Material.BREAD, plugin.getLang().get("menus.setup.minion.fuelEnabled.nameItem"), plugin.getLang().get("menus.setup.minion.fuelEnabled.loreItem").replaceAll("<state>", Utils.parseBoolean(sm.isFuelEnabled())));
        ItemStack autoSellEnabled = ItemBuilder.item(Material.valueOf("SUNFLOWER"), plugin.getLang().get("menus.setup.minion.autoSellEnabled.nameItem"), plugin.getLang().get("menus.setup.minion.autoSellEnabled.loreItem").replaceAll("<state>", Utils.parseBoolean(sm.isAutoSellEnabled())));
        ItemStack smeltEnabled = ItemBuilder.item(Material.valueOf("FIRE_CHARGE"), plugin.getLang().get("menus.setup.minion.smeltEnabled.nameItem"), plugin.getLang().get("menus.setup.minion.smeltEnabled.loreItem").replaceAll("<state>", Utils.parseBoolean(sm.isSmeltEnabled())));
        ItemStack compressorEnabled = ItemBuilder.item(Material.DISPENSER, plugin.getLang().get("menus.setup.minion.compressorEnabled.nameItem"), plugin.getLang().get("menus.setup.minion.compressorEnabled.loreItem").replaceAll("<state>", Utils.parseBoolean(sm.isCompressorEnabled())));
        if (sm.getType().equals(MinionType.CACTUSCANE) || sm.getType().equals(MinionType.MINER) || sm.getType().equals(MinionType.FARMER) || sm.getType().equals(MinionType.LUMBERJACK) || sm.getType().equals(MinionType.PEASANT)) {
            inv.setItem(0, place);
        } else {
            inv.setItem(0, entity);
        }
        inv.setItem(5, fuelEnabled);
        inv.setItem(6, autoSellEnabled);
        inv.setItem(7, smeltEnabled);
        inv.setItem(8, compressorEnabled);
    }

    public void createSetupMinionLevelMenu(Player p, SetupMinionLevel sml) {
        Inventory inv = Bukkit.createInventory(null, 45, plugin.getLang().get("menus.setup.minionLevel.title"));
        ItemStack delay = ItemBuilder.item(Material.DIAMOND_PICKAXE, plugin.getLang().get("menus.setup.minionLevel.delay.nameItem"), plugin.getLang().get("menus.setup.minionLevel.delay.loreItem").replaceAll("<amount>", String.valueOf(sml.getDelay())));
        ItemStack max = ItemBuilder.item(Material.CHEST, plugin.getLang().get("menus.setup.minionLevel.max.nameItem"), plugin.getLang().get("menus.setup.minionLevel.max.loreItem").replaceAll("<amount>", String.valueOf(sml.getMax())));
        ItemStack upgradeLevel = ItemBuilder.item(Material.ANVIL, plugin.getLang().get("menus.setup.minionLevel.upgradeLevel.nameItem"), plugin.getLang().get("menus.setup.minionLevel.upgradeLevel.loreItem").replaceAll("<amount>", String.valueOf(sml.getUpgradeLevel())));
        ItemStack upgradeCoins = ItemBuilder.item(Material.SLIME_BALL, plugin.getLang().get("menus.setup.minionLevel.upgradeCoins.nameItem"), plugin.getLang().get("menus.setup.minionLevel.upgradeCoins.loreItem").replaceAll("<amount>", String.valueOf(sml.getUpgradeCoins())));
        ItemStack health = ItemBuilder.item(Material.APPLE, plugin.getLang().get("menus.setup.minionLevel.health.nameItem"), plugin.getLang().get("menus.setup.minionLevel.health.loreItem").replaceAll("<amount>", String.valueOf(sml.getHealth())));
        ItemStack food = ItemBuilder.item(Material.COOKED_BEEF, plugin.getLang().get("menus.setup.minionLevel.food.nameItem"), plugin.getLang().get("menus.setup.minionLevel.food.loreItem").replaceAll("<amount>", String.valueOf(sml.getFood())));
        ItemStack workTime = ItemBuilder.item(Material.valueOf("WOODEN_AXE"), plugin.getLang().get("menus.setup.minionLevel.workTime.nameItem"), plugin.getLang().get("menus.setup.minionLevel.workTime.loreItem").replaceAll("<amount>", String.valueOf(sml.getWorkTime())));
        ItemStack sleep = ItemBuilder.item(Material.valueOf("RED_BED"), plugin.getLang().get("menus.setup.minionLevel.sleep.nameItem"), plugin.getLang().get("menus.setup.minionLevel.sleep.loreItem").replaceAll("<amount>", String.valueOf(sml.getSleep())));
        ItemStack setCraft = ItemBuilder.item(Material.IRON_CHESTPLATE, plugin.getLang().get("menus.setup.minionLevel.setCraft.nameItem"), plugin.getLang().get("menus.setup.minionLevel.setCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sml.isCraft())));
        ItemStack isCraft = ItemBuilder.item(Material.valueOf("CRAFTING_TABLE"), plugin.getLang().get("menus.setup.minionLevel.isCraft.nameItem"), plugin.getLang().get("menus.setup.minionLevel.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sml.isCraft())));
        ItemStack isLevel = ItemBuilder.item(Material.valueOf("EXPERIENCE_BOTTLE"), plugin.getLang().get("menus.setup.minionLevel.isLevel.nameItem"), plugin.getLang().get("menus.setup.minionLevel.isLevel.loreItem").replaceAll("<state>", Utils.parseBoolean(sml.isLevel())));
        ItemStack isCoins = ItemBuilder.item(Material.valueOf("SUNFLOWER"), plugin.getLang().get("menus.setup.minionLevel.isCoins.nameItem"), plugin.getLang().get("menus.setup.minionLevel.isCoins.loreItem").replaceAll("<state>", Utils.parseBoolean(sml.isCoins())));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.minionLevel.save.nameItem"), plugin.getLang().get("menus.setup.minionLevel.save.loreItem"));
        inv.setItem(0, delay);
        inv.setItem(1, max);
        inv.setItem(2, upgradeLevel);
        inv.setItem(3, upgradeCoins);
        inv.setItem(5, health);
        inv.setItem(6, food);
        inv.setItem(7, workTime);
        inv.setItem(8, sleep);
        inv.setItem(12, isLevel);
        inv.setItem(13, isCraft);
        inv.setItem(14, isCoins);
        inv.setItem(22, setCraft);
        inv.setItem(40, save);
        p.openInventory(inv);
    }

    public void updateSetupMinionLevelMenu(SetupMinionLevel sml, Inventory inv) {
        ItemStack isCraft = ItemBuilder.item(Material.valueOf("CRAFTING_TABLE"), plugin.getLang().get("menus.setup.minionLevel.isCraft.nameItem"), plugin.getLang().get("menus.setup.minionLevel.isCraft.loreItem").replaceAll("<state>", Utils.parseBoolean(sml.isCraft())));
        ItemStack isLevel = ItemBuilder.item(Material.valueOf("EXPERIENCE_BOTTLE"), plugin.getLang().get("menus.setup.minionLevel.isLevel.nameItem"), plugin.getLang().get("menus.setup.minionLevel.isLevel.loreItem").replaceAll("<state>", Utils.parseBoolean(sml.isLevel())));
        ItemStack isCoins = ItemBuilder.item(Material.valueOf("SUNFLOWER"), plugin.getLang().get("menus.setup.minionLevel.isCoins.nameItem"), plugin.getLang().get("menus.setup.minionLevel.isCoins.loreItem").replaceAll("<state>", Utils.parseBoolean(sml.isCoins())));
        inv.setItem(12, isCraft);
        inv.setItem(13, isLevel);
        inv.setItem(14, isCoins);
    }

    public void createSetupCraftMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.setup.craft.title"));
        ItemStack black = ItemBuilder.item(Material.valueOf("BLACK_STAINED_GLASS_PANE"), 1, (short) 15, "§7", "§7");
        ItemStack perm = ItemBuilder.item(Material.PAPER, plugin.getLang().get("menus.setup.craft.permission.nameItem"), plugin.getLang().get("menus.setup.craft.permission.loreItem"));
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.craft.save.nameItem"), plugin.getLang().get("menus.setup.craft.save.loreItem"));
        fillCrafting(p, inv, black, perm, save);
    }

    private void fillCrafting(Player p, Inventory inv, ItemStack black, ItemStack perm, ItemStack save) {
        for (int i = 0; i < 54; i++) {
            if (!workbench.contains(i)) {
                inv.setItem(i, black);
            }
        }
        inv.setItem(48, perm);
        inv.setItem(49, save);
        p.openInventory(inv);
    }

    public void createAddGiveInInvItems(Player p, SetupMinion sm) {
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.setup.giveInInv.title"));
        for (ItemStack s : sm.getGiveInInv()) {
            if (s == null || s.getType().equals(Material.AIR)) continue;
            inv.addItem(s);
        }
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.giveInInv.save.nameItem"), plugin.getLang().get("menus.setup.giveInInv.save.loreItem"));
        inv.setItem(31, save);
        p.openInventory(inv);
    }

    public void createAddSmeltItems(Player p, SetupMinion sm) {
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.setup.smelt.title"));
        for (ItemStack s : sm.getAutoSmelt()) {
            if (s == null || s.getType().equals(Material.AIR)) continue;
            inv.addItem(s);
        }
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.smelt.save.nameItem"), plugin.getLang().get("menus.setup.smelt.save.loreItem"));
        inv.setItem(31, save);
        p.openInventory(inv);
    }

    public void createAddCompressorItems(Player p, SetupMinion sm) {
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.setup.compressor.title"));
        for (ItemStack s : sm.getCompressor()) {
            if (s == null || s.getType().equals(Material.AIR)) continue;
            inv.addItem(s);
        }
        ItemStack save = ItemBuilder.item(Material.NETHER_STAR, plugin.getLang().get("menus.setup.compressor.save.nameItem"), plugin.getLang().get("menus.setup.compressor.save.loreItem"));
        inv.setItem(31, save);
        p.openInventory(inv);
    }

    public List<String> getLore(SetupMinion sm) {
        List<String> lore = new ArrayList<>();
        for (String s : plugin.getLang().get("menus.setup.minion.lore.loreItem").split("\\n")) {
            if (s.equals("<lore>")) {
                if (sm.getLore() != null) {
                    lore.addAll(sm.getLore());
                }
            } else {
                lore.add(s);
            }
        }
        return lore;
    }

    public void addPage(Player p) {
        pages.putIfAbsent(p.getUniqueId(), 1);
        pages.put(p.getUniqueId(), pages.get(p.getUniqueId()) + 1);
    }

    public void removePage(Player p) {
        pages.put(p.getUniqueId(), pages.get(p.getUniqueId()) - 1);
    }

}