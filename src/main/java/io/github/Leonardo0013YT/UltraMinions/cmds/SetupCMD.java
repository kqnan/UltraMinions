package io.github.Leonardo0013YT.UltraMinions.cmds;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerData;
import io.github.Leonardo0013YT.UltraMinions.fanciful.FancyMessage;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.skins.MinionSkin;
import io.github.Leonardo0013YT.UltraMinions.setup.SetupAutoSell;
import io.github.Leonardo0013YT.UltraMinions.setup.SetupAutoSmelt;
import io.github.Leonardo0013YT.UltraMinions.setup.SetupCompressor;
import io.github.Leonardo0013YT.UltraMinions.setup.SetupFuel;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSell;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSmelt;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeCompressor;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SetupCMD implements CommandExecutor, TabExecutor {

    private Main plugin;

    public SetupCMD(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length < 1) {
                sendHelp(p);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "setmaxminion": {
                    if (args.length < 3) {
                        sendHelp(p);
                        return true;
                    }
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    Boolean x = executeMaxMinion(p, args);
                    if (x != null) return x;
                }
                case "shop":
                    if (!plugin.getCfm().isShopEnabled()) {
                        sendHelp(p);
                        return true;
                    }
                    plugin.getMem().getPages().put(p, 1);
                    plugin.getMem().createShopMenu(p);
                    break;
                case "forcesave":
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    plugin.getDb().autoSave();
                    p.sendMessage(plugin.getLang().get("messages.forceSaving"));
                    break;
                case "setup":
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    plugin.getSem().getPages().put(p.getUniqueId(), 1);
                    plugin.getSem().createSetupMainMenu(p);
                    break;
                case "food":
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    plugin.getSem().getPages().put(p.getUniqueId(), 1);
                    plugin.getSem().createSetupMainFoodMenu(p);
                    break;
                case "autosell":
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    if (plugin.getSm().isSetupAutoSell(p)) {
                        SetupAutoSell autoSell = plugin.getSm().getSetupAutoSell(p);
                        plugin.getSm().setSetupAutoSell(p, autoSell);
                        plugin.getSem().createSetupAutoSellMenu(p, autoSell);
                        return true;
                    }
                    if (args.length < 2) {
                        sendHelp(p);
                        return true;
                    }
                    String name = args[1];
                    SetupAutoSell autoSell = new SetupAutoSell(p, name);
                    plugin.getSm().setSetupAutoSell(p, autoSell);
                    plugin.getSem().createSetupAutoSellMenu(p, autoSell);
                    p.sendMessage(plugin.getLang().get("setup.autosell.create").replaceAll("<name>", name));
                    break;
                case "autosmelt":
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    if (plugin.getSm().isSetupAutoSmelt(p)) {
                        SetupAutoSmelt autoSmelt = plugin.getSm().getSetupAutoSmelt(p);
                        plugin.getSm().setSetupAutoSmelt(p, autoSmelt);
                        plugin.getSem().createSetupAutoSmeltMenu(p, autoSmelt);
                        return true;
                    }
                    if (args.length < 2) {
                        sendHelp(p);
                        return true;
                    }
                    String name2 = args[1];
                    SetupAutoSmelt autoSmelt = new SetupAutoSmelt(p, name2);
                    plugin.getSm().setSetupAutoSmelt(p, autoSmelt);
                    plugin.getSem().createSetupAutoSmeltMenu(p, autoSmelt);
                    p.sendMessage(plugin.getLang().get("setup.autosmelt.create").replaceAll("<name>", name2));
                    break;
                case "compressor":
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    if (plugin.getSm().isSetupCompressor(p)) {
                        SetupCompressor compressor = plugin.getSm().getSetupCompressor(p);
                        plugin.getSm().setSetupCompressor(p, compressor);
                        plugin.getSem().createSetupCompressorMenu(p, compressor);
                        return true;
                    }
                    if (args.length < 2) {
                        sendHelp(p);
                        return true;
                    }
                    String name3 = args[1];
                    SetupCompressor compressor = new SetupCompressor(p, name3);
                    plugin.getSm().setSetupCompressor(p, compressor);
                    plugin.getSem().createSetupCompressorMenu(p, compressor);
                    p.sendMessage(plugin.getLang().get("setup.compressor.create").replaceAll("<name>", name3));
                    break;
                case "fuel":
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    if (plugin.getSm().isSetupFuel(p)) {
                        SetupFuel fuel = plugin.getSm().getSetupFuel(p);
                        plugin.getSm().setSetupFuel(p, fuel);
                        plugin.getSem().createSetupFuelMenu(p, fuel);
                        return true;
                    }
                    if (args.length < 2) {
                        sendHelp(p);
                        return true;
                    }
                    String name4 = args[1];
                    SetupFuel fuel = new SetupFuel(p, name4);
                    plugin.getSm().setSetupFuel(p, fuel);
                    plugin.getSem().createSetupFuelMenu(p, fuel);
                    p.sendMessage(plugin.getLang().get("setup.fuel.create").replaceAll("<name>", name4));
                    break;
                case "give":
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    if (args.length < 5) {
                        sendHelp(p);
                        return true;
                    }
                    switch (args[1].toLowerCase()) {
                        case "minion":
                            String key = args[2];
                            if (!plugin.getMm().getMinions().containsKey(key)) {
                                p.sendMessage(plugin.getLang().get("messages.noMinion"));
                                return false;
                            }
                            Player on = Bukkit.getPlayer(args[3]);
                            if (on == null) {
                                p.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            int level;
                            if (args.length > 5) {
                                try {
                                    level = Integer.parseInt(args[5]);
                                } catch (NumberFormatException e) {
                                    p.sendMessage(plugin.getLang().get("messages.noNumber"));
                                    return false;
                                }
                            } else {
                                level = 1;
                            }
                            Minion m = plugin.getMm().getMinion(key);
                            if (m.getMinionLevelByLevel(level) == null) {
                                p.sendMessage(plugin.getLang().get("messages.noExistLevel"));
                                return true;
                            }
                            ItemStack head = m.getMinionLevelByLevel(level).getMinionHead();
                            for (int i = 0; i < amount; i++) {
                                on.getInventory().addItem(head);
                            }
                            p.sendMessage(plugin.getLang().get("messages.giveMinion").replaceAll("<key>", key).replaceAll("<amount>", String.valueOf(amount)));
                            break;
                        case "fuel":
                            String key2 = args[2];
                            if (!plugin.getUm().getFuel().containsKey(key2)) {
                                p.sendMessage(plugin.getLang().get("messages.noFuel"));
                                return false;
                            }
                            Player on2 = Bukkit.getPlayer(args[3]);
                            if (on2 == null) {
                                p.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            UpgradeFuel m2 = plugin.getUm().getFuel(key2);
                            ItemStack head2 = m2.getResult(false);
                            for (int i = 0; i < amount2; i++) {
                                on2.getInventory().addItem(head2);
                            }
                            p.sendMessage(plugin.getLang().get("messages.giveFuel").replaceAll("<key>", key2).replaceAll("<amount>", String.valueOf(amount2)));
                            break;
                        case "autosell":
                            String key3 = args[2];
                            if (!plugin.getUm().getAutoSell().containsKey(key3)) {
                                p.sendMessage(plugin.getLang().get("messages.noAutoSell"));
                                return false;
                            }
                            Player on3 = Bukkit.getPlayer(args[3]);
                            if (on3 == null) {
                                p.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount3;
                            try {
                                amount3 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            UpgradeAutoSell uas = plugin.getUm().getAutoSell(key3);
                            ItemStack head3 = uas.getResult();
                            for (int i = 0; i < amount3; i++) {
                                on3.getInventory().addItem(head3);
                            }
                            p.sendMessage(plugin.getLang().get("messages.giveAutoSell").replaceAll("<key>", key3).replaceAll("<amount>", String.valueOf(amount3)));
                            break;
                        case "autosmelt":
                            String key4 = args[2];
                            if (!plugin.getUm().getAutoSmelt().containsKey(key4)) {
                                p.sendMessage(plugin.getLang().get("messages.noAutoSmelt"));
                                return false;
                            }
                            Player on4 = Bukkit.getPlayer(args[3]);
                            if (on4 == null) {
                                p.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount4;
                            try {
                                amount4 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            UpgradeAutoSmelt usm = plugin.getUm().getAutoSmelt(key4);
                            ItemStack head4 = usm.getResult();
                            for (int i = 0; i < amount4; i++) {
                                on4.getInventory().addItem(head4);
                            }
                            p.sendMessage(plugin.getLang().get("messages.giveAutoSmelt").replaceAll("<key>", key4).replaceAll("<amount>", String.valueOf(amount4)));
                            break;
                        case "compressor":
                            String key5 = args[2];
                            if (!plugin.getUm().getCompressor().containsKey(key5)) {
                                p.sendMessage(plugin.getLang().get("messages.noCompressor"));
                                return false;
                            }
                            Player on5 = Bukkit.getPlayer(args[3]);
                            if (on5 == null) {
                                p.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount5;
                            try {
                                amount5 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            UpgradeCompressor uc = plugin.getUm().getCompressor(key5);
                            ItemStack head5 = uc.getResult();
                            for (int i = 0; i < amount5; i++) {
                                on5.getInventory().addItem(head5);
                            }
                            p.sendMessage(plugin.getLang().get("messages.giveMinion").replaceAll("<key>", key5).replaceAll("<amount>", String.valueOf(amount5)));
                            break;
                        case "skin":
                            String key6 = args[2];
                            if (!plugin.getSkm().getSkins().containsKey(key6)) {
                                p.sendMessage(plugin.getLang().get("messages.noCompressor"));
                                return false;
                            }
                            Player on6 = Bukkit.getPlayer(args[3]);
                            if (on6 == null) {
                                p.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount6;
                            try {
                                amount6 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            MinionSkin ms = plugin.getSkm().getSkins().get(key6);
                            ItemStack head6 = ms.getHead();
                            for (int i = 0; i < amount6; i++) {
                                on6.getInventory().addItem(head6);
                            }
                            p.sendMessage(plugin.getLang().get("messages.giveMinion").replaceAll("<key>", key6).replaceAll("<amount>", String.valueOf(amount6)));
                            break;
                        default:
                            sendHelp(p);
                            break;
                    }
                    break;
                case "reload":
                    if (!p.hasPermission("ultraminions.admin")) {
                        p.sendMessage(plugin.getLang().get("messages.noPermission"));
                        return true;
                    }
                    plugin.reload();
                    p.sendMessage(plugin.getLang().get("messages.reload"));
                    break;
                default:
                    sendHelp(p);
                    break;
            }
        } else {
            if (args.length < 1) {
                sendHelp(sender);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "setmaxminion": {
                    if (args.length < 3) {
                        sendHelp(sender);
                        return true;
                    }
                    Boolean x = executeMaxMinion(sender, args);
                    if (x != null) return x;
                }
                case "forcesave":
                    plugin.getDb().autoSave();
                    sender.sendMessage(plugin.getLang().get("messages.forceSaving"));
                    break;
                case "setup":
                case "autosell":
                case "autosmelt":
                case "compressor":
                case "fuel":
                    sender.sendMessage(plugin.getLang().get("messages.noConsole"));
                    break;
                case "shop":
                    if (!plugin.getCfm().isShopEnabled()) {
                        sendHelp(sender);
                        return true;
                    }
                    Player son = Bukkit.getPlayer(args[1]);
                    if (son == null) {
                        sender.sendMessage(plugin.getLang().get("messages.noOnline"));
                        return false;
                    }
                    plugin.getMem().getPages().put(son, 1);
                    plugin.getMem().createShopMenu(son);
                    break;
                case "give":
                    switch (args[1].toLowerCase()) {
                        case "minion":
                            if (args.length < 5) {
                                sendHelp(sender);
                                return true;
                            }
                            String key = args[2];
                            if (!plugin.getMm().getMinions().containsKey(key)) {
                                sender.sendMessage(plugin.getLang().get("messages.noMinion"));
                                return false;
                            }
                            Player on = Bukkit.getPlayer(args[3]);
                            if (on == null) {
                                sender.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            int level;
                            if (args.length > 5) {
                                try {
                                    level = Integer.parseInt(args[5]);
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(plugin.getLang().get("messages.noNumber"));
                                    return false;
                                }
                            } else {
                                level = 1;
                            }
                            Minion m = plugin.getMm().getMinion(key);
                            if (m.getMinionLevelByLevel(level) == null) {
                                sender.sendMessage(plugin.getLang().get("messages.noExistLevel"));
                                return true;
                            }
                            ItemStack head = m.getMinionLevelByLevel(level).getMinionHead();
                            for (int i = 0; i < amount; i++) {
                                on.getInventory().addItem(head);
                            }
                            sender.sendMessage(plugin.getLang().get("messages.giveMinion").replaceAll("<key>", key).replaceAll("<amount>", String.valueOf(amount)));
                            break;
                        case "fuel":
                            if (args.length < 5) {
                                sendHelp(sender);
                                return true;
                            }
                            String key2 = args[2];
                            if (!plugin.getUm().getFuel().containsKey(key2)) {
                                sender.sendMessage(plugin.getLang().get("messages.noFuel"));
                                return false;
                            }
                            Player on2 = Bukkit.getPlayer(args[3]);
                            if (on2 == null) {
                                sender.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            UpgradeFuel m2 = plugin.getUm().getFuel(key2);
                            ItemStack head2 = m2.getResult(false);
                            for (int i = 0; i < amount2; i++) {
                                on2.getInventory().addItem(head2);
                            }
                            sender.sendMessage(plugin.getLang().get("messages.giveFuel").replaceAll("<key>", key2).replaceAll("<amount>", String.valueOf(amount2)));
                            break;
                        case "autosell":
                            if (args.length < 5) {
                                sendHelp(sender);
                                return true;
                            }
                            String key3 = args[2];
                            if (!plugin.getUm().getAutoSell().containsKey(key3)) {
                                sender.sendMessage(plugin.getLang().get("messages.noAutoSell"));
                                return false;
                            }
                            Player on3 = Bukkit.getPlayer(args[3]);
                            if (on3 == null) {
                                sender.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount3;
                            try {
                                amount3 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            UpgradeAutoSell uas = plugin.getUm().getAutoSell(key3);
                            ItemStack head3 = uas.getResult();
                            for (int i = 0; i < amount3; i++) {
                                on3.getInventory().addItem(head3);
                            }
                            sender.sendMessage(plugin.getLang().get("messages.giveAutoSell").replaceAll("<key>", key3).replaceAll("<amount>", String.valueOf(amount3)));
                            break;
                        case "autosmelt":
                            if (args.length < 5) {
                                sendHelp(sender);
                                return true;
                            }
                            String key4 = args[2];
                            if (!plugin.getUm().getAutoSmelt().containsKey(key4)) {
                                sender.sendMessage(plugin.getLang().get("messages.noAutoSmelt"));
                                return false;
                            }
                            Player on4 = Bukkit.getPlayer(args[3]);
                            if (on4 == null) {
                                sender.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount4;
                            try {
                                amount4 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            UpgradeAutoSmelt usm = plugin.getUm().getAutoSmelt(key4);
                            ItemStack head4 = usm.getResult();
                            for (int i = 0; i < amount4; i++) {
                                on4.getInventory().addItem(head4);
                            }
                            sender.sendMessage(plugin.getLang().get("messages.giveAutoSmelt").replaceAll("<key>", key4).replaceAll("<amount>", String.valueOf(amount4)));
                            break;
                        case "compressor":
                            if (args.length < 5) {
                                sendHelp(sender);
                                return true;
                            }
                            String key5 = args[2];
                            if (!plugin.getUm().getCompressor().containsKey(key5)) {
                                sender.sendMessage(plugin.getLang().get("messages.noCompressor"));
                                return false;
                            }
                            Player on5 = Bukkit.getPlayer(args[3]);
                            if (on5 == null) {
                                sender.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount5;
                            try {
                                amount5 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            UpgradeCompressor uc = plugin.getUm().getCompressor(key5);
                            ItemStack head5 = uc.getResult();
                            for (int i = 0; i < amount5; i++) {
                                on5.getInventory().addItem(head5);
                            }
                            sender.sendMessage(plugin.getLang().get("messages.giveMinion").replaceAll("<key>", key5).replaceAll("<amount>", String.valueOf(amount5)));
                            break;
                        case "skin":
                            if (args.length < 5) {
                                sendHelp(sender);
                                return true;
                            }
                            String key6 = args[2];
                            if (!plugin.getSkm().getSkins().containsKey(key6)) {
                                sender.sendMessage(plugin.getLang().get("messages.noSkin"));
                                return false;
                            }
                            Player on6 = Bukkit.getPlayer(args[3]);
                            if (on6 == null) {
                                sender.sendMessage(plugin.getLang().get("messages.noOnline"));
                                return false;
                            }
                            int amount6;
                            try {
                                amount6 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("messages.noNumber"));
                                return false;
                            }
                            MinionSkin ms = plugin.getSkm().getSkins().get(key6);
                            ItemStack head6 = ms.getHead();
                            for (int i = 0; i < amount6; i++) {
                                on6.getInventory().addItem(head6);
                            }
                            sender.sendMessage(plugin.getLang().get("messages.giveSkin").replaceAll("<key>", key6).replaceAll("<amount>", String.valueOf(amount6)));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "reload":
                    plugin.reload();
                    sender.sendMessage(plugin.getLang().get("messages.reload"));
                    break;
                default:
                    sendHelp(sender);
                    break;
            }
        }
        return false;
    }

    private Boolean executeMaxMinion(CommandSender sender, String[] args) {
        Player on = Bukkit.getPlayer(args[1]);
        if (on == null) {
            sender.sendMessage(plugin.getLang().get("messages.noOnline"));
            return false;
        }
        PlayerData pd = PlayerData.getPlayerData(on);
        if (pd == null) {
            sender.sendMessage(plugin.getLang().get("messages.noOnline"));
            return false;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getLang().get("messages.noNumber"));
            return false;
        }
        pd.setMaxMinion(amount);
        return false;
    }

    private void sendHelp(Player s) {
        s.sendMessage("§7§m--------------------------------------");
        s.sendMessage("§6[] §7- §dOptional");
        new FancyMessage("§e/minions shop §b- §aOpens the minions shop.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.RUN_COMMAND, "/minions shop").send(s);
        if (s.hasPermission("ultraminions.admin")) {
            new FancyMessage("§e/minions reload §b- §aReloads the plugin.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.RUN_COMMAND, "/minions reload").send(s);
            new FancyMessage("§e/minions setup §b- §aOpens the setup inventory.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.RUN_COMMAND, "/minions setup").send(s);
            new FancyMessage("§e/minions food §b- §aOpens the food inventory.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.RUN_COMMAND, "/minions food").send(s);
            new FancyMessage("§e/minions forcesave §b- §aForce save minions data.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.RUN_COMMAND, "/minions forcesave").send(s);
            new FancyMessage("§e/minions autosell <name> §b- §aCreate a new one autosell.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions autosell ").send(s);
            new FancyMessage("§e/minions autosmelt <name> §b- §aCreate a new one autosmelt.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions autosmelt ").send(s);
            new FancyMessage("§e/minions compressor <name> §b- §aCreate a new one compressor.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions compressor ").send(s);
            new FancyMessage("§e/minions fuel <name> §b- §aCreate a new one fuel.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions fuel ").send(s);
            new FancyMessage("§e/minions setmaxminion <name> <amount> §b- §aSet max minion by data.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions setmaxminion ").send(s);
            new FancyMessage("§e/minions give minion <key> <player> <amount> [level] §b- §aI'll give §ayou §athe §aselected §aminion.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions give minion ").send(s);
            new FancyMessage("§e/minions give fuel <key> <player> <amount> §b- §aI'll give §ayou §athe §aselected §afuel.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions give fuel ").send(s);
            new FancyMessage("§e/minions give autosell <key> <player> <amount> §b- §aI'll give §ayou §athe §aselected §aautosell.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions give autosell ").send(s);
            new FancyMessage("§e/minions give autosmelt <key> <player> <amount> §b- §aI'll give §ayou §athe §aselected §aautosmelt.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions give autosmelt ").send(s);
            new FancyMessage("§e/minions give compressor <key> <player> <amount> §b- §aI'll give §ayou §athe §aselected §acompressor.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions give compressor ").send(s);
            new FancyMessage("§e/minions give skin <key> <player> <amount> §b- §aI'll give §ayou §athe §aselected §askin.").setHover(HoverEvent.Action.SHOW_TEXT, "§bClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/minions give skin ").send(s);
        }
        s.sendMessage("§7§m--------------------------------------");
    }

    private void sendHelp(CommandSender s) {
        s.sendMessage("§7§m--------------------------------------");
        s.sendMessage("§6[] §7- §dOptional");
        s.sendMessage("§e/minions shop <player> §b- §aOpens the minions shop.");
        if (s.hasPermission("ultraminions.admin")) {
            s.sendMessage("§e/minions reload §b- §aReloads the plugin.");
            s.sendMessage("§e/minions setup §b- §aOpens the setup inventory.");
            s.sendMessage("§e/minions food §b- §aOpens the food inventory.");
            s.sendMessage("§e/minions autosell <name> §b- §aCreate a new one autosell.");
            s.sendMessage("§e/minions autosmelt <name> §b- §aCreate a new one autosmelt.");
            s.sendMessage("§e/minions compressor <name> §b- §aCreate a new one compressor.");
            s.sendMessage("§e/minions fuel <name> §b- §aCreate a new one fuel.");
            s.sendMessage("§e/minions setmaxminion <name> <amount> §b- §aSet max minion by data.");
            s.sendMessage("§e/minions give minion <key> <player> <amount> [level] §b- §aI'll give you the selected minion.");
            s.sendMessage("§e/minions give fuel <key> <player> <amount> §b- §aI'll give you the selected fuel.");
            s.sendMessage("§e/minions give autosell <key> <player> <amount> §b- §aI'll give you the selected autosell.");
            s.sendMessage("§e/minions give autosmelt <key> <player> <amount> §b- §aI'll give you the selected autosmelt.");
            s.sendMessage("§e/minions give compressor <key> <player> <amount> §b- §aI'll give you the selected compressor.");
            s.sendMessage("§e/minions give skin <key> <player> <amount> §b- §aI'll give you the selected skin.");
        }
        s.sendMessage("§7§m--------------------------------------");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "shop", "setup", "food", "autosell", "autosmelt", "compressor", "fuel", "give");
        }
        if (args.length == 2) {
            String name = args[0].toLowerCase();
            switch (name) {
                case "autosell":
                case "autosmelt":
                case "compressor":
                case "fuel":
                    return Collections.singletonList("default");
                case "give":
                    return Arrays.asList("minion", "fuel", "autosell", "autosmelt", "compressor", "skin");
            }
        }
        if (args.length == 3) {
            String name = args[1].toLowerCase();
            String key = args[2].toLowerCase();
            switch (name) {
                case "minion":
                    return plugin.getMm().getMinions().keySet().stream().filter(m -> m.toLowerCase().startsWith(key)).collect(Collectors.toList());
                case "autosmelt":
                    return plugin.getUm().getAutoSmelt().keySet().stream().filter(m -> m.toLowerCase().startsWith(key)).collect(Collectors.toList());
                case "fuel":
                    return plugin.getUm().getFuel().keySet().stream().filter(m -> m.toLowerCase().startsWith(key)).collect(Collectors.toList());
                case "autosell":
                    return plugin.getUm().getAutoSell().keySet().stream().filter(m -> m.toLowerCase().startsWith(key)).collect(Collectors.toList());
                case "compressor":
                    return plugin.getUm().getCompressor().keySet().stream().filter(m -> m.toLowerCase().startsWith(key)).collect(Collectors.toList());
                case "skin":
                    return plugin.getSkm().getSkins().keySet().stream().filter(m -> m.toLowerCase().startsWith(key)).collect(Collectors.toList());
            }
        }
        if (args.length == 4) {
            String name = args[3].toLowerCase();
            List<String> online = new ArrayList<>();
            Bukkit.getOnlinePlayers().stream().filter(n -> n.getName().toLowerCase().startsWith(name)).forEach(pl -> online.add(pl.getName()));
            return online;
        }
        return new ArrayList<>();
    }

}