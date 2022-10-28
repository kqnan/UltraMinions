package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import lombok.Getter;
import org.bukkit.Sound;

@Getter
public class ConfigManager {

    private final Main plugin;
    private Sound upgrade, noUpgrade;
    private boolean secureStop, UltimateClaims, socialHolograms, optimizeOnUnloadChunk, maxMinionInData, MVdWPlaceholderAPI, placeholdersAPI, trHologram, adminBypass, hologramsSystem, stopOnlyFoodLow, goliatapi, autoSaveEnabled, chestLink, offlineWorking, plotsquaredv5, nbtTagsCrafting, cmiholograms, autoMinionEnabled, shopEnabled, debugMode, unlockingTiers, lands, massivefaction, factionsUUID, food, health, permissionToPlace, residence, essentials, shopguiplus, cmi, worldguard, preciousstones, redprotect, griefprevention, protectionstones, towny, removeMinion, openInventory, destroyToRemove, levelPermission, fabledskyblock, holograms, holographicdisplays, superiorskyblock, acidisland, askyblock, bentobox, iridiumskyblock, vault, playerpoints, luckperms, plotsquared;
    private int minutesAutoSave, defaultMaxMinion, minionLevel;
    private String minionKey;
    private double x, y, z, addX, addY, addZ;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        secureStop = plugin.getConfig().getBoolean("secureStop");
        UltimateClaims = plugin.getConfig().getBoolean("addons.UltimateClaims");
        socialHolograms = plugin.getConfig().getBoolean("socialHolograms");
        optimizeOnUnloadChunk = plugin.getConfig().getBoolean("optimizeOnUnloadChunk");
        MVdWPlaceholderAPI = plugin.getConfig().getBoolean("addons.MVdWPlaceholderAPI");
        placeholdersAPI = plugin.getConfig().getBoolean("addons.PlaceholderAPI");
        adminBypass = plugin.getConfig().getBoolean("adminBypass");
        hologramsSystem = plugin.getConfig().getBoolean("hologramsSystem");
        autoSaveEnabled = plugin.getConfig().getBoolean("autoSave.enabled");
        minutesAutoSave = plugin.getConfig().getInt("autoSave.minutes");
        offlineWorking = plugin.getConfig().getBoolean("offlineWorking");
        nbtTagsCrafting = plugin.getConfig().getBoolean("nbtTagsCrafting");
        minionLevel = plugin.getConfig().getInt("autoSpawnMinion.minionLevel");
        addX = plugin.getConfig().getDouble("autoSpawnMinion.coordinates.x");
        addY = plugin.getConfig().getDouble("autoSpawnMinion.coordinates.y");
        addZ = plugin.getConfig().getDouble("autoSpawnMinion.coordinates.z");
        autoMinionEnabled = plugin.getConfig().getBoolean("autoSpawnMinion.enabled");
        minionKey = plugin.getConfig().getString("autoSpawnMinion.minionType");
        shopEnabled = plugin.getConfig().getBoolean("shopEnabled");
        debugMode = plugin.getConfig().getBoolean("debugMode");
        unlockingTiers = plugin.getConfig().getBoolean("unlockingTiers");
        lands = plugin.getConfig().getBoolean("addons.lands");
        massivefaction = plugin.getConfig().getBoolean("addons.massivefaction");
        factionsUUID = plugin.getConfig().getBoolean("addons.factionsUUID");
        food = plugin.getConfig().getBoolean("stats.food");
        health = plugin.getConfig().getBoolean("stats.health");
        chestLink = plugin.getConfig().getBoolean("stats.chestLink");
        stopOnlyFoodLow = plugin.getConfig().getBoolean("stats.stopOnlyFoodLow");
        maxMinionInData = plugin.getConfig().getBoolean("stats.maxMinionInData");
        x = plugin.getConfig().getDouble("rangeCheck.x");
        y = plugin.getConfig().getDouble("rangeCheck.y");
        z = plugin.getConfig().getDouble("rangeCheck.z");
        upgrade = Sound.valueOf(plugin.getConfig().getString("sounds.upgrade"));
        noUpgrade = Sound.valueOf(plugin.getConfig().getString("sounds.noUpgrade"));
        defaultMaxMinion = plugin.getConfig().getInt("settings.defaultMaxMinion");
        residence = plugin.getConfig().getBoolean("addons.residence");
        goliatapi = plugin.getConfig().getBoolean("addons.goliatapi");
        essentials = plugin.getConfig().getBoolean("addons.essentials");
        shopguiplus = plugin.getConfig().getBoolean("addons.shopguiplus");
        cmi = plugin.getConfig().getBoolean("addons.cmi");
        trHologram = plugin.getConfig().getBoolean("addons.trHologram");
        worldguard = plugin.getConfig().getBoolean("addons.worldguard");
        preciousstones = plugin.getConfig().getBoolean("addons.preciousstones");
        redprotect = plugin.getConfig().getBoolean("addons.redprotect");
        griefprevention = plugin.getConfig().getBoolean("addons.griefprevention");
        protectionstones = plugin.getConfig().getBoolean("addons.protectionstones");
        towny = plugin.getConfig().getBoolean("addons.towny");
        vault = plugin.getConfig().getBoolean("addons.vault");
        cmiholograms = plugin.getConfig().getBoolean("addons.cmiholograms");
        playerpoints = plugin.getConfig().getBoolean("addons.playerpoints");
        fabledskyblock = plugin.getConfig().getBoolean("addons.fabledskyblock");
        acidisland = plugin.getConfig().getBoolean("addons.acidisland");
        luckperms = plugin.getConfig().getBoolean("addons.luckperms");
        askyblock = plugin.getConfig().getBoolean("addons.askyblock");
        bentobox = plugin.getConfig().getBoolean("addons.bentobox");
        plotsquared = plugin.getConfig().getBoolean("addons.plotsquared");
        plotsquaredv5 = plugin.getConfig().getBoolean("addons.plotsquaredv5");
        levelPermission = plugin.getConfig().getBoolean("settings.levelPermission");
        iridiumskyblock = plugin.getConfig().getBoolean("addons.iridiumskyblock");
        superiorskyblock = plugin.getConfig().getBoolean("addons.superiorskyblock");
        holograms = plugin.getConfig().getBoolean("addons.holograms");
        holographicdisplays = plugin.getConfig().getBoolean("addons.holographicdisplays");
        permissionToPlace = plugin.getConfig().getBoolean("settings.permissionToPlace");
        destroyToRemove = plugin.getConfig().getBoolean("settings.destroyToRemove");
        openInventory = plugin.getConfig().getBoolean("settings.memberIsland.openInventory");
        removeMinion = plugin.getConfig().getBoolean("settings.memberIsland.removeMinion");
    }

    public boolean isAnyStat() {
        return !chestLink && !health && !food;
    }

}