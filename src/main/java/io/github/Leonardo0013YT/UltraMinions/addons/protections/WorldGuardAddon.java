package io.github.Leonardo0013YT.UltraMinions.addons.protections;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuardAddon implements ProtectionAddon {

    private WorldGuard instance = WorldGuard.getInstance();

    @Override
    public boolean canBuild(Player p, Location loc) {
        RegionQuery rg = this.instance.getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet ar = rg.getApplicableRegions(BukkitAdapter.adapt(loc));
        return ar.testState(WorldGuardPlugin.inst().wrapPlayer(p), Flags.BUILD);
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        RegionQuery rg = this.instance.getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet ar = rg.getApplicableRegions(BukkitAdapter.adapt(b.getLocation()));
        return ar.testState(WorldGuardPlugin.inst().wrapPlayer(p), Flags.BUILD);
    }

}