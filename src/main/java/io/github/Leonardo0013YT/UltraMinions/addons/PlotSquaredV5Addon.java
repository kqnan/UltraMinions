package io.github.Leonardo0013YT.UltraMinions.addons;

import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.entity.Player;

public class PlotSquaredV5Addon {

    public boolean isAllowedPlot(Player p, org.bukkit.Location loc) {
        if (loc.getWorld() == null) {
            return false;
        }

        Location l = Location.at(loc.getWorld().getName(),BlockVector3.at(loc.getX(),loc.getY(),loc.getZ()), loc.getYaw(), loc.getPitch());
        Plot plot = Plot.getPlot(l);
        if (plot == null) {
            return false;
        }
        return plot.isAdded(p.getUniqueId()) || plot.isOwner(p.getUniqueId());
    }

}