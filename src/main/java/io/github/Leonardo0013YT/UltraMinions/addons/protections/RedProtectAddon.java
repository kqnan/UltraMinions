package io.github.Leonardo0013YT.UltraMinions.addons.protections;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RedProtectAddon implements ProtectionAddon {

    @Override
    public boolean canBuild(Player p, Location loc) {
        Region r = RedProtect.get().getAPI().getRegion(loc);
        if (r != null) {
            return r.canBuild(p);
        }
        return true;
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        Region r = RedProtect.get().getAPI().getRegion(b.getLocation());
        if (r != null) {
            return r.canBuild(p);
        }
        return true;
    }

}