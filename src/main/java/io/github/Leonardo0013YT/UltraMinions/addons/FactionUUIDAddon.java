package io.github.Leonardo0013YT.UltraMinions.addons;


import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.Faction;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Deprecated
public class FactionUUIDAddon implements ProtectionAddon {

    @Override
    public boolean canBuild(Player p, Location loc) {
        return true;
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        return true;
    }
}
