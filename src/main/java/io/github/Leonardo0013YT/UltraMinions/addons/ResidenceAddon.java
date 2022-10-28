package io.github.Leonardo0013YT.UltraMinions.addons;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ResidenceAddon implements ProtectionAddon {

    @Override
    public boolean canBuild(Player p, Location loc) {
        return Residence.getInstance().getPermsByLocForPlayer(loc, p).playerHas(p, Flags.build, true);
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        return Residence.getInstance().getPermsByLocForPlayer(b.getLocation(), p).playerHas(p, Flags.build, true);
    }

}