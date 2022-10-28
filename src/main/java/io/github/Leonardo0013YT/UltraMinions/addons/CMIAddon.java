package io.github.Leonardo0013YT.UltraMinions.addons;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.interfaces.HologramAddon;
import io.github.Leonardo0013YT.UltraMinions.interfaces.SellAddon;
import net.Zrips.CMILib.Container.CMILocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CMIAddon implements SellAddon, HologramAddon {

    private HashMap<PlayerMinion, CMIHologram> holograms = new HashMap<>();
    private Main plugin;

    public CMIAddon(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public float getPrice(Player p, ItemStack item) {
        ItemStack var3 = item.clone();
        var3.setAmount(1);
        return CMI.getInstance().getWorthManager().getWorth(var3).getSellPrice().floatValue() * (float) item.getAmount();
    }

    @Override
    public void createHologram(PlayerMinion pm, Location spawn, List<String> lines) {
        if (!plugin.getCfm().isHologramsSystem()) return;
        CMIHologram h = new CMIHologram(UUID.randomUUID().toString(), new CMILocation(spawn.clone().add(0, 1.7 + (lines.size() * 0.3), 0)));
        for (String l : lines) {
            h.addLine(l);
        }
        CMI.getInstance().getHologramManager().addHologram(h);
        holograms.put(pm, h);
    }

    @Override
    public void deleteHologram(PlayerMinion pm) {
        if (!plugin.getCfm().isHologramsSystem()) return;
        if (holograms.containsKey(pm)) {
            CMIHologram h = holograms.get(pm);
            h.hide();
            h.remove();
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
        holograms.values().forEach(CMIHologram::remove);
        holograms.clear();
    }

}
