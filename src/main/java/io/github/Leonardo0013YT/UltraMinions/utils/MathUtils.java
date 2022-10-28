package io.github.Leonardo0013YT.UltraMinions.utils;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionSellItemEvent;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionType;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.MinionItem;
import io.github.Leonardo0013YT.UltraMinions.minions.MinionSell;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSell;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSmelt;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeCompressor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MathUtils {

    private PlayerMinion pm;
    private UpgradeCompressor compressor;
    private UpgradeAutoSmelt autoSmelt;
    private UpgradeAutoSell autoSell;
    private boolean isCompressor;
    private boolean isAutoSmelt;
    private boolean isAutoSell;
    private int actions, slots, spaces;
    private MinionItem normal, compressed, smelted;

    public MathUtils(PlayerMinion pm, UpgradeCompressor compressor, UpgradeAutoSmelt autoSmelt, UpgradeAutoSell autoSell) {
        this.pm = pm;
        this.compressor = compressor;
        this.autoSmelt = autoSmelt;
        this.autoSell = autoSell;
        this.isAutoSell = autoSell != null;
        this.isAutoSmelt = autoSmelt != null;
        this.isCompressor = compressor != null;
        this.actions = pm.getActions();
        this.slots = pm.getMinionLevel().getMax();
        this.spaces = slots / 64;
        if (spaces < 1) {
            spaces++;
        }
        this.normal = pm.getMinion().getGiveInInv();
        this.compressed = pm.getMinion().getCompressor();
        this.smelted = pm.getMinion().getSmelt();
    }

    public synchronized void fill() {
        if (pm.getType().equals(MinionType.COLLECTOR) || pm.getType().equals(MinionType.SELLER)) return;
        if (actions > 0) {
            pm.getItems().clear();
            Minion m = pm.getMinion();
            MinionSell ms;
            if (isAutoSmelt) {
                if (isCompressor) {
                    ms = compressed.getItems(pm, actions, spaces, compressor.getAmount(pm.getKey()), slots, smelted);
                } else {
                    ms = smelted.getItems(pm, actions, spaces, slots);
                }
            } else {
                if (isCompressor) {
                    ms = compressed.getItems(pm, actions, spaces, compressor.getAmount(pm.getKey()), slots, normal);
                } else {
                    ms = normal.getItems(pm, actions, spaces, slots);
                }
            }
            pm.getItems().putAll(ms.getItems());
            ArrayList<ItemStack> sells = new ArrayList<>(ms.getSell());
            pm.setActions(pm.getActions() - ms.getChest());
            pm.setFull(!sells.isEmpty());
            if (isAutoSell) {
                if (!sells.isEmpty()) {
                    boolean isSellAddon = Main.get().getAdm().isPricePlugin();
                    double price = 0;
                    for (ItemStack i : sells) {
                        if (isSellAddon) {
                            price += (Main.get().getAdm().getPrice(pm.getP(), i) * (autoSell.getSell() / 100)) * i.getAmount();
                        } else {
                            price += (m.getPriceNormalSell() * (autoSell.getSell() / 100)) * i.getAmount();
                        }
                    }
                    if (price > 0) {
                        MinionSellItemEvent item = new MinionSellItemEvent(pm.getP(), sells, isAutoSmelt, isCompressor, m.getPriceNormalSell(), m.getPriceSmeltedSell(), m.getPriceCompressedSell(), price);
                        Bukkit.getServer().getPluginManager().callEvent(item);
                        if (!item.isCancelled()) {
                            pm.getP().sendMessage(Main.get().getLang().get("messages.produced").replaceAll("<title>", m.getTitle()).replaceAll("<coins>", Utils.format(item.getFinalPrice())));
                            Main.get().getAdm().addCoins(pm.getP(), item.getFinalPrice());
                            pm.setActions(pm.getActions() - ms.getRealized());
                        }
                    }
                }
            }
        }
    }

    public int getSpaces() {
        return spaces;
    }
}