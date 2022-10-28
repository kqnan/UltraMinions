package io.github.Leonardo0013YT.UltraMinions.api;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.craft.Craft;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerData;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class UltraMinionsAPI {

    public static ArrayList<Craft> getCrafts() {
        return Main.get().getCm().getCrafts();
    }

    public static int getAmountMinions(Player p) {
        if (PlayerData.getPlayerData(p) != null) {
            return PlayerData.getPlayerData(p).getMinionSize();
        }
        return 0;
    }

    public static PlayerData getPlayerData(Player p) {
        if (PlayerData.getPlayerData(p) != null) {
            return PlayerData.getPlayerData(p);
        }
        return null;
    }

    public static Collection<PlayerMinion> getPlayerMinions(Player p) {
        if (PlayerData.getPlayerData(p) != null) {
            return PlayerData.getPlayerData(p).getMinions().values();
        }
        return new ArrayList<>();
    }

}