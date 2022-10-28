package io.github.Leonardo0013YT.UltraMinions.addons;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.entity.Player;

public class TownyAddon {

    public boolean isInYourTown(Player p) {
        try {
            Resident resident = TownyAPI.getInstance().getDataSource().getResident(p.getName());
            Town town = TownyAPI.getInstance().getTownBlock(p.getLocation()).getTown();
            if (resident.getTown().equals(town)) {
                return true;
            }
        } catch (NotRegisteredException e) {
            return false;
        }
        return false;
    }

}