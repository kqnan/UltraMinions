package io.github.Leonardo0013YT.UltraMinions.addons;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LandsAddon implements ProtectionAddon {

    private LandsIntegration landsAddon = new LandsIntegration(Main.get(), false);
    private String key;

    public LandsAddon() {
        this.key = landsAddon.initialize();
    }

    @Override
    public boolean canBuild(Player p, Location loc) {
        Land lc = this.landsAddon.getLand(loc);
        if (lc == null) {
            return false;
        } else {
            if (lc.getOwnerUID().equals(p.getUniqueId())) {
                return true;
            }
            return lc.getTrustedPlayer(p.getUniqueId())!=null;

        }
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        return canBuild(p, b.getLocation());
    }
}
