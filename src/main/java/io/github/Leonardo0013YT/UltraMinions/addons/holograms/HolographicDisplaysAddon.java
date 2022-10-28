package io.github.Leonardo0013YT.UltraMinions.addons.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.interfaces.HologramAddon;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;

public class HolographicDisplaysAddon implements HologramAddon {

    private HashMap<PlayerMinion, Hologram> holograms = new HashMap<>();
    private Main plugin;

    public HolographicDisplaysAddon(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void createHologram(PlayerMinion pm, Location spawn, List<String> lines) {
        if (!plugin.getCfm().isHologramsSystem()) return;
        Hologram h = HologramsAPI.createHologram(Main.get(), spawn.clone().add(0, 1.4 + (lines.size() * 0.3), 0));
        for (String l : lines) {
            h.appendTextLine(l);
        }
        holograms.put(pm, h);
    }

    @Override
    public void deleteHologram(PlayerMinion pm) {
        if (!plugin.getCfm().isHologramsSystem()) return;
        if (holograms.containsKey(pm)) {
            holograms.get(pm).delete();
            holograms.remove(pm);
        }
    }

    @Override
    public boolean hasHologram(PlayerMinion pm) {
        if (!plugin.getCfm().isHologramsSystem()) return false;
        return holograms.containsKey(pm);
    }

    @Override
    public void delete() {
        if (!plugin.getCfm().isHologramsSystem()) return;
        holograms.values().forEach(Hologram::delete);
        holograms.clear();
    }

}