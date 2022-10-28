package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerData;
import io.github.Leonardo0013YT.UltraMinions.tiers.Tier;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class TiersManager {

    private Main plugin;
    private HashMap<Integer, Tier> tiers = new HashMap<>();
    private int last;

    public TiersManager(Main plugin) {
        this.plugin = plugin;
        loadTiers();
    }

    public void loadTiers() {
        tiers.clear();
        if (plugin.getTiers().isSet("tiers")) {
            ConfigurationSection conf = plugin.getTiers().getConfig().getConfigurationSection("tiers");
            for (String s : conf.getKeys(false)) {
                int required = plugin.getTiers().getInt("tiers." + s + ".typesOrLevelsRequired");
                int max = plugin.getTiers().getInt("tiers." + s + ".max");
                String msg = plugin.getTiers().get("tiers." + s + ".newTier");
                int l = Integer.parseInt(s);
                tiers.put(l, new Tier(tiers.size(), required, max, msg));
                if (last < l) {
                    last = l;
                }
            }
        }
    }

    public Tier getTier(PlayerData pd) {
        Tier t = getTierByUnlocked(pd.getUnlocked());
        if (t == null) {
            return tiers.get(last);
        }
        return t;
    }

    public Tier getTierByUnlocked(int unlocked) {
        Tier next;
        for (int i = 0; i < tiers.size(); i++) {
            if (tiers.containsKey(i + 1)) {
                next = tiers.get(i + 1);
            } else {
                next = tiers.get(i);
            }
            Tier now = tiers.get(i);
            if (unlocked >= now.getRequired() && unlocked < next.getRequired()) {
                return now;
            }
        }
        return null;
    }

    public Tier getNextTier(Tier tier) {
        if (tiers.containsKey(tier.getOrder() + 1)) {
            return tiers.get(tier.getOrder() + 1);
        }
        return tiers.get(last);
    }

    public HashMap<Integer, Tier> getTiers() {
        return tiers;
    }

}