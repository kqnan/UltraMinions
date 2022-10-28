package io.github.Leonardo0013YT.UltraMinions.interfaces;

import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import org.bukkit.Location;

import java.util.List;

public interface HologramAddon {


    void createHologram(PlayerMinion id, Location spawn, List<String> lines);

    void deleteHologram(PlayerMinion id);

    boolean hasHologram(PlayerMinion id);

    void delete();
}