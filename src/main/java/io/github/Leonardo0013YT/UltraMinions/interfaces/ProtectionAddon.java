package io.github.Leonardo0013YT.UltraMinions.interfaces;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface ProtectionAddon {
    boolean canBuild(Player p, Location loc);

    boolean canBuild(Player p, Block b);
}
