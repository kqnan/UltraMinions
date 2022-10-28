package io.github.Leonardo0013YT.UltraMinions.addons.protections;

import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GriefPreventionAddon implements ProtectionAddon {

    @Override
    public boolean canBuild(Player p, Location loc) {
        return GriefPrevention.instance.allowBuild(p, loc) == null;
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        return GriefPrevention.instance.allowBuild(p, b.getLocation()) == null;
    }

}