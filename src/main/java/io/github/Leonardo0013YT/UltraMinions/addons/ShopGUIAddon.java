package io.github.Leonardo0013YT.UltraMinions.addons;

import io.github.Leonardo0013YT.UltraMinions.interfaces.SellAddon;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopGUIAddon implements SellAddon {

    @Override
    public float getPrice(Player p, ItemStack item) {
        try {
            return (float) ShopGuiPlusApi.getItemStackPriceSell(p, item);
        } catch (Exception var4) {
            return 0.0F;
        }
    }

}