package io.github.Leonardo0013YT.UltraMinions.addons.protections;

import com.songoda.ultimateclaims.UltimateClaims;
import com.songoda.ultimateclaims.claim.Claim;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class UltimateClaimsAddon implements ProtectionAddon {

    @Override
    public boolean canBuild(Player p, Location loc) {
        Claim claim = UltimateClaims.getInstance().getClaimManager().getClaim(loc.getChunk());
        if (claim != null) {
            return claim.isOwnerOrMember(p);
        }
        return true;
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        Claim claim = UltimateClaims.getInstance().getClaimManager().getClaim(b.getLocation().getChunk());
        if (claim != null) {
            return claim.isOwnerOrMember(p);
        }
        return true;
    }
}