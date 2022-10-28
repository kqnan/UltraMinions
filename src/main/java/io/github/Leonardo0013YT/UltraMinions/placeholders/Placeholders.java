package io.github.Leonardo0013YT.UltraMinions.placeholders;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerData;
import io.github.Leonardo0013YT.UltraMinions.tiers.Tier;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {

    private Main plugin;

    public Placeholders(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "um";
    }

    @Override
    public String getName() {
        return "UltraMinions";
    }

    @Override
    public String getAuthor() {
        return "Leonardo0013YT";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String id) {
        PlayerData pd = PlayerData.getPlayerData(p);
        Tier tier = plugin.getTm().getTier(pd);
        if (id.equals("minions_size")) {
            return "" + pd.getMinionSize();
        }
        if (id.equals("max_size")) {
            if (tier == null) {
                return "0";
            }
            return "" + tier.getMax();
        }
        if (id.equals("next_required")) {
            if (plugin.getTm().getTier(pd) == null) {
                return "0";
            }
            Tier tnext = plugin.getTm().getNextTier(tier);
            return "" + tnext.getRequired();
        }
        if (id.equals("next_unlocked")) {
            return "" + pd.getUnlocked();
        }
        if (id.equals("next_restant")) {
            if (plugin.getTm().getTier(pd) == null) {
                return "0";
            }
            Tier tnext = plugin.getTm().getNextTier(tier);
            return "" + (tnext.getRequired() - pd.getUnlocked());
        }
        if (id.startsWith("unlocked")) {
            String[] s = id.split("_");
            String key = s[1];
            int level = Integer.parseInt(s[3]);
            return (pd.isUnlocked(key, level) ? plugin.getLang().get("unlocked") : plugin.getLang().get("locked"));
        }
        return null;
    }

}
