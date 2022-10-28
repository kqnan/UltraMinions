package io.github.Leonardo0013YT.UltraMinions.addons;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;

public class PlayerPointsAddon {

    private PlayerPointsAPI pointsAPI;

    public PlayerPointsAddon() {
        pointsAPI = PlayerPoints.getPlugin(PlayerPoints.class).getAPI();
    }

    public void addCoins(Player p, double amount) {
        if (pointsAPI != null) {
            pointsAPI.give(p.getUniqueId(), (int) amount);
        }
    }

    public void removeCoins(Player p, double amount) {
        if (pointsAPI != null) {
            pointsAPI.take(p.getUniqueId(), (int) amount);
        }
    }

    public double getCoins(Player p) {
        return pointsAPI.look(p.getUniqueId());
    }

}