package io.github.Leonardo0013YT.UltraMinions.addons;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MassiveFactionAddon implements ProtectionAddon {

    @Override
    public boolean canBuild(Player p, Location loc) {
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(loc.getBlock()));
        if (faction != null) {
            boolean has = false;
            for (MPlayer mp : faction.getMPlayers()) {
                if (mp.getUuid().equals(p.getUniqueId())) {
                    has = true;
                    break;
                }
            }
            return has;
        }

        return true;
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        return canBuild(p, b.getLocation());
    }
}
