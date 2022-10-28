package io.github.Leonardo0013YT.UltraMinions.addons;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import io.github.Leonardo0013YT.UltraMinions.interfaces.PlaceholderAddon;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MVdWPlaceholderAPIAddon implements PlaceholderAddon {

    @Override
    public String parsePlaceholders(Player p, String value) {
        return PlaceholderAPI.replacePlaceholders(p, value);
    }

    @Override
    public List<String> parsePlaceholders(Player p, List<String> list) {
        List<String> replaced = new ArrayList<>();
        for (String s : list) {
            replaced.add(PlaceholderAPI.replacePlaceholders(p, s));
        }
        return replaced;
    }

}