package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.addons.*;
import io.github.Leonardo0013YT.UltraMinions.addons.holograms.HologramsAddon;
import io.github.Leonardo0013YT.UltraMinions.addons.protections.*;
import io.github.Leonardo0013YT.UltraMinions.addons.skyblocks.*;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerData;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.interfaces.HologramAddon;
import io.github.Leonardo0013YT.UltraMinions.interfaces.PlaceholderAddon;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import io.github.Leonardo0013YT.UltraMinions.interfaces.SellAddon;
import io.github.Leonardo0013YT.UltraMinions.tiers.Tier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AddonManager {

    private Main plugin;
    private List<ProtectionAddon> protectionAddons = new ArrayList<>();
    private List<SellAddon> sellAddons = new ArrayList<>();
    private LuckPermsAddon lpa;
    private FabledSkyBlockAddon fsba;
    private SuperiorSkyBlockAddon ssba;
    private AcidIslandAddon aia;
    private ASkyBlockAddon asa;
    private BentoBoxAddon bba;
    private IridiumSkyBlockAddon irs;
    private PlotSquaredAddon psa;
    private PlotSquaredV5Addon psav5;
    private VaultAddon vault;
    private PlayerPointsAddon points;
    private HologramAddon ha;
    private TownyAddon towny;
    private PreciousStonesAddon pca;
    private ProtectionStonesAddon prsa;
    private PlaceholderAddon placeholder;
    private CMIAddon cmi;
    private boolean addon = false;

    public AddonManager(Main plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        if (plugin.getCfm().isMVdWPlaceholderAPI()) {
            if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
                placeholder = new MVdWPlaceholderAPIAddon();
                plugin.sendLogMessage("Hooked into §aMVdWPlaceholderAPI§e!");
            } else {
                plugin.getConfig().set("addons.MVdWPlaceholderAPI", false);
                plugin.saveConfig();
                plugin.getCm().reload();
            }
        }
        if (plugin.getCfm().isPlaceholdersAPI()) {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                placeholder = new PlaceholderAPIAddon();
                plugin.sendLogMessage("Hooked into §aPlaceholderAPI§e!");
            } else {
                plugin.getConfig().set("addons.PlaceholderAPI", false);
                plugin.saveConfig();
                plugin.getCm().reload();
            }
        }
        if (plugin.getCfm().isShopguiplus()) {
            if (Bukkit.getPluginManager().isPluginEnabled("ShopGUIPlus")) {
                sellAddons.add(new ShopGUIAddon());
            } else {
                plugin.getConfig().set("addons.shopguiplus", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isCmi() || plugin.getCfm().isCmiholograms()) {
            if (Bukkit.getPluginManager().isPluginEnabled("CMI")) {
                CMIAddon cmi = new CMIAddon(plugin);
                if (plugin.getCfm().isCmi()) {
                    sellAddons.add(cmi);
                }
                if (plugin.getCfm().isCmiholograms()) {
                    this.cmi = cmi;
                }
            } else {
                plugin.getConfig().set("addons.cmi", false);
                plugin.getConfig().set("addons.cmiholograms", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isEssentials()) {
            if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
                sellAddons.add(new EssentialsAddon());
            } else {
                plugin.getConfig().set("addons.essentials", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isUltimateClaims()) {
            if (Bukkit.getPluginManager().isPluginEnabled("UltimateClaims")) {
                protectionAddons.add(new UltimateClaimsAddon());
            } else {
                plugin.getConfig().set("addons.UltimateClaims", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isLands()) {
            if (Bukkit.getPluginManager().isPluginEnabled("Lands")) {
                protectionAddons.add(new LandsAddon());
            } else {
                plugin.getConfig().set("addons.lands", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isFactionsUUID()) {
            if (Bukkit.getPluginManager().isPluginEnabled("Factions")) {
                protectionAddons.add(new FactionUUIDAddon());
            } else {
                plugin.getConfig().set("addons.factionsUUID", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isMassivefaction()) {
            if (Bukkit.getPluginManager().isPluginEnabled("Factions")) {
                protectionAddons.add(new MassiveFactionAddon());
            } else {
                plugin.getConfig().set("addons.massivefaction", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isWorldguard()) {
            if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
                protectionAddons.add(new WorldGuardAddon());
            } else {
                plugin.getConfig().set("addons.worldguard", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isGriefprevention()) {
            if (Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
                protectionAddons.add(new GriefPreventionAddon());
            } else {
                plugin.getConfig().set("addons.griefprevention", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isResidence()) {
            if (Bukkit.getPluginManager().isPluginEnabled("Residence")) {
                protectionAddons.add(new ResidenceAddon());
            } else {
                plugin.getConfig().set("addons.residence", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isRedprotect()) {
            if (Bukkit.getPluginManager().isPluginEnabled("RedProtect")) {
                protectionAddons.add(new RedProtectAddon());
            } else {
                plugin.getConfig().set("addons.redprotect", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isPreciousstones()) {
            if (Bukkit.getPluginManager().isPluginEnabled("PreciousStones")) {
                pca = new PreciousStonesAddon();
            } else {
                plugin.getConfig().set("addons.preciousstones", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isProtectionstones()) {
            if (Bukkit.getPluginManager().isPluginEnabled("ProtectionStones")) {
                prsa = new ProtectionStonesAddon(plugin);
            } else {
                plugin.getConfig().set("addons.protectionstones", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isTowny()) {
            if (Bukkit.getPluginManager().isPluginEnabled("Towny")) {
                towny = new TownyAddon();
            } else {
                plugin.getConfig().set("addons.towny", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isTrHologram()) {
            if (Bukkit.getPluginManager().isPluginEnabled("TrHologram")) {
                ha = null;
            } else {
                plugin.getConfig().set("addons.trHologram", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isHolograms()) {
            if (Bukkit.getPluginManager().isPluginEnabled("Holograms")) {
                ha = new HologramsAddon(plugin);
            } else {
                plugin.getConfig().set("addons.holograms", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isHolographicdisplays()) {
            if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
                ha = new io.github.Leonardo0013YT.UltraMinions.addons.holograms.HolographicDisplaysAddon(plugin);
            } else {
                plugin.getConfig().set("addons.holographicdisplays", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isPlotsquared()) {
            if (Bukkit.getPluginManager().isPluginEnabled("PlotSquared")) {
                psa = new PlotSquaredAddon();
            } else {
                plugin.getConfig().set("addons.plotsquared", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isPlotsquaredv5()) {
            if (Bukkit.getPluginManager().isPluginEnabled("PlotSquared")) {
                psav5 = new PlotSquaredV5Addon();
            } else {
                plugin.getConfig().set("addons.plotsquaredv5", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isSuperiorskyblock()) {
            if (Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) {
                ssba = new SuperiorSkyBlockAddon(plugin);
                Bukkit.getServer().getPluginManager().registerEvents(ssba, plugin);
                addon = true;
            } else {
                plugin.getConfig().set("addons.superiorskyblock", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isAcidisland()) {
            if (Bukkit.getPluginManager().isPluginEnabled("AcidIsland")) {
                aia = new AcidIslandAddon(plugin);
                Bukkit.getServer().getPluginManager().registerEvents(aia, plugin);
                addon = true;
            } else {
                plugin.getConfig().set("addons.acidisland", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isAskyblock()) {
            if (Bukkit.getPluginManager().isPluginEnabled("ASkyBlock")) {
                asa = new ASkyBlockAddon(plugin);
                Bukkit.getServer().getPluginManager().registerEvents(asa, plugin);
                addon = true;
            } else {
                plugin.getConfig().set("addons.askyblock", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isBentobox()) {
            if (Bukkit.getPluginManager().isPluginEnabled("BentoBox")) {
                bba = new BentoBoxAddon(plugin);
                Bukkit.getServer().getPluginManager().registerEvents(bba, plugin);
                addon = true;
            } else {
                plugin.getConfig().set("addons.bentobox", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isIridiumskyblock()) {
            if (Bukkit.getPluginManager().isPluginEnabled("IridiumSkyblock")) {
                irs = new IridiumSkyBlockAddon(plugin);
                Bukkit.getServer().getPluginManager().registerEvents(irs, plugin);
                addon = true;
            } else {
                plugin.getConfig().set("addons.iridiumskyblock", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isVault()) {
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                vault = new VaultAddon(plugin);
            } else {
                plugin.getConfig().set("addons.vault", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isPlayerpoints()) {
            if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
                points = new PlayerPointsAddon();
            } else {
                plugin.getConfig().set("addons.playerpoints", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        if (plugin.getCfm().isLuckperms()) {
            if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
                lpa = new LuckPermsAddon(plugin);
            } else {
                plugin.getConfig().set("addons.luckperms", false);
                plugin.saveConfig();
                plugin.getCfm().reload();
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getPluginManager().isPluginEnabled("FabledSkyBlock")) {
                    fsba = new FabledSkyBlockAddon(plugin);
                    Bukkit.getServer().getPluginManager().registerEvents(fsba, plugin);
                    addon = true;
                } else {
                    plugin.getConfig().set("addons.fabledskyblock", false);
                    plugin.saveConfig();
                    plugin.getCfm().reload();
                }
            }
        }.runTaskLater(plugin, 20 * 5);
    }

    public List<String> parsePlaceholders(Player p, List<String> text) {
        if (placeholder != null) {
            return placeholder.parsePlaceholders(p, text);
        }
        return text;
    }

    public int getMaxPerType(Player p, String key) {
        int max = 0;
        for (PermissionAttachmentInfo attachmentInfo : p.getEffectivePermissions()) {
            String perm = attachmentInfo.getPermission();
            if (perm.startsWith("ultraminions.maxplace." + key)) {
                try {
                    int d = Integer.parseInt(perm.replaceFirst("ultraminions.maxplace." + key, ""));
                    if (d > max) {
                        max = d;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return max;
    }

    public PreciousStonesAddon getPca() {
        return pca;
    }

    public ProtectionStonesAddon getPrsa() {
        return prsa;
    }

    public TownyAddon getTowny() {
        return towny;
    }

    public boolean isProtect(Player p, Location loc) {
        boolean canBuild = false;
        for (ProtectionAddon pa : protectionAddons) {
            if (!pa.canBuild(p, loc)) {
                canBuild = true;
            }
        }
        return canBuild;
    }

    public boolean isPricePlugin() {
        return !sellAddons.isEmpty();
    }

    public double getPrice(Player p, ItemStack item) {
        double price = 0.0;
        for (SellAddon sa : sellAddons) {
            price = sa.getPrice(p, item);
            break;
        }
        return price;
    }

    public boolean isAddon() {
        return addon;
    }

    public boolean hasPermission(Player p, String perm) {
        if (p.isOp()) return true;
        if (lpa == null) {
            return p.hasPermission(perm);
        }
        return lpa.hasPermission(p, perm.toLowerCase());
    }

    public Tier getTier(Player p) {
        PlayerData pd = PlayerData.getPlayerUUID(p.getUniqueId());
        if (pd != null) {
            return plugin.getTm().getTier(pd);
        }
        return null;
    }

    public int getMaxMinion(Player p) {
        int maxSelected = 0;
        if (plugin.getCfm().isUnlockingTiers()) {
            PlayerData pd = PlayerData.getPlayerUUID(p.getUniqueId());
            if (pd != null) {
                maxSelected = plugin.getTm().getTier(pd).getMax();
            }
        }
        if (plugin.getCfm().isMaxMinionInData()) {
            PlayerData pd = PlayerData.getPlayerUUID(p.getUniqueId());
            if (pd != null) {
                if (maxSelected < pd.getMaxMinion()) {
                    maxSelected = pd.getMaxMinion();
                }
            }
        }
        if (p.isOp() || p.hasPermission("minions.max.*")) {
            return 999;
        }
        if (maxSelected < plugin.getCfm().getDefaultMaxMinion()) {
            maxSelected = plugin.getCfm().getDefaultMaxMinion();
        }
        for (PermissionAttachmentInfo attachmentInfo : p.getEffectivePermissions()) {
            String perm = attachmentInfo.getPermission();
            if (perm.startsWith("minions.max.")) {
                try {
                    int d = Integer.parseInt(perm.replaceFirst("minions.max.", ""));
                    if (d > maxSelected) {
                        maxSelected = d;
                    }
                } catch (NumberFormatException e) {
                    return maxSelected;
                }
            }
        }
        return maxSelected;
    }

    public boolean isAllowedPlot(Player p, Location loc) {
        if (psav5 != null) {
            return psav5.isAllowedPlot(p, loc);
        }
        return psa.isAllowedPlot(p, loc);
    }

    public boolean isStackable(Location b) {
        if (fsba != null && b != null) {
            return fsba.isStackable(b);
        }
        return false;
    }

    public boolean checkMember(Player p) {
        if (fsba != null) {
            return fsba.checkMember(p);
        }
        if (ssba != null) {
            return ssba.checkMember(p);
        }
        if (asa != null) {
            return asa.checkMember(p);
        }
        if (aia != null) {
            return aia.checkMember(p);
        }
        if (bba != null) {
            return bba.checkMember(p);
        }
        if (irs != null) {
            return irs.checkMember(p);
        }
        return false;
    }

    public void addCoins(Player p, double amount) {
        if (plugin.getCfm().isVault()) {
            vault.addCoins(p, amount);
        } else if (plugin.getCfm().isPlayerpoints()) {
            points.addCoins(p, amount);
        }
    }

    public void removeCoins(Player p, double amount) {
        if (plugin.getCfm().isVault()) {
            vault.removeCoins(p, amount);
        } else if (plugin.getCfm().isPlayerpoints()) {
            points.removeCoins(p, amount);
        }
    }

    public double getCoins(Player p) {

        if (plugin.getCfm().isVault()) {
            return vault.getCoins(p);
        } else if (plugin.getCfm().isPlayerpoints()) {
            return points.getCoins(p);
        }
        return 0;
    }

    public void createHologram(PlayerMinion pm, Location spawn, List<String> lines) {
        if (!hasHologramPlugin()) {
            return;
        }
        if (ha != null) {
            ha.createHologram(pm, spawn, lines);
        }
        if (plugin.getCfm().isCmiholograms()) {
            cmi.createHologram(pm, spawn, lines);
        }
    }

    public void deleteHologram(PlayerMinion pm) {
        if (!hasHologramPlugin()) {
            return;
        }
        if (ha != null) {
            ha.deleteHologram(pm);
        }
        if (plugin.getCfm().isCmiholograms()) {
            cmi.deleteHologram(pm);
        }
    }

    public boolean hasHologram(PlayerMinion pm) {
        if (!hasHologramPlugin()) {
            return false;
        }
        if (ha != null) {
            return ha.hasHologram(pm);
        }
        if (plugin.getCfm().isCmiholograms()) {
            return cmi.hasHologram(pm);
        }
        return false;
    }

    public void delete() {
        if (!hasHologramPlugin()) {
            return;
        }
        if (ha != null) {
            ha.delete();
        }
        if (plugin.getCfm().isCmiholograms()) {
            cmi.delete();
        }
    }

    public boolean hasEconomyPlugin() {
        if (vault != null) {
            return true;
        }
        return points != null;
    }

    public boolean hasHologramPlugin() {
        if (cmi != null) {
            return true;
        }
        return ha != null;
    }

}