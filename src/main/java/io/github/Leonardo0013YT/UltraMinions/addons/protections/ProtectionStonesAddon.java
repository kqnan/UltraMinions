package io.github.Leonardo0013YT.UltraMinions.addons.protections;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import io.github.Leonardo0013YT.UltraMinions.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ProtectionStonesAddon {

    private WorldGuard instance = WorldGuard.getInstance();
    private Main plugin;

    public ProtectionStonesAddon(Main plugin) {
        this.plugin = plugin;
    }

    public boolean checkRegion(Player p, Location pb) {
        RegionQuery rg = this.instance.getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet ar = rg.getApplicableRegions(BukkitAdapter.adapt(pb));
        return ar.testState(WorldGuardPlugin.inst().wrapPlayer(p), Flags.BUILD);
    }

}