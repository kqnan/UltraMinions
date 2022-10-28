package io.github.Leonardo0013YT.UltraMinions.minions.types;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionChest;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class MinionSeller extends Minion {

    private final Main plugin;

    public MinionSeller(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
        this.plugin = plugin;
    }

    @Override
    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        if (armor == null || pm == null) return;
        if (!spawn.getChunk().isLoaded()) return;
        if (pm.isChest()) {
            action.done(false);
            PlayerMinionChest pmc = pm.getChest();
            if (pmc == null || pmc.getInventory() == null) return;
            armor.setItemInHand(getHandItem());
            for (int it = 0; it < pmc.getInventory().getSize(); it++) {
                ItemStack i = pmc.getInventory().getContents()[it];
                if (i == null || i.getType().equals(Material.AIR)) continue;
                Player p = pm.getP();
                double price = plugin.getAdm().getPrice(p, i) * i.getAmount();
                if (price > 0) {
                    plugin.getAdm().addCoins(p, price);
                    pmc.getInventory().remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public boolean check(Location spawn) {
        return true;
    }

}