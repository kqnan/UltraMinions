package io.github.Leonardo0013YT.UltraMinions.addons.holograms;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramManager;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.TextLine;
import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.interfaces.HologramAddon;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class HologramsAddon implements HologramAddon {

    private HashMap<PlayerMinion, Hologram> holograms = new HashMap<>();
    private HologramManager hologramManager;
    private Main plugin;

    public HologramsAddon(Main plugin) {
        this.hologramManager = JavaPlugin.getPlugin(HologramPlugin.class).getHologramManager();
        this.plugin = plugin;
    }

    @Override
    public void createHologram(PlayerMinion pm, Location spawn, List<String> lines) {
        if (!plugin.getCfm().isHologramsSystem()) return;
        Hologram h = new Hologram(pm.toString(), spawn.clone().add(0, 1 + (lines.size() * 0.3), 0), false);
        for (String l : lines) {
            HologramLine hl = new TextLine(h, l);
            h.addLine(hl);
        }
        h.spawn();
        hologramManager.addActiveHologram(h);
        holograms.put(pm, h);
    }

    @Override
    public void deleteHologram(PlayerMinion pm) {
        if (!plugin.getCfm().isHologramsSystem()) return;
        if (holograms.containsKey(pm)) {
            hologramManager.deleteHologram(holograms.get(pm));
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
        holograms.keySet().forEach(this::deleteHologram);
        holograms.clear();
    }

}