package io.github.Leonardo0013YT.UltraMinions.addons;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Worth;
import io.github.Leonardo0013YT.UltraMinions.interfaces.SellAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

public class EssentialsAddon implements SellAddon {

    private Worth ess;
    private Essentials es;

    public EssentialsAddon() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Essentials") != null) {
            this.es = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        } else {
            this.es = (Essentials) Bukkit.getPluginManager().getPlugin("EssentialsX");
        }
        this.ess = this.es.getWorth();
    }

    @Override
    public float getPrice(Player p, ItemStack item) {
        try {
            ItemStack var3 = item.clone();
            var3.setAmount(1);
            BigDecimal var4 = this.ess.getPrice(this.es, var3);
            return var4 != null ? var4.floatValue() * (float) item.getAmount() : 0.0F;
        } catch (Exception var5) {
            return 0.0F;
        }
    }

}